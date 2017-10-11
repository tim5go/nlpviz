package com.bpodgursky.nlpviz;

import edu.stanford.nlp.ie.util.RelationTriple;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeCoreAnnotations;
import edu.stanford.nlp.naturalli.NaturalLogicAnnotations;
import edu.stanford.nlp.util.CoreMap;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.common.collect.Lists;

/**
 * A demo illustrating how to call the OpenIE system programmatically.
 */
public class OpenIEParser {
	
	private final StanfordCoreNLP pipeline;
	
	public OpenIEParser() {
	    Properties props = new Properties();
	    props.setProperty("annotators", "tokenize,ssplit,pos,lemma,depparse,parse,natlog,openie");
		pipeline = new StanfordCoreNLP(props);
	}

	public static void main(String[] args) throws Exception {
    // Create the Stanford CoreNLP pipeline
    Properties props = new Properties();
    props.setProperty("annotators", "tokenize,ssplit,pos,lemma,depparse,parse,natlog,openie");
    StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

    // Annotate an example document.
    Annotation doc = new Annotation("Obama was born in Hawaii. He is our president.");
    pipeline.annotate(doc);

    // Loop over sentences in the document
    for (CoreMap sentence : doc.get(CoreAnnotations.SentencesAnnotation.class)) {
      // Get the OpenIE triples for the sentence
      Collection<RelationTriple> triples = sentence.get(NaturalLogicAnnotations.RelationTriplesAnnotation.class);
      // Print the triples
      for (RelationTriple triple : triples) {
        System.out.println(triple.confidence + "\t" +
            triple.subjectLemmaGloss() + "\t" +
            triple.relationLemmaGloss() + "\t" +
            triple.objectLemmaGloss());
      }
    }
  }
  
  public JSONArray parse(String text) throws JSONException {
	  Annotation document = new Annotation(text);
	  pipeline.annotate(document);
	  JSONArray array = new JSONArray();

	  List<CoreMap> sentences = document.get(CoreAnnotations.SentencesAnnotation.class);
	  
	  for (CoreMap sentence : sentences) {
		  Collection<RelationTriple> triples = sentence.get(NaturalLogicAnnotations.RelationTriplesAnnotation.class);
	      array.put(toJSON(triples));
	    }

	    return array;
	  }
  
  public static JSONObject toJSON(Collection<RelationTriple> triples) throws JSONException {
	    List<JSONObject> tripleList = Lists.newArrayList();
	    
	    for (RelationTriple triple : triples) {
	    	JSONObject obj = new JSONObject();
	          System.out.println(triple.confidence + "\t" +
	              triple.subjectLemmaGloss() + "\t" +
	              triple.relationLemmaGloss() + "\t" +
	              triple.objectLemmaGloss());
	          obj.put("subjectLemmaGloss", triple.subjectLemmaGloss());
	          obj.put("relationLemmaGloss", triple.relationLemmaGloss());
	          obj.put("objectLemmaGloss", triple.objectLemmaGloss());
	          tripleList.add(new JSONObject().put("triple", obj));
	        }

	    return new JSONObject()
	            .put("tripleList", new JSONArray(tripleList));
	  }
}
