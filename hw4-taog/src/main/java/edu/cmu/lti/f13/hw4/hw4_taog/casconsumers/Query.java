package edu.cmu.lti.f13.hw4.hw4_taog.casconsumers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import edu.cmu.lti.f13.hw4.hw4_taog.typesystems.Document;
import edu.cmu.lti.f13.hw4.hw4_taog.typesystems.Token;
import java.lang.Math;
import java.util.HashMap;

/**
 * 
 * @author Cambi
 * a Query object that represent a query sentence
 * in addition to the features provided by the Sentence abstract class
 * this class implements the candidateList to store reference to the corresponding candidate sentences
 * and rank denotes to the reciprocal rank of the correct candidate
 *
 * the method to compute cosine similarity and the rank is also implemented in this class 
 *
 */
public class Query extends Sentence {

  /** an ArrayList that records the reference of the corresponding candidates */
  private ArrayList<Candidate> candidateList;
  
  /** the reciprocal rank of the correct candidate */
  private int rank;
  
  public Query(Document aQuery) {
    super(aQuery);
    rank = 0;
    candidateList = new ArrayList<Candidate>();
    
  }
    
  public ArrayList<Candidate> getCandidateList(){
    return candidateList;
  }
  
  public void setCandidateList(ArrayList<Candidate> aList){
    candidateList = aList;
  }
  
  public int getRank(){
    return rank;
  }
  
  
  /**
   * 
   * @return an integer reflects if the evaluation is successful
   * 
   * computes the cosine similarity, sorts the candidates according to the similarity
   * and computes the rank of the correct answer (answer with relevant value 1)
   * 
   */
  public int conductEvaluation()
  {
    if (candidateList.isEmpty()) return -1;
    
    
    //compute cosine similarity
    for (Candidate aCandidate:candidateList)
    {
      computeCosineSimilarity(aCandidate); 
    }
    
    //sorting
    Collections.sort(candidateList, new Comparator<Candidate>() {
      public int compare(Candidate a, Candidate b) {
        if (b.getCosineSimilarity() > a.getCosineSimilarity())
          return 1;
        return -1;
      }
    });
  //compute rank
    rank = 0;
    for (int i = 0; i < candidateList.size(); i++)
    {
      if (candidateList.get(i).getRelevantValue() == 1) rank = i+1;      
    }
    System.out.println("Rank = " + rank);
    if (rank == 0) return -1;
    return 0;
  }
  
  /**
   * 
   * @param aCandidate
   * method to compute the cosine similarity of the candidate with the query itself
   * computes the similarity value using the information stored in the tokenHashMap
   * and store the value in the cosineSimilarity field of the Candidate object; 
   */
  public void computeCosineSimilarity(Candidate aCandidate)
  {
    if (aCandidate.getTokenHashMap().isEmpty()) return;
    if (super.getTokenHashMap().isEmpty()) return;
    
    double queryNorm = 0.0;
    for (String aWord: super.getTokenHashMap().keySet() )
      queryNorm = queryNorm + super.getTokenHashMap().get(aWord)*super.getTokenHashMap().get(aWord);
    queryNorm = Math.sqrt(queryNorm);
   
    double candidateNorm = 0.0;
    for (String aWord: aCandidate.getTokenHashMap().keySet())
     candidateNorm = candidateNorm + aCandidate.getTokenHashMap().get(aWord)*aCandidate.getTokenHashMap().get(aWord);
    candidateNorm = Math.sqrt(candidateNorm);
    
    
    double cosineSimilarity = 0.0;
    for (String aWord: aCandidate.getTokenHashMap().keySet())
    {
      if (super.getTokenHashMap().containsKey(aWord))
        cosineSimilarity = cosineSimilarity + super.getTokenHashMap().get(aWord) * aCandidate.getTokenHashMap().get(aWord);
    }
    
    cosineSimilarity = cosineSimilarity / (queryNorm * candidateNorm);
    
    aCandidate.setCosineSimiarity(cosineSimilarity);
    
    System.out.println("Score: " + cosineSimilarity+ " \t" + "qid=" + aCandidate.getqueryId() + " \trel=" + aCandidate.getRelevantValue());
  }
  
}
