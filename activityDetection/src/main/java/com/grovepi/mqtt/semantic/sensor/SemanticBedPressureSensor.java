package com.grovepi.mqtt.semantic.sensor;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttTopic;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.ValueFactory;
import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.sail.SailRepository;
import org.eclipse.rdf4j.sail.inferencer.fc.ForwardChainingRDFSInferencer;
import org.eclipse.rdf4j.sail.memory.MemoryStore;

import com.grovepi.mqtt.connection.MqttPublisher;
import com.grovepi.semantic.enrichment.PublisherSparqlQuery;
import com.grovepi.semantic.enrichment.SemanticSensor;

import usefulClasses.PressureObservation;

public class SemanticBedPressureSensor {

	public static void main(String[] args) throws Exception {
		
		SemanticSensor pressureSensor = new SemanticSensor("PressureSensor", 
				"BedPressureSensor1", "personInBed","activity",""	); 
		pressureSensor.addSensorToOntology();  
		MqttPublisher app = new MqttPublisher();		
		//connect to the grovePi and retrieve messages each 10 minutes 
		String message = "person is not in Bed"; //temporary message
		
		//He creates a new observation for each message I need to make it dynamic
		pressureSensor.addObservation(message); 
		
		//getting the message for a specific property
		PublisherSparqlQuery query = new PublisherSparqlQuery("personInBed");
		BindingSet result = query.runQuery();
		//creating MQTT message
		MqttMessage msg = new MqttMessage(result.toString().getBytes());
		app.runClient();
		
		for (int i = 0; i < 20; i++){
		app.sendMessage("personInBed", msg.toString());			
          		//System.out.print(button.isPressed() ? 1 : 0);	
		Thread.sleep(200);
	    }
		 app.stopClient();
	}

}
