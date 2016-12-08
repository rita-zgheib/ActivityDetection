package com.grovepi.mqtt.virtualSemanticSensor;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.repository.RepositoryException;
import org.eclipse.rdf4j.rio.RDFParseException;

import com.grovepi.mqtt.connection.MqttPublisherSubscriber;
import com.grovepi.semantic.enrichment.VirtualSemanticSensor;

public class TakingShowerCognitiveSensor {
	private static  boolean PUBLISHER = false;
	private static  boolean SUBSCRIBER = true;
	
	public static void main(String[] args) throws MqttException, InterruptedException, RDFParseException, RepositoryException, IOException {
		
	VirtualSemanticSensor takingShower = new VirtualSemanticSensor("ShowerSensor",
			"ShowerSensor1", "shower","activity", "");
	takingShower.addSensorToOntology();		
	
	MqttPublisherSubscriber app =  new MqttPublisherSubscriber("shower-Client"); 
	app.runClient();
	String[] topics = new String[]{"personInBed","personUp","showerON"};
	ApplicationRulesQueries wearingClothesquery = new ApplicationRulesQueries();
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

			//ButtonUltrasonicRulesQueries wearingClothesquery = new ButtonUltrasonicRulesQueries();
			BindingSet res = wearingClothesquery.runQuery("shower");				
			if (res != null){
				activity = "Taking shower at time: " + res.toString();
				takingShower.addObservation(activity);
				MqttMessage msg = new MqttMessage(activity.getBytes());
				app.sendMessage("activity", msg.toString());			
	         		//System.out.print(button.isPressed() ? 1 : 0);	
			}
			Thread.sleep(1000);
			SUBSCRIBER = true;		    
		// app.stopClient();
		}
	}
	
	}

}
