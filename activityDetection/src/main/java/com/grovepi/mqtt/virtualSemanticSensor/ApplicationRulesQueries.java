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
import org.eclipse.rdf4j.repository.RepositoryException;
import org.eclipse.rdf4j.repository.sail.SailRepository;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.RDFParseException;
import org.eclipse.rdf4j.sail.inferencer.fc.ForwardChainingRDFSInferencer;
import org.eclipse.rdf4j.sail.memory.MemoryStore;

public class ApplicationRulesQueries {
	File intput = new File("ApplicationOntology.owl");
	String ssnURI = "http://purl.oclc.org/NET/ssnx/ssn#";
	String DUL = "http://www.loa-cnr.it/ontologies/DUL.owl#";
	
	final String DressingActivity = " SELECT (MAX(?buttontime) AS ?maxTime) WHERE { "  + "?forceOutput <" + DUL +"hasDataValue> ?forceValue . "  + "?pressobservation <"
		       + ssnURI  + "observationResult> ?forceOutput ."  + "?pressobservation <"  + ssnURI  + "observedProperty> ?forceproperty  ." + "?forceOutput <"
		       + ssnURI + "hasDatetime> ?forcetime ." + "filter regex(str(?forceproperty), \"personInBed\")." 
		       + "filter (?forceValue < 50) ."
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
		       + "filter (?buttonValue = 1) ."
		       //
		       + "?ultrasonicOutput <"
		       + DUL
		       +"hasDataValue> ?ultrasonicValue . "
		       + "?ultrasonicObservation <"
		       + ssnURI
		       + "observationResult> ?ultrasonicOutput ."
		       + "?ultrasonicObservation <"
		       + ssnURI
		       + "observedProperty> ?ultrasonicProperty  ."
		       + "?ultrasonicOutput <"
		       + ssnURI
		       + "hasDatetime> ?ultrasonicTime ."
		       + "filter regex(str(?ultrasonicProperty), \"personUp\")."
		       + "filter (?ultrasonicValue < 30) ."
		       + "filter (?buttontime = ?ultrasonicTime)."
		       + "filter (?buttontime = ?forcetime) . "
		       + "}";
	
	final String showerActivity = " SELECT (MAX(?forcetime) AS ?maxTime) WHERE { "
			   + "?forceOutput <"
		       + DUL +"hasDataValue> ?forceValue . "
		       + "?pressobservation <"
		       + ssnURI + "observationResult> ?forceOutput ."
		       + "?pressobservation <"
		       + ssnURI + "observedProperty> ?forceproperty  ."
		       + "?forceOutput <"
		       + ssnURI + "hasDatetime> ?forcetime ."
		       + "filter regex(str(?forceproperty), \"personInBed\")."
		       + "filter (?forceValue < 5) ."
		       //
		       + "?ultrasonicOutput <"
		       + DUL +"hasDataValue> ?ultrasonicValue . "
		       + "?ultrasonicObservation <"
		       + ssnURI + "observationResult> ?ultrasonicOutput ."
		       + "?ultrasonicObservation <"
		       + ssnURI + "observedProperty> ?ultrasonicProperty  ."
		       + "?ultrasonicOutput <"
		       + ssnURI + "hasDatetime> ?ultrasonicTime ."
		       + "filter regex(str(?ultrasonicProperty), \"personUp\")."
		       + "filter (?ultrasonicValue > 20) ."
		       //
		       + "?waterOutput <"
		       + DUL +"hasDataValue> ?waterValue . "
		       + "?waterObservation <"
		       + ssnURI + "observationResult> ?waterOutput ."
		       + "?waterObservation <"
		       + ssnURI + "observedProperty> ?waterProperty  ."
		       + "?waterOutput <"
		       + ssnURI + "hasDatetime> ?waterTime ."
		       + "filter regex(str(?waterProperty), \"showerON\")."
		       + "filter (?waterValue > 100)"
		       + "filter (?waterValue <> 1023)"
		       + "filter (?forcetime = ?ultrasonicTime)."
		       + "filter (?waterTime = ?forcetime) . "
		       + "}";
	
	RepositoryConnection con;
	Repository repo;
	
	public ApplicationRulesQueries() throws RDFParseException, RepositoryException, IOException{
		//Repository repo = new SailRepository(new ForwardChainingRDFSInferencer(new MemoryStore()));
		//repo.initialize();
			
		// + "filter regex(str(?buttontime), str(?vibrationtime))."
	      // + "filter regex(str(?buttontime), str(?forcetime))"
		repo = new SailRepository(new ForwardChainingRDFSInferencer(new MemoryStore()));
		repo.initialize();
		try(RepositoryConnection con = repo.getConnection()){
			 con.add(intput, ssnURI, RDFFormat.RDFXML);
		}
		
	}
	
	public BindingSet runQuery(String activity){
		//Repository repo = new SailRepository(new ForwardChainingRDFSInferencer(new MemoryStore()));
		//repo.initialize();
		BindingSet bindingSet = null;
		TupleQuery tupleQuery;
		try(RepositoryConnection con = repo.getConnection()) {	   
			   //con.add(intput, ssnURI, RDFFormat.RDFXML);
			  // System.out.println("connection established.");
			   //System.out.println("connection established. processing query...");	
		//if (con.isOpen())
		   try {
			   if (activity.equals("dressing"))
					   tupleQuery = con.prepareTupleQuery(QueryLanguage.SPARQL, DressingActivity);
			   else
				   tupleQuery = con.prepareTupleQuery(QueryLanguage.SPARQL, showerActivity);
			   
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
			} catch (RDF4JException e) {
			   // handle exception				
				System.out.println("rdf exeption: " + e.getMessage());
			} 
		return bindingSet;
	}
	}
/*	
	public static void main(String[] args) throws RDFParseException, RepositoryException, IOException{
		
		ApplicationRulesQueries wearingClothes = new ApplicationRulesQueries();	
		BindingSet res = wearingClothes.runQuery("dressing");

	}*/

}

