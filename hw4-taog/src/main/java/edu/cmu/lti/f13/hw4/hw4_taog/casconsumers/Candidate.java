package edu.cmu.lti.f13.hw4.hw4_taog.casconsumers;

import edu.cmu.lti.f13.hw4.hw4_taog.typesystems.Document;

/**
 * 
 * @author Cambi
 * a Candidate object that denotes to a candidate sentence of the query
 * implemented the cosineSimilarity field
 * 
 */
public class Candidate extends Sentence {

  /** the cosine similarity value of the candidate to its corresponding query */
  private double cosineSimilarity;
  
  public Candidate() {
    
  }
  
  public Candidate(Document aDoc) {
    super(aDoc);
  }
  
  public double getCosineSimilarity(){
    return cosineSimilarity;    
  }
  
  public void setCosineSimiarity(double aValue){
    cosineSimilarity = aValue;
  }
  
}
