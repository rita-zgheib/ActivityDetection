package com.grovepi.mqtt.virtualSemanticSensor;

import java.io.FileNotFoundException;
import java.util.HashMap;

import org.eclipse.paho.client.mqttv3.MqttException;
import com.grovepi.mqtt.connection.MqttSubscriber;
import com.grovepi.semantic.enrichment.VirtualSemanticSensor;

public class WearingClothes_App {
	
	public static void main(String[] args) throws MqttException, InterruptedException, FileNotFoundException {
		//create virtualSemanticSensor in ssnApplication
		VirtualSemanticSensor wearingClothes = new VirtualSemanticSensor("WearingSensor",
				"WearingClothesSensor1", "WearingClothes","activity", "");
		//wearingClothes.addSensorToOntology();		

		VirtualSemanticSubscriber app =  new VirtualSemanticSubscriber();
		app.runClient();
		String[] topics = new String[]{"personInBed","personUp","wardrobeOpened"};
		app.subscribeTO(topics);
		//app.subscribeTO("personInBed");
		//	app.subscribeTO("personUp");
		//	app.subscribeTO("wardrobeOpened");
		//app.stopClient();

	}
}
