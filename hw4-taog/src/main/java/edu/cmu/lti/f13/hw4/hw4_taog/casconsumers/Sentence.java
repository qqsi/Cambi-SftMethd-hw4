package edu.cmu.lti.f13.hw4.hw4_taog.casconsumers;

/**
 * the Sentence class is the abstract superclass for casConsuming part
 * it is initialized with the TokenHashMap generated
 * and set the document instance field to the document
 */

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.uima.jcas.cas.FSList;

import edu.cmu.lti.f13.hw4.hw4_taog.typesystems.Document;
import edu.cmu.lti.f13.hw4.hw4_taog.typesystems.Token;
import edu.cmu.lti.f13.hw4.hw4_taog.utils.Utils;

/**
 * 
 * @author Cambi
 * class Sentence, the abstract class for the Query and Candidate class
 * implemented the tokenHashMap queryID, relevantValue and document reference features for each sentence
 */


public abstract class Sentence {

  /** maps the token text with its corresponding frequency in the sentence */
  private HashMap<String, Integer> tokenHashMap;
  
  /** records the queryId of the sentence */
  private Integer queryId;
  
  /** records the relevant value of the sentence */
  private Integer relevantValue;
  
  /** records the document reference in the sentence, actually not in use */
  private Document document;
  
  
  public HashMap<String, Integer> getTokenHashMap(){
    return tokenHashMap;
  }
  
  public Document getDocument(){
    return document;
  }
  
  public void setqueryId(int anID){
    queryId = anID;
  }

  public Integer getqueryId(){
    return queryId;
  }
  
  public void setRelevantValue(int aValue){
    relevantValue = aValue;
  }
  
  public Integer getRelevantValue(){
    return relevantValue;
  }
  
  public Sentence() {
  //intentionally left blank
  }
  
  /**
   * 
   * @param aDoc
   * constructor of this class
   * generate and initialize each instance field of this class from the corresponding document
   * 
   */
  
  public Sentence(Document aDoc){
    FSList fsTokenList = aDoc.getTokenList();
    ArrayList<Token> tokenList = Utils.fromFSListToCollection(fsTokenList, Token.class);
    tokenHashMap = new HashMap<String, Integer>();
    for (Token aToken:tokenList)
    {
      tokenHashMap.put(aToken.getText(), aToken.getFrequency());
    }
    queryId = aDoc.getQueryID();
    relevantValue = aDoc.getRelevanceValue();
    document = aDoc;
  }
}
