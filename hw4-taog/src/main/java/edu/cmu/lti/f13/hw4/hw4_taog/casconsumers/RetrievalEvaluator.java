package edu.cmu.lti.f13.hw4.hw4_taog.casconsumers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

import org.apache.uima.cas.CAS;
import org.apache.uima.cas.CASException;
import org.apache.uima.cas.FSIterator;
import org.apache.uima.collection.CasConsumer_ImplBase;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.cas.FSList;
import org.apache.uima.jcas.cas.FSArray;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.resource.ResourceProcessException;
import org.apache.uima.util.ProcessTrace;

import edu.cmu.lti.f13.hw4.hw4_taog.casconsumers.Query;
import edu.cmu.lti.f13.hw4.hw4_taog.casconsumers.Candidate;
import edu.cmu.lti.f13.hw4.hw4_taog.typesystems.Document;
import edu.cmu.lti.f13.hw4.hw4_taog.typesystems.Token;
import edu.cmu.lti.f13.hw4.hw4_taog.utils.Utils;

/**
 * 
 * @author Cambi
 * 
 * the casConsumers class RetrivelEvaluator
 * 
 * As all the document instances will be destroyed after the processCas method
 * but some of the evaluation part should be done after that
 * the processCas created several objects to store the information and logical relations of the documents
 * And the evaluation will be done in the collectionProcessComplete method
 * 
 *   the HashMap queryHashMap is used to store the queryId and its corresponding query object
 */

public class RetrievalEvaluator extends CasConsumer_ImplBase {

	/** query id number **/
	public ArrayList<Integer> qIdList;
	
	/** query and text relevant values **/
	public ArrayList<Integer> relList;

	/** query values, maps queryID to the correspoinding object	 **/
	public HashMap<Integer, Query> queryHashMap;
		
	public void initialize() throws ResourceInitializationException {

		qIdList = new ArrayList<Integer>();

		relList = new ArrayList<Integer>();

		queryHashMap = new HashMap<Integer, Query>();
	}

	/**
	 * 
	 * @param aCas the target CAS
	 * validate the cas
	 * iterate through every document annotation
	 * create Query instances and store information inside
	 * create connections between queries and their corresponding candidate sentences
	 * and store the query id and relevance values in the list
	 */
	
	@Override
	public void processCas(CAS aCas) throws ResourceProcessException {

		JCas jcas;
		try {
			jcas =aCas.getJCas();
		} catch (CASException e) {
			throw new ResourceProcessException(e);
		}

		FSIterator it = jcas.getAnnotationIndex(Document.type).iterator();
	
		if (it.hasNext()) {
			Document doc = (Document) it.next();

			//Make sure that your previous annotators have populated this in CAS
			
			int qid = doc.getQueryID();
			int relevanceValue = doc.getRelevanceValue();
			if (relevanceValue == 99) //It's a query
			{
			  Query aQuery = new Query(doc);			  
			  queryHashMap.put(qid, aQuery);
			}
			else // a Candidate
			{
			   Candidate aCandidate = new Candidate(doc);
			   queryHashMap.get(qid).getCandidateList().add(aCandidate);
			}


			qIdList.add(doc.getQueryID());
			relList.add(doc.getRelevanceValue());
			
		}

	}

	
	/**
	 * @param arg0 the processTrace
	 * call the Query object to conduct evaluation, i.e.
	 * compute the cosine similarity of each candidate and compute the rank
	 * 
	 * then called compute_mrr() to compute the mean reciprocal rank
	 * 
	 */
	@Override
	public void collectionProcessComplete(ProcessTrace arg0)
			throws ResourceProcessException, IOException {

		super.collectionProcessComplete(arg0);
		
		for (Query aQuery : queryHashMap.values())
		{
		  aQuery.conductEvaluation();
		}
		
		
		double metric_mrr = compute_mrr();
		
		System.out.println(" (MRR) Mean Reciprocal Rank ::" + metric_mrr);
	}

	/**
	 * 
	 * @return mrr
	 * class to compute the mean reciprocal rank
	 */
	private double compute_mrr() {
		double metric_mrr=0.0;


    if (! queryHashMap.isEmpty())
    {
      for (Query aQuery:queryHashMap.values())
      {
        metric_mrr = metric_mrr + 1 / (1.0 * aQuery.getRank());
      }
      metric_mrr = metric_mrr / queryHashMap.size();
    }
		return metric_mrr;
	}

}
