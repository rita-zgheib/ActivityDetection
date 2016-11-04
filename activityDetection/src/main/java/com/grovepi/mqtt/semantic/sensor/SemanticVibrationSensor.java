package com.grovepi.mqtt.semantic.sensor;

import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.query.BindingSet;

import com.grovepi.mqtt.connection.MqttPublisher;
import com.grovepi.semantic.enrichment.PublisherSparqlQuery;
import com.grovepi.semantic.enrichment.SemanticSensor;

public class SemanticVibrationSensor {
	
	public static void main(String[] args) throws Exception {
		
			SemanticSensor vibrationSensor = new SemanticSensor("VibrationSensor", 
					"floorVibSensor1", "personUp","activity",""	); 
			vibrationSensor.addSensorToOntology();  
			MqttPublisher app = new MqttPublisher("VibrationClient-Pub");
			//vibration > 30 means the person is in front of his bed
			//connect to the grovePi and retrieve messages each 10 minutes 
			//int message = 50; //temporary message
			//int message1 = 10;
						
			//getting the message for a specific property
			//PublisherSparqlQuery query = new PublisherSparqlQuery("floorVibration");
			//BindingSet result = query.runQuery();
			
			//creating MQTT message
			
			app.runClient();
			//change topic to floorVibration later
			for (int i = 0; i < 30; i++){
				vibrationSensor.addObservation(i*10);
				Model result = vibrationSensor.getSensorOutput();
				String resultat = result.toString();
				String res = resultat.substring(2, resultat.length()-2);
				MqttMessage msg = new MqttMessage(res.getBytes());
				//MqttMessage msg = new MqttMessage("vibrationsTest".toString().getBytes());
				//String msgToSend = msg.toString().substring(2, int(length(msg.toString()-2));
				app.sendMessage("personUp", msg.toString());			
		          		//System.out.print(button.isPressed() ? 1 : 0);	
				Thread.sleep(1000);
		    }
			 app.stopClient();
		}

}
