package usefulClasses;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Optional;

import org.eclipse.rdf4j.RDF4JException;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.model.ValueFactory;
import org.eclipse.rdf4j.model.impl.LinkedHashModel;
import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.QueryLanguage;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.sail.SailRepository;
import org.eclipse.rdf4j.repository.sparql.SPARQLRepository;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.RDFParser;
import org.eclipse.rdf4j.rio.Rio;
import org.eclipse.rdf4j.rio.helpers.StatementCollector;
import org.eclipse.rdf4j.sail.inferencer.fc.ForwardChainingRDFSInferencer;
import org.eclipse.rdf4j.sail.memory.MemoryStore;

public class SemanticSubscriberSparql {
	
	// I have to add the subcriber code here and then publisher
	// this example is just to test sparql
	public static void main(String args[]) throws IOException{
		System.out.println("program started");
		Repository repo = new SailRepository(new ForwardChainingRDFSInferencer(new MemoryStore()));
		repo.initialize();
		System.out.println("repository initialized");
		
		File intput = new File("ssnApplication.owl");
		String ssnURI = "http://purl.oclc.org/NET/ssnx/ssn#";
		String DUL = "http://www.loa-cnr.it/ontologies/DUL.owl#";
		ValueFactory f = repo.getValueFactory();	
		
		try(RepositoryConnection con = repo.getConnection()) {	   
		   con.add(intput, ssnURI, RDFFormat.RDFXML);
		   System.out.println("connection established.");
		  // URL url = new URL("http://example.org/example/remote.rdf");
		   //con.add(url, url.toString(), RDFFormat.RDFXML);
		   System.out.println("connection established. processing query...");
		   
		   try { 
		  /*   String queryString = "SELECT  ?dataValues WHERE { ?sensorOutput  DUL:hasDataValue ?dataValues . "
		      		+ "?observation ssn:observationResult ?sensorOutput . "
		      		+ "?observation  ssn:observedProperty ?property  . "
		      		+ " FILTER regex(str(?property), \"inBed\")} ";	
		      		*/	     
			   
			   String basicQueryString = " SELECT ?sensor WHERE { "
				   		+ "?sensor <"
				   		+ ssnURI
				   		+ "observes> ?property }";
			   
			   String queryString = " SELECT ?dataValues WHERE { "
					   + "?sensorOutput <"
				       + DUL
				       +"hasDataValue> ?dataValues . "
				       + "?observation <"
				       + ssnURI
				       + "observationResult> ?sensorOutput ."
				       + "?observation <"
				       + ssnURI
				       + "observedProperty> ?property  ."
				       + "filter regex(str(?property), \"personInBed\")}";
								                 		   
			   //String queryString = "SELECT * WHERE {?s ?p ?o . }";
			   TupleQuery tupleQuery = con.prepareTupleQuery(QueryLanguage.SPARQL, queryString);
			   TupleQueryResult result = tupleQuery.evaluate();
			   try{
				   while (result.hasNext()) {  // iterate over the result
				   BindingSet bindingSet = result.next();
				   
				   //Value valueOfX = bindingSet.getValue("sensorOutput");
				   System.out.println(bindingSet.toString());
	    		   System.out.println();
				   // do something interesting with the values here...
				   System.out.println("query done succesffully");
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
		}

}
}
