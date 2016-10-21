package com.grovepi.mqtt.semantic.sensor;

import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.rdf4j.query.BindingSet;

import com.grovepi.mqtt.connection.MqttPublisher;
import com.grovepi.semantic.enrichment.PublisherSparqlQuery;
import com.grovepi.semantic.enrichment.SemanticSensor;

public class SemanticBedPressureSensor {

	public static void main(String[] args) throws Exception {
		
		SemanticSensor pressureSensor = new SemanticSensor("PressureSensor", 
				"BedPressureSensor1", "personInBed","activity",""	); 
		pressureSensor.addSensorToOntology();  
				
		//connect to the grovePi and retrieve messages each 10 minutes 
		int message = 20; //temporary message
		int message1 = 60;
		
		//He creates a new observation for each message I need to make it dynamic
		 pressureSensor.addObservation(message); 
		 pressureSensor.addObservation(message1); 
		
		//getting the message for a specific property
		MqttPublisher app = new MqttPublisher();
		PublisherSparqlQuery query = new PublisherSparqlQuery("pressureInBed");
		BindingSet result = query.runQuery();
		//creating MQTT message
		MqttMessage msg = new MqttMessage(result.toString().getBytes());
		app.runClient();
		
		for (int i = 0; i < 3; i++){
		app.sendMessage("pressureInBed", msg.toString());			
          		//System.out.print(button.isPressed() ? 1 : 0);	
		Thread.sleep(200);
	    }
		 app.stopClient();
	}

}
