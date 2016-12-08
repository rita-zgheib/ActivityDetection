package com.grovepi.mqtt.semantic.sensors.cossn;

import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.rdf4j.model.Model;

import com.grovepi.mqtt.connection.MqttPublisher;
import com.grovepi.physical.sensors.jgrove.UltrasonSensor;
import com.grovepi.physical.sensors.jgrove.groveButtonSensor;

import grovepi.Pin;


public class SemanticUltrasonicButtonSensors {
		
	public static void main(String[] args) throws Exception {
		
		UltrasonSensor ultraSens = new UltrasonSensor(Pin.DIGITAL_PIN_4);
		groveButtonSensor button = new groveButtonSensor(Pin.DIGITAL_PIN_3);
		
		
		SemanticSensor ButtonSensor = new SemanticSensor("button","ButtonSensor"); //button or groveSensorsId					
		ButtonSensor.addSensorToOntology();
		
		SemanticSensor ultrasemSensor = new SemanticSensor("ultraSens","UltrasonicRangerSensor"); //button or groveSensorsId
		ultrasemSensor.addSensorToOntology();
		
		MqttPublisher buttonApp = new MqttPublisher("ButtonClient-Pub");
		MqttPublisher ultrasonicApp = new MqttPublisher("ultrasonicClient-Pub");
		buttonApp.runClient();
		ultrasonicApp.runClient();
		
		for(;;){
			if(button.buttonPressed()){
			    ButtonSensor.addObservation("ButtonProperty","",1);		    
			    Model result = ButtonSensor.getSensorOutput();
				String resultat = result.toString();
				String res = resultat.substring(2, resultat.length()-2);
				MqttMessage msg = new MqttMessage(res.toString().getBytes());
				//MqttMessage msg = new MqttMessage(" testButton".toString().getBytes());
				buttonApp.sendMessage("wardrobeOpened", msg.toString());			
	          		//System.out.print(button.isPressed() ? 1 : 0);
			}
				//ultrasonicApp
			    ultrasemSensor.addObservation(ultraSens.getDistance());
				Model ultraResult = ultrasemSensor.getSensorOutput();
				String ultraResultat = ultraResult.toString();
				String ultraRes = ultraResultat.substring(2, ultraResultat.length()-2);
				MqttMessage ultraMsg = new MqttMessage(ultraRes.toString().getBytes());
				
				ultrasonicApp.sendMessage("personUp", ultraMsg.toString());
				
				//Thread.sleep(1000);
		    
			
		}
	
	}
	

}
