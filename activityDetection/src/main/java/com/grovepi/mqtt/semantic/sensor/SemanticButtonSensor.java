package com.grovepi.mqtt.semantic.sensor;

import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.rdf4j.query.BindingSet;

import com.grovepi.mqtt.connection.MqttPublisher;
import com.grovepi.semantic.enrichment.PublisherSparqlQuery;
import com.grovepi.semantic.enrichment.SemanticSensor;

public class SemanticButtonSensor {
	
	public static void main(String[] args) throws Exception {
		
			SemanticSensor ButtonSensor = new SemanticSensor("ButtonSensor", 
					"wardrobeButton1", "wardrobeOpened","activity",""	); 
			ButtonSensor.addSensorToOntology();  
			
			// if the button = 0 means the wardrobe is opened
			//connect to the grovePi and retrieve messages each 10 minutes 
			int message = 0; //temporary message
			int message1 = 1;
			
			//He creates a new observation for each message I need to make it dynamic
			ButtonSensor.addObservation(message);
			ButtonSensor.addObservation(message1);
			
			MqttPublisher app = new MqttPublisher();
			//getting the message for a specific property
			PublisherSparqlQuery query = new PublisherSparqlQuery("wardrobeOpened");
			BindingSet result = query.runQuery();
			//creating MQTT message
			MqttMessage msg = new MqttMessage(result.toString().getBytes());
			app.runClient();
			
			for (int i = 0; i < 3; i++){
			app.sendMessage("wardrobeOpened", msg.toString());			
	          		//System.out.print(button.isPressed() ? 1 : 0);	
			Thread.sleep(200);
		    }
			 app.stopClient();
		}

}
