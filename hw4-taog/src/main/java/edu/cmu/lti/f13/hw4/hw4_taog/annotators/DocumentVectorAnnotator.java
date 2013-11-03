package edu.cmu.lti.f13.hw4.hw4_taog.annotators;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.FSIterator;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.cas.IntegerArray;
import org.apache.uima.jcas.cas.StringArray;
import org.apache.uima.jcas.tcas.Annotation;
import org.apache.uima.jcas.cas.FSList;

import edu.cmu.lti.f13.hw4.hw4_taog.typesystems.Token;
import edu.cmu.lti.f13.hw4.hw4_taog.typesystems.Document;
import edu.cmu.lti.f13.hw4.hw4_taog.utils.Utils;

/**
 * 
 * @author Cambi
 * class that annotates the documents
 */

public class DocumentVectorAnnotator extends JCasAnnotator_ImplBase {

	@Override
	public void process(JCas jcas) throws AnalysisEngineProcessException {

		FSIterator<Annotation> iter = jcas.getAnnotationIndex().iterator();
		if (iter.isValid()) {
			iter.moveToNext();
			Document doc = (Document) iter.get();
			createTermFreqVector(jcas, doc);
		}

	}
	/**
	 * 
	 * @param jcas
	 * @param doc
	 * the param doc denotes to the document to be annotated
	 * an HashMap tokenCountMap is utilized to count the frequency of tokens efficiently
	 * and a lot of Token instances are created through according to this HashMap  
	 * and referred in the TokenList
	 * Finally, convert the TokenList to an instance of FSList to be stored in the document
	 */

	private void createTermFreqVector(JCas jcas, Document doc) {

		String docText = doc.getText();
		//TO DO: construct a vector of tokens and update the tokenList in CAS
		
		HashMap<String, Integer> tokenCountMap = new HashMap<String, Integer>();
		
		//using split() to get the tokenStrings based on spaces
		String[] tokenStringArray = docText.split("\\s");
		for (String aWord: tokenStringArray)
		{
		  //lower the case and eliminate the punctuation
		  aWord = aWord.toLowerCase();
		  int startPos=0;
		  int endPos= aWord.length()-1;
		  while (aWord.charAt(startPos)<'a' || aWord.charAt(startPos) >'z') startPos ++;
		  while (aWord.charAt(endPos)<'a' || aWord.charAt(endPos)>'z') endPos --;
		  aWord = aWord.substring(startPos, endPos+1);
		  
		  //count the number of times that aWord appears using the HashMap
		  if (tokenCountMap.containsKey(aWord))
		  {
		    tokenCountMap.put(aWord,  tokenCountMap.get(aWord)+1);
		  }
		  else
		  {
		    tokenCountMap.put(aWord, 1);
		  }
		}
		
		//Create Tokens and add them to the ArrayList
		ArrayList<Token> tokenList = new ArrayList<Token>();
		for (String aWord : tokenCountMap.keySet())
		{
//		  System.out.println("aWord: " + aWord + " , Freq: "+tokenCountMap.get(aWord) );
		  Token aToken = new Token (jcas);
		  aToken.setText(aWord);
		  aToken.setFrequency(tokenCountMap.get(aWord));
		  tokenList.add(aToken);
		}
		
		//Convert the ArrayList to FSList (is a linked list)
		//And store the FSList in the "TokenList" instance field of the doc, which denotes to a sentence
		FSList tokenFSList = Utils.fromCollectionToFSList(jcas, tokenList);
		doc.setTokenList(tokenFSList);

	}

}
