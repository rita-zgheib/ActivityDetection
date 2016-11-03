package com.grovepi.semantic.enrichment;

import java.io.File;
import java.io.IOException;

import org.eclipse.rdf4j.RDF4JException;
import org.eclipse.rdf4j.model.ValueFactory;
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

public class PublisherSparqlQuery {
	
	File intput = new File("ssn.owl");
	String ssnURI = "http://purl.oclc.org/NET/ssnx/ssn#";
	String DUL = "http://www.loa.istc.cnr.it/ontologies/DUL.owl#";
	
	String queryString;
	
	public PublisherSparqlQuery(String property){
					
	queryString = " SELECT ?dataValues WHERE { "
			   + "?sensorOutput <"
		       + DUL
		       +"hasDataValue> ?dataValues . "
		       + "?observation <"
		       + ssnURI
		       + "observationResult> ?sensorOutput ."
		       + "?observation <"
		       + ssnURI
		       + "observedProperty> ?property  ."
		       + "filter regex(str(?property), \""
		       + property
		       + "\")}";
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
			   TupleQuery tupleQuery = con.prepareTupleQuery(QueryLanguage.SPARQL, queryString);
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

}
