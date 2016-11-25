package com.grovepi.mqtt.virtualSemanticSensor;

import java.io.FileNotFoundException;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.rdf4j.query.BindingSet;

import com.grovepi.mqtt.connection.MqttPublisherSubscriber;
import com.grovepi.semantic.enrichment.VirtualSemanticSensor;

public class WearingClothes_App {
	private static  boolean PUBLISHER = false;
	private static  boolean SUBSCRIBER = true;
	
	
	public static void main(String[] args) throws MqttException, InterruptedException, FileNotFoundException {
		//create virtualSemanticSensor in ssnApplication
		VirtualSemanticSensor wearingClothes = new VirtualSemanticSensor("WearingSensor",
				"WearingClothesSensor1", "WearingClothes","activity", "");
		wearingClothes.addSensorToOntology();		
		
		MqttPublisherSubscriber app =  new MqttPublisherSubscriber("wearingClothes-Client"); 
		app.runClient();
		String[] topics = new String[]{"personInBed","personUp","wardrobeOpened"};
		String activity = "";
		
		for (;;){
			if(SUBSCRIBER){
				app.subscribeTO(topics); // include updating application ontology addObservations to local Ontology
				Thread.sleep(20000);
				PUBLISHER = true;
			}
			
			if(PUBLISHER){
				SUBSCRIBER = false;	
				//for (int i = 0; i < 5; i++){			
				//ApplicationRulesQueries wearingClothesquery = new ApplicationRulesQueries();
				ButtonUltrasonicRulesQueries wearingClothesquery = new ButtonUltrasonicRulesQueries();
				BindingSet res = wearingClothesquery.runQuery();				
				if (res != null){
					activity = "Wearing his clothes at time: " + res.toString();
					wearingClothes.addObservation(activity);
					MqttMessage msg = new MqttMessage(activity.getBytes());
					app.sendMessage("behaviour", msg.toString());			
		         		//System.out.print(button.isPressed() ? 1 : 0);	
				}
				Thread.sleep(1000);
				SUBSCRIBER = true;		    
			// app.stopClient();
			}
		}
	
	}
}
