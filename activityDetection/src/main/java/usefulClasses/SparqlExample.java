package usefulClasses;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.model.ValueFactory;
import org.eclipse.rdf4j.model.vocabulary.FOAF;
import org.eclipse.rdf4j.model.vocabulary.RDF;
import org.eclipse.rdf4j.model.vocabulary.RDFS;
import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.QueryLanguage;
import org.eclipse.rdf4j.query.QueryResults;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.RepositoryException;
import org.eclipse.rdf4j.repository.RepositoryResult;
import org.eclipse.rdf4j.repository.sail.SailRepository;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.RDFParseException;
import org.eclipse.rdf4j.rio.Rio;
import org.eclipse.rdf4j.sail.inferencer.fc.ForwardChainingRDFSInferencer;
import org.eclipse.rdf4j.sail.memory.MemoryStore;

public class SparqlExample {

	public static void main(String[] args)  {
		// TODO Auto-generated method stub
		 Repository rep = new SailRepository(new ForwardChainingRDFSInferencer(new MemoryStore()));
		 rep.initialize();
		 
		 String namespace = "http://example.org/"; 
		 ValueFactory f = rep.getValueFactory();
		 
		 IRI john = f.createIRI(namespace, "john");
		 
		 // Get all the entities of subclasses of a certain class
		 String sparqlQueryString1 = "SELECT ?entity WHERE { ?entity rdf:type ?type. ?type rdfs:subClassOf <http://www.loa-cnr.it/ontologies/DUL.owl#PhysicalObject>.}";
		 		 
		 //File file = new File("BusinessIntelligenceOntology.owl");
		 File file = new File("ssn.owl");
		 
		 try (RepositoryConnection conn = rep.getConnection()) {
			 conn.add(file,  namespace, RDFFormat.RDFXML);
			 //conn.add(john, RDF.TYPE, FOAF.PERSON);
			   try {
				  // String queryString = " SELECT ?sensor WHERE { "
					   //		+ "?sensor rdfs:observes ?property }";
				   	 
				   	String queryString = "SELECT * WHERE {?s ?p ?o . }";
				   	//String queryString = "SELECT * WHERE {<http://www.semanticweb.org/antoniodenicola/ontologies/2016/5/BusinessIntelligenceOntology.owl#Advertiser> ?p ?o . }";
				      //TupleQuery tupleQuery = conn.prepareTupleQuery(QueryLanguage.SERQL, queryString);
				      //TupleQuery tupleQuery = conn.prepareTupleQuery(QueryLanguage.SPARQL, sparqlQueryString1);
				      TupleQuery tupleQuery = conn.prepareTupleQuery(QueryLanguage.SPARQL, queryString);
				      TupleQueryResult result = tupleQuery.evaluate();
				      try {
				    	  while (result.hasNext()) {
				    		   BindingSet bindingSet = result.next();
				    		   //Value valueOfX = bindingSet.getValue("x");
				    		   //Value valueOfY = bindingSet.getValue("y");
				    		   System.out.println(bindingSet.toString());
				    		   System.out.println();
				    		   //System.out.println(bindingSet.getValue(result.getBindingNames().get(0)));

				    		   //System.out.println(valueOfX.stringValue() + " %%%% " + valueOfY.stringValue());
				    		   //System.out.println(valueOfY.stringValue());
				    		   // do something interesting with the values here...
				    		}				      }
				      finally {
				         result.close();
				      }
				   }
				   finally {
				      conn.close();
				   }
			 //conn.add(john, RDF.TYPE, FOAF.PERSON); 
			 //conn.add(john, RDFS.LABEL, f.createLiteral("John"));
			 
			   // prepare the query
			   /*String queryString = "SELECT * WHERE {?s ?p ?o . }";
			   TupleQuery query = conn.prepareTupleQuery(QueryLanguage.SPARQL, queryString);
			   TupleQueryResult result = query.evaluate();
			   int i = 0;
			   while (result.hasNext()) {
				   System.out.println(i);
				   i++;
				   // do something interesting with the values here...
				}*/
			   
			   
			 //RepositoryResult<Statement> statements = conn.getStatements(null, null, null);
			 //Model model = QueryResults.asModel(statements);
			 //Rio.write(model, System.out, RDFFormat.TURTLE);		 
		 } catch (Exception e){
			 System.out.println(e);
		 }
	}

}

