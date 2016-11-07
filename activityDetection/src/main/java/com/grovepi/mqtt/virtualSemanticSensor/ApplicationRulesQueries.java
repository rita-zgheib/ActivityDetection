package com.grovepi.mqtt.virtualSemanticSensor;

import java.io.File;
import java.io.IOException;

import org.eclipse.rdf4j.RDF4JException;
import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.QueryLanguage;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.sail.SailRepository;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.sail.inferencer.fc.ForwardChainingRDFSInferencer;
import org.eclipse.rdf4j.sail.memory.MemoryStore;

public class ApplicationRulesQueries {
	File intput = new File("ApplicationOntology.owl");
	String ssnURI = "http://purl.oclc.org/NET/ssnx/ssn#";
	String DUL = "http://www.loa-cnr.it/ontologies/DUL.owl#";
	
	String queryString;
	String wearinClothesQuery;
	
	public ApplicationRulesQueries(){
		wearinClothesQuery = " SELECT ?pressuretime WHERE { "
				   + "?pressureOutput <"
			       + DUL
			       +"hasDataValue> ?pressureValue . "
			       + "?pressobservation <"
			       + ssnURI
			       + "observationResult> ?pressureOutput ."
			       + "?pressobservation <"
			       + ssnURI
			       + "observedProperty> ?pressureproperty  ."
			       + "?pressureOutput <"
			       + ssnURI
			       + "hasDatetime> ?pressuretime ."
			       + "filter regex(str(?pressureproperty), \"personInBed\")."
			       + "filter (?pressureValue < 30) ."
			       //
			       + "?buttonOutput <"
			       + DUL
			       +"hasDataValue> ?buttonValue . "
			       + "?buttonobservation <"
			       + ssnURI
			       + "observationResult> ?buttonOutput ."
			       + "?buttonobservation <"
			       + ssnURI
			       + "observedProperty> ?buttonproperty  ."
			       + "?buttonOutput <"
			       + ssnURI
			       + "hasDatetime> ?buttontime ."
			       + "filter regex(str(?buttonproperty), \"wardrobeOpened\")."
			       + "filter (?buttonValue = 0) ."
			       //
			       + "?vibrationOutput <"
			       + DUL
			       +"hasDataValue> ?vibrationValue . "
			       + "?vibrationobservation <"
			       + ssnURI
			       + "observationResult> ?vibrationOutput ."
			       + "?vibrationobservation <"
			       + ssnURI
			       + "observedProperty> ?vibrationproperty  ."
			       + "?vibrationOutput <"
			       + ssnURI
			       + "hasDatetime> ?vibrationtime ."
			       + "filter regex(str(?vibrationproperty), \"personUp\")."
			       + "filter (?vibrationValue > 30) ."
			       + "filter (?buttontime = ?vibrationtime)."
			       + "filter (?buttontime = ?pressuretime) . "
			       + "}";
		// + "filter regex(str(?buttontime), str(?vibrationtime))."
	      // + "filter regex(str(?buttontime), str(?pressuretime))"
		
	}
	
	public BindingSet runQuery(){
		Repository repo = new SailRepository(new ForwardChainingRDFSInferencer(new MemoryStore()));
		repo.initialize();
		BindingSet bindingSet = null;
		try(RepositoryConnection con = repo.getConnection()) {	   
			   con.add(intput, ssnURI, RDFFormat.RDFXML);
			  // System.out.println("connection established.");
			   //System.out.println("connection established. processing query...");			   
		   try { 									                 		 
			   TupleQuery tupleQuery = con.prepareTupleQuery(QueryLanguage.SPARQL, wearinClothesQuery);
			   TupleQueryResult result = tupleQuery.evaluate();
			   try{
				   while (result.hasNext()) {  // iterate over the result
				   bindingSet = result.next();				   
				   //Value valueOfX = bindingSet.getValue("sensorOutput");
				   System.out.println(bindingSet.toString());
	    		   System.out.println();
				   // do something interesting with the values here...
				   //System.out.println("query done succesffully");
				      }
				   }
			   finally {
			         result.close();
			      }
			}
			   finally {
			      con.close();
			   }
			}catch (RDF4JException e) {
			   // handle exception
				
				System.out.println("rdf exeption: " + e.getMessage());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return bindingSet;
	}
	/*
	public static void main(String[] args){

		ApplicationRulesQueries wearingClothes = new ApplicationRulesQueries();
		BindingSet res = wearingClothes.runQuery();

	}
*/
}
