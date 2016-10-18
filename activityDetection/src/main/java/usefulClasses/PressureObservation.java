package usefulClasses;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Literal;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.model.ValueFactory;
import org.eclipse.rdf4j.model.vocabulary.RDF;
import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.QueryLanguage;
import org.eclipse.rdf4j.query.QueryResults;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.RepositoryResult;
import org.eclipse.rdf4j.repository.sail.SailRepository;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.RDFHandlerException;
import org.eclipse.rdf4j.rio.RDFParser;
import org.eclipse.rdf4j.rio.RDFWriter;
import org.eclipse.rdf4j.rio.Rio;
import org.eclipse.rdf4j.sail.inferencer.fc.ForwardChainingRDFSInferencer;
import org.eclipse.rdf4j.sail.memory.MemoryStore;

public class PressureObservation {
	
	//les capteurs envoient toujours 0 et 1 on obligent à ce que dans
	//la traduction sémantique on ajoute que des strings pour le moment
	//String value = "person is in his bed";
	private String value;
	String ssnNamespace = "http://purl.oclc.org/NET/ssnx/ssn#"; 
	String dulNamespace = "http://www.loa-cnr.it/ontologies/DUL.owl#";
	File file = new File("ssn.owl");
	Model model;
		
	public PressureObservation(String msg){
		this.value = msg;		
	}
	
	public void addObservation() throws FileNotFoundException{
		Repository rep = new SailRepository(new ForwardChainingRDFSInferencer(new MemoryStore()));
		rep.initialize();
		ValueFactory f = rep.getValueFactory();		
		//IRI observation
		IRI observation = f.createIRI("http://purl.oclc.org/NET/ssnx/ssn#Observation"); 
		IRI sensorOutput = f.createIRI("http://purl.oclc.org/NET/ssnx/ssn#SensorOutput");

		IRI hasDataValue = f.createIRI(dulNamespace, "hasDataValue");
		IRI observedProperty = f.createIRI(ssnNamespace, "observedProperty");
		IRI observedBy = f.createIRI(ssnNamespace, "observedBy");
		IRI featureOfInterest = f.createIRI(ssnNamespace, "featureOfInterest");
		IRI observationResult = f.createIRI(ssnNamespace, "observationResult");
		
		IRI observationInd = f.createIRI(ssnNamespace, "pressureObservation5");
		IRI sensorOutputInd = f.createIRI(ssnNamespace, "vibrationOutput3");
		
		IRI PropertyInd = f.createIRI(ssnNamespace, "personInBed");
		IRI sensorInd = f.createIRI(ssnNamespace, "bedPressureSensor");
		IRI foiInd = f.createIRI(ssnNamespace, "activity");
		
		Literal val = f.createLiteral(value);
		
		//he gets msg from grovePi supposons 
		 
		 try (RepositoryConnection conn = rep.getConnection()) {
			 conn.add(file,  ssnNamespace, RDFFormat.RDFXML);
			 
			 conn.add(sensorOutputInd, RDF.TYPE, sensorOutput);
			 conn.add(sensorOutputInd, hasDataValue, val);
			 
			 conn.add(observationInd, RDF.TYPE, observation);
			 conn.add(observationInd, observedProperty, PropertyInd);
			 conn.add(observationInd, observedBy, sensorInd);
			 conn.add(observationInd, featureOfInterest, foiInd);
			 conn.add(observationInd, observationResult, sensorOutputInd);			   
			 RepositoryResult<Statement> statements = conn.getStatements(null, null, null);
			 model = QueryResults.asModel(statements);			
			 conn.close();
			 Rio.write(model, System.out, RDFFormat.TURTLE);
			 saveObservation(model);
		 } catch (Exception e){
			 System.out.println(e);
		 }		
	}
	
	public void saveObservation(Model model) throws FileNotFoundException{
		OutputStream output = new FileOutputStream("ssn.owl");
		RDFWriter rdfWriter = Rio.createWriter(RDFFormat.RDFXML,output);
		try {
			 rdfWriter.startRDF();
			  for (Statement st: model) {
				  rdfWriter.handleStatement(st);
			  }
			  rdfWriter.endRDF();
			}
			catch (RDFHandlerException e) {
			 // oh no, do something!
			}		
	}
	
	/*
	public static void main(String[] args) throws FileNotFoundException  {
		Observation obs = new Observation("person is not in his bed");
		obs.addObservation();
		
		System.out.println("observation added successfully");		
	}
*/
}
