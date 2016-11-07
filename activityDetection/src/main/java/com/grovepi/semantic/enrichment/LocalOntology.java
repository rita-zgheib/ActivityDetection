package com.grovepi.semantic.enrichment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.eclipse.rdf4j.RDF4JException;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Literal;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.model.ValueFactory;
import org.eclipse.rdf4j.model.impl.LinkedHashModel;
import org.eclipse.rdf4j.model.util.RDFCollections;
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


public abstract class LocalOntology {
	
	static String baseURI = "http://purl.oclc.org/NET/ssnx/ssn";
	static String ssnNamespace = "http://purl.oclc.org/NET/ssnx/ssn#"; 
	static String dulNamespace = "http://www.loa-cnr.it/ontologies/DUL.owl#";
	static File file = new File("ApplicationOntology.owl");
	static Model model;
	
	
	public static void updateOntology(String topic, String foi, String sensorInd) throws FileNotFoundException{
		MemoryStore mem = new MemoryStore();
		mem.setPersist(false);
		//mem.initialize();
		Repository rep = new SailRepository(mem);
		//Repository rep = new SailRepository(new ForwardChainingRDFSInferencer(new MemoryStore()));
		rep.initialize();
		ValueFactory f = rep.getValueFactory();	
		Random rand = new Random();
		
		RDFParser rdfParser = Rio.createParser(RDFFormat.RDFXML);
		model = new LinkedHashModel();
		rdfParser.setRDFHandler(new StatementCollector(model));
		RDFFormat format = Rio.getParserFormatForFileName(file.toString()).orElse(RDFFormat.RDFXML);
		//System.out.println("entered updateOntology");
		
		//IRI classes
		IRI observation = f.createIRI(ssnNamespace+"Observation"); 
		IRI sensorOutput = f.createIRI(ssnNamespace+"SensorOutput");
		
		//IRI object properties I have to add the classification
		IRI hasDataValue = f.createIRI(dulNamespace+ "hasDataValue");
		IRI hasDatetime = f.createIRI(ssnNamespace+ "hasDatetime");
		IRI observedProperty = f.createIRI(ssnNamespace+ "observedProperty");

		IRI featureOfInterest = f.createIRI(ssnNamespace+ "featureOfInterest");
		IRI observationResult = f.createIRI(ssnNamespace+ "observationResult");
		
		IRI observationInd = f.createIRI(ssnNamespace+ topic+"observation"+rand.nextInt(50));
		//IRI sensorOutputInd = f.createIRI(sensorInd);
		IRI sensorOutputInd = f.createIRI(ssnNamespace+ topic+"sensorOutput"+rand.nextInt(50));
		
		//Date d;
		
		String[] words = null;
		for (int i = 0; i <sensorInd.length() ; i++){
			words =  sensorInd.split(",");			
		}
		int index = words[5].lastIndexOf("\"");
		int value = Integer.parseInt(words[5].substring(2, index));
		
		//System.out.println("words are: " + words[10] );
		//Resource sensorOutputInd = f.createIRI(sensorInd);
		//System.out.println("output saved to Local ontology is: " + sensorOutputInd );
		
		//List<Literal> outputs = Arrays.asList(new Literal[] { f.createLiteral("A"), f.createLiteral("B"), f.createLiteral("C") });
		//Model sensorOutputModel = RDFCollections.asRDF(message, r, new LinkedHashModel());
		//Model sensorOutputModel = new Model ();
		
		IRI PropertyInd = f.createIRI(ssnNamespace + topic );
		IRI foiInd = f.createIRI(ssnNamespace+ foi);
		//IRI sensorInd = f.createIRI(ssnNamespace + name );
		
		//IRI PropertyInd = f.createIRI(ssnNamespace, "personInBed");
		//IRI sensorInd = f.createIRI(ssnNamespace, "bedPressureSensor");
		//IRI foiInd = f.createIRI(ssnNamespace, "activity");
		
		Literal val = f.createLiteral(value);
		Literal datetime = f.createLiteral(getDatetime());
		
		//he gets msg from grovePi supposons 		 
		 try {
			 RepositoryConnection conn = rep.getConnection();
			 try {
				 conn.add(file, baseURI, format);
				 //conn.add(file,  ssnNamespace, RDFFormat.RDFXML);
			 
				 conn.add(sensorOutputInd, RDF.TYPE, sensorOutput);
				 conn.add(sensorOutputInd, hasDataValue, val);
				 conn.add(sensorOutputInd, hasDatetime, datetime);
				 
				 conn.add(observationInd, RDF.TYPE, observation);
				 conn.add(observationInd, observedProperty, PropertyInd);
				 //conn.add(observationInd, observedBy, sensorInd);
				 conn.add(observationInd, featureOfInterest, foiInd);
				 conn.add(observationInd, observationResult, sensorOutputInd);			   
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

	public static void saveModel(Model model) throws FileNotFoundException{
		OutputStream output = new FileOutputStream("ApplicationOntology.owl");
		//RDFWriter rdfWriter = Rio.createWriter(RDFFormat.RDFXML,output);
		RDFFormat format = Rio.getParserFormatForFileName(file.toString()).orElse(RDFFormat.RDFXML);
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
	public static Timestamp getDatetime(){
		Date date = new java.util.Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		 
		// Set time fields to zero
		cal.set(Calendar.MILLISECOND, 0);	 
		// Put it back in the Date object
		date = cal.getTime();		
		Timestamp timestamp = new Timestamp(date.getTime());
		return(timestamp);
		
	}

}
