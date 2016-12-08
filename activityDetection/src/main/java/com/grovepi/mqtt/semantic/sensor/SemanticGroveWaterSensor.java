package com.grovepi.mqtt.semantic.sensor;

import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.rdf4j.model.Model;

import com.grovepi.mqtt.connection.MqttPublisher;
import com.grovepi.physical.sensors.jgrove.WaterSensor;
import com.grovepi.semantic.enrichment.SemanticSensor;

import grovepi.Pin;


public class SemanticGroveWaterSensor {
	public static void main(String[] args) throws Exception {
		WaterSensor water = new WaterSensor(Pin.ANALOG_PIN_0);
		SemanticSensor WaterSensor = new SemanticSensor("WaterSensor", //button or groveSensorsId
				"WaterSensorAN0", "showerON","activity",""); 
		WaterSensor.addSensorToOntology();  
		MqttPublisher app = new MqttPublisher("WaterClient-Pub");
		app.runClient();
		for(;;){
			WaterSensor.addObservation(water.getValue());
			Model waterResult = WaterSensor.getSensorOutput();
			String waterResultat = waterResult.toString();
			String waterRes = waterResultat.substring(2, waterResultat.length()-2);
			MqttMessage waterMsg = new MqttMessage(waterRes.toString().getBytes());
			
			app.sendMessage("showerON", waterMsg.toString());
		}
	}

}
