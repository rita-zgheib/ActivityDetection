package com.grovepi.mqtt.semantic.sensor;

import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.rdf4j.model.Model;

import com.grovepi.mqtt.connection.MqttPublisher;
import com.grovepi.physical.sensors.jgrove.groveButtonSensor;
import com.grovepi.semantic.enrichment.SemanticSensor;

import grovepi.Pin;

public class SemanticGroveButtonSensor extends Thread { //later on it will extends Thread
	
	public static void main(String[] args) throws Exception {
		
		groveButtonSensor button = new groveButtonSensor(Pin.DIGITAL_PIN_4);
		SemanticSensor ButtonSensor = new SemanticSensor("ButtonSensor", //button or groveSensorsId
				"wardrobeButtonPIN4", "wardrobeOpened","behaviour",""); 
		ButtonSensor.addSensorToOntology();  
		MqttPublisher app = new MqttPublisher("ButtonClient-Pub");
		app.runClient();
		for(;;)
			if(button.buttonNotPressed()){
				    ButtonSensor.addObservation(0);
				    Model result = ButtonSensor.getSensorOutput();
					String resultat = result.toString();
					String res = resultat.substring(2, resultat.length()-2);
					MqttMessage msg = new MqttMessage(res.toString().getBytes());
					//MqttMessage msg = new MqttMessage(" testButton".toString().getBytes());
					app.sendMessage("wardrobeOpened", msg.toString());			
		          		//System.out.print(button.isPressed() ? 1 : 0);	
				    Thread.sleep(1000);
			}
	}

}
