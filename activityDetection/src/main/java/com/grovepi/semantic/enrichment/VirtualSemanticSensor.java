package com.grovepi.semantic.enrichment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Random;

import org.eclipse.rdf4j.RDF4JException;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Literal;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.model.ValueFactory;
import org.eclipse.rdf4j.model.impl.LinkedHashModel;
import org.eclipse.rdf4j.model.vocabulary.RDF;
import org.eclipse.rdf4j.query.QueryResults;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.RepositoryResult;
import org.eclipse.rdf4j.repository.sail.SailRepository;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.RDFHandlerException;
import org.eclipse.rdf4j.rio.RDFParser;
import org.eclipse.rdf4j.rio.RDFWriter;
import org.eclipse.rdf4j.rio.Rio;
import org.eclipse.rdf4j.rio.helpers.StatementCollector;
import org.eclipse.rdf4j.sail.inferencer.fc.ForwardChainingRDFSInferencer;
import org.eclipse.rdf4j.sail.memory.MemoryStore;

public class VirtualSemanticSensor {
	
	private String type;
	private String name;
	private String property;
	private String foi;
	private String classification;
	
	String ssnNamespace = "http://purl.oclc.org/NET/ssnx/ssn#"; 
	String dulNamespace = "http://www.loa-cnr.it/ontologies/DUL.owl#";

	File file = new File("ApplicationOntology.owl");
	String baseURI = "http://purl.oclc.org/NET/ssnx/ssn";
	
	Model model;
	java.util.Date date;
	Model sensorOutputModel;
	
	ValueFactory f;
	Repository rep;
	MemoryStore mem;
	RDFFormat format;
		
	public VirtualSemanticSensor(String type, String name, String property, 
			String foi, String classification) {
		this.type = type;
		this.name = name;
		this.property = property;
		this.foi = foi;
		this.classification = classification;
		date = new java.util.Date();
		
		mem = new MemoryStore();
		mem.setPersist(false);
		//mem.initialize();
		rep = new SailRepository(mem);
		rep.initialize();
		f = rep.getValueFactory();
		RDFParser rdfParser = Rio.createParser(RDFFormat.RDFXML);
		model = new LinkedHashModel();
		rdfParser.setRDFHandler(new StatementCollector(model));
		format = Rio.getParserFormatForFileName(file.toString()).orElse(RDFFormat.RDFXML);
	}
	
	public void addSensorToOntology() throws FileNotFoundException{
		
		//IRI classes
		IRI Sensor = f.createIRI(ssnNamespace+type); 
		IRI Property = f.createIRI(ssnNamespace+"Property");
		IRI FeatureOfInterest = f.createIRI(ssnNamespace+"FeatureOfInterest");
		IRI Classification = f.createIRI(ssnNamespace+"Classification");
		
		//IRI object properties I have to add the classification
		IRI observes = f.createIRI(ssnNamespace + "observes");
		
		//IRI individuals
		IRI PropertyInd = f.createIRI(ssnNamespace+ property);
		IRI foiInd = f.createIRI(ssnNamespace+ foi );
		IRI sensorInd = f.createIRI(ssnNamespace+ name );
		IRI classInd = f.createIRI(ssnNamespace+ classification );
		
		 
		 try {
			 RepositoryConnection conn = rep.getConnection();
			 try {
				 //conn.add(file,  ssnNamespace, RDFFormat.RDFXML);
				 conn.add(file, baseURI, format);
			 
				 conn.add(sensorInd, RDF.TYPE, Sensor);
				 conn.add(PropertyInd, RDF.TYPE, Property);
				 conn.add(foiInd, RDF.TYPE, FeatureOfInterest);
				 conn.add(classInd, RDF.TYPE, Classification);
				 			
				 conn.add(sensorInd, observes, PropertyInd);
				 			   
				 RepositoryResult<Statement> statements = conn.getStatements(null, null, null);
				 model = QueryResults.asModel(statements);
			 }finally {
			      conn.close();			      
			 }
				// Rio.write(model, System.out, RDFFormat.TURTLE);
				 saveModel(model);
		 } catch (RDF4JException e) {
			   // handle exception
			 System.out.println(e);
		 }catch (Exception e){
			 System.out.println(e);
		 }		
	}
	
	public void addObservation(String newMsg) throws FileNotFoundException{
		
		Random rand = new Random();
		
		//IRI classes
		IRI observation = f.createIRI(ssnNamespace+"Observation"); 
		IRI sensorOutput = f.createIRI(ssnNamespace+"SensorOutput");
		
		//IRI object properties I have to add the classification
		IRI hasDataValue = f.createIRI(dulNamespace+ "hasDataValue");
		IRI hasDatetime = f.createIRI(ssnNamespace+ "hasDatetime");
		IRI observedProperty = f.createIRI(ssnNamespace+ "observedProperty");
		IRI observedBy = f.createIRI(ssnNamespace+ "observedBy");
		IRI featureOfInterest = f.createIRI(ssnNamespace+ "featureOfInterest");
		IRI observationResult = f.createIRI(ssnNamespace+ "observationResult");
		
		IRI observationInd = f.createIRI(ssnNamespace+ property+"observation"+rand.nextInt(50));
		IRI sensorOutputInd = f.createIRI(ssnNamespace+ property+"sensorOutput"+rand.nextInt(50));
		
		IRI PropertyInd = f.createIRI(ssnNamespace + property );
		IRI foiInd = f.createIRI(ssnNamespace+ foi);
		IRI sensorInd = f.createIRI(ssnNamespace + name);
		
		Literal val = f.createLiteral(newMsg);
		//Literal datetime = f.createLiteral(new Timestamp(date.getTime()));
		Literal datetime = f.createLiteral(getDatetime());
		//he gets msg from grovePi supposons 
		 
		 try {
			 RepositoryConnection conn = rep.getConnection();
			 try {
				 conn.add(file,  ssnNamespace, format);
				 //conn.add(file,  ssnNamespace, RDFFormat.RDFXML);
			 
				 conn.add(sensorOutputInd, RDF.TYPE, sensorOutput);
				 conn.add(sensorOutputInd, hasDataValue, val);
				 conn.add(sensorOutputInd, hasDatetime, datetime);
				 
				 conn.add(observationInd, RDF.TYPE, observation);
				 conn.add(observationInd, observedProperty, PropertyInd);
				 conn.add(observationInd, observedBy, sensorInd);
				 conn.add(observationInd, featureOfInterest, foiInd);
				 conn.add(observationInd, observationResult, sensorOutputInd);
				 
				 RepositoryResult<Statement> statements = conn.getStatements(null, null, null);
				 model = QueryResults.asModel(statements);	
				 
				 RepositoryResult<Statement> sensorOutputStatements = conn.getStatements(sensorOutputInd, hasDataValue, val);
				 //sensorOutputStatements.
				 sensorOutputModel = QueryResults.asModel(sensorOutputStatements);
			 }finally {
			      conn.close();			      
			 }
		//	 Rio.write(model, System.out, RDFFormat.TURTLE);
		//	 saveModel(model);
		 } catch (RDF4JException e) {
			   // handle exception
			 System.out.println(e);
		 }catch (Exception e){
			 System.out.println(e);
		 }		
	}
	public Model getSensorOutput(){
		return sensorOutputModel;
		
	}
	public void saveModel(Model model) throws FileNotFoundException{
		OutputStream output = new FileOutputStream("ApplicationOntology.owl");
		//RDFWriter rdfWriter = Rio.createWriter(RDFFormat.RDFXML,output);
		RDFWriter rdfWriter = Rio.createWriter(format,output);
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
	public Timestamp getDatetime(){
		java.util.Date date = new java.util.Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		 
		// Set time fields to zero
		cal.set(Calendar.MILLISECOND, 0);	 
		// Put it back in the Date object
		date = cal.getTime();		
		Timestamp timestamp = new Timestamp(date.getTime());
		return(timestamp);
		
	}
	
	/*
	public static void main(String[] args) throws FileNotFoundException  {
		Observation obs = new Observation("person is not in his bed");
		obs.addObservation();
		
		System.out.println("observation added successfully");		
	}
*/

}
