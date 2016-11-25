package com.grovepi.mqtt.sensorsSimulation;

import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.query.BindingSet;

import com.grovepi.mqtt.connection.MqttPublisher;
import com.grovepi.semantic.enrichment.PublisherSparqlQuery;
import com.grovepi.semantic.enrichment.SemanticSensor;

public class SemanticButtonSensor {
	
	public static void main(String[] args) throws Exception {
		
			SemanticSensor ButtonSensor = new SemanticSensor("ButtonSensor", 
					"wardrobeButton1", "wardrobeOpened","activity",""	); 
			ButtonSensor.addSensorToOntology();  
			MqttPublisher app = new MqttPublisher("ButtonClient-Pub");
			
			// if the button = 0 means the wardrobe is opened
			//connect to the grovePi and retrieve messages each 10 minutes 
			//int message = 0; //temporary message
			//int message1 = 1;
						
			//getting the message for a specific property
			//PublisherSparqlQuery query = new PublisherSparqlQuery("wardrobeOpened");
			//BindingSet result = query.runQuery();
			app.runClient();
			
			for (int i = 0; i < 30; i++){
				ButtonSensor.addObservation(i%2);
				Model result = ButtonSensor.getSensorOutput();
				String resultat = result.toString();
				String res = resultat.substring(2, resultat.length()-2);
				MqttMessage msg = new MqttMessage(res.toString().getBytes());
				//MqttMessage msg = new MqttMessage(" testButton".toString().getBytes());
				app.sendMessage("wardrobeOpened", msg.toString());			
	          		//System.out.print(button.isPressed() ? 1 : 0);	
			    Thread.sleep(1000);
		    }
			 app.stopClient();
		}

}
