package com.grovepi.mqtt.semantic.sensor;

import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.query.BindingSet;

import com.grovepi.mqtt.connection.MqttPublisher;
import com.grovepi.semantic.enrichment.PublisherSparqlQuery;
import com.grovepi.semantic.enrichment.SemanticSensor;

public class SemanticBedPressureSensor {

	public static void main(String[] args) throws Exception {
		
		SemanticSensor pressureSensor = new SemanticSensor("PressureSensor", 
				"BedPressureSensor1", "personInBed","activity",""	); 
		pressureSensor.addSensorToOntology();  
		MqttPublisher app = new MqttPublisher("PressureClient-Pub");		
		//connect to the grovePi and retrieve messages each 10 minutes 
		//int message = 20; //temporary message
		//int message1 = 60;
			
		//getting the message for a specific property		
		//PublisherSparqlQuery query = new PublisherSparqlQuery("pressureInBed");
		//BindingSet result = query.runQuery();
		//creating MQTT message
		
		app.runClient();
		
		for (int i = 0; i < 30; i++){
			 pressureSensor.addObservation(i*10);
			 Model result = pressureSensor.getSensorOutput();
			 MqttMessage msg = new MqttMessage(result.toString().getBytes());
			 //MqttMessage msg = new MqttMessage("testBed".toString().getBytes());
			 app.sendMessage("personInBed", msg.toString());			
          		//System.out.print(button.isPressed() ? 1 : 0);	
			 Thread.sleep(1000);
	    }
		 app.stopClient();
	}

}
