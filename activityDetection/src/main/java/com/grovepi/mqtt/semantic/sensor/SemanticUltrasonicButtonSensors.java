package com.grovepi.mqtt.semantic.sensor;

import java.sql.Timestamp;
import java.util.Calendar;

import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.rdf4j.model.Model;

import com.grovepi.mqtt.connection.MqttPublisher;
import com.grovepi.physical.sensors.jgrove.UltrasonSensor;
import com.grovepi.physical.sensors.jgrove.groveButtonSensor;
import com.grovepi.semantic.enrichment.SemanticSensor;

import grovepi.Pin;

public class SemanticUltrasonicButtonSensors {
		
	public static void main(String[] args) throws Exception {
		
		UltrasonSensor ultraSens = new UltrasonSensor(Pin.DIGITAL_PIN_4);
		groveButtonSensor button = new groveButtonSensor(Pin.DIGITAL_PIN_3);
		
		SemanticSensor ButtonSensor = new SemanticSensor("ButtonSensor", //button or groveSensorsId
				"wardrobeButtonPIN3", "wardrobeOpened","activity",""); 
		ButtonSensor.addSensorToOntology();
		
		SemanticSensor ultrasemSensor = new SemanticSensor("UltrasonicRangerSensor", //button or groveSensorsId
				"FloorPresenceSensorPin4", "personUp","activity",""); 
		ultrasemSensor.addSensorToOntology();
		
		MqttPublisher buttonApp = new MqttPublisher("ButtonClient-Pub");
		MqttPublisher ultrasonicApp = new MqttPublisher("ultrasonicClient-Pub");
		buttonApp.runClient();
		ultrasonicApp.runClient();
		
		for(;;){
			if(button.buttonPressed()){
				Timestamp t = ButtonSensor.getDatetime();
			    ButtonSensor.addObservation(1,t);
			    ultrasemSensor.addObservation(ultraSens.getDistance(),t);
			    Model result = ButtonSensor.getSensorOutput();
			    Model ultraResult = ultrasemSensor.getSensorOutput();
			    			    
				String resultat = result.toString();
				String res = resultat.substring(2, resultat.length()-2);
				MqttMessage msg = new MqttMessage(res.toString().getBytes());
				//MqttMessage msg = new MqttMessage(" testButton".toString().getBytes());
				buttonApp.sendMessage("wardrobeOpened", msg.toString());			
	          		//System.out.print(button.isPressed() ? 1 : 0);
			
				//ultrasonicApp
				String ultraResultat = ultraResult.toString();
				String ultraRes = ultraResultat.substring(2, ultraResultat.length()-2);
				MqttMessage ultraMsg = new MqttMessage(ultraRes.toString().getBytes());
				
				ultrasonicApp.sendMessage("personUp", ultraMsg.toString());
				
				//Thread.sleep(1000);
			}
			
		}
	
	}
}
