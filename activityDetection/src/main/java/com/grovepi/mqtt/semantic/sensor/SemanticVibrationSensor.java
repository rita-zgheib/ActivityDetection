package com.grovepi.mqtt.semantic.sensor;

import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.rdf4j.query.BindingSet;

import com.grovepi.mqtt.connection.MqttPublisher;
import com.grovepi.semantic.enrichment.PublisherSparqlQuery;
import com.grovepi.semantic.enrichment.SemanticSensor;

public class SemanticVibrationSensor {
	
	public static void main(String[] args) throws Exception {
		/*
			SemanticSensor vibrationSensor = new SemanticSensor("VibrationSensor", 
					"floorVibSensor1", "personUp","activity",""	); 
			vibrationSensor.addSensorToOntology();  
					
			//connect to the grovePi and retrieve messages each 10 minutes 
			String message = "The person is Up"; //temporary message
			String message1 = "The person is not here";
			
			//He creates a new observation for each message I need to make it dynamic
			 vibrationSensor.addObservation(message);
			 vibrationSensor.addObservation(message1);
		*/	
			MqttPublisher app = new MqttPublisher();
			//getting the message for a specific property
			PublisherSparqlQuery query = new PublisherSparqlQuery("personUp");
			BindingSet result = query.runQuery();
			//creating MQTT message
			MqttMessage msg = new MqttMessage(result.toString().getBytes());
			app.runClient();
			
			for (int i = 0; i < 3; i++){
			app.sendMessage("personUp", msg.toString());			
	          		//System.out.print(button.isPressed() ? 1 : 0);	
			Thread.sleep(200);
		    }
			 app.stopClient();
		}

}
