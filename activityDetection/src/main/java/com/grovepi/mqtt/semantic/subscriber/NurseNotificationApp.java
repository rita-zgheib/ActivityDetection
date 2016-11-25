package com.grovepi.mqtt.semantic.subscriber;

import org.eclipse.paho.client.mqttv3.MqttException;

import com.grovepi.mqtt.connection.MqttPublisherSubscriber;
import com.grovepi.mqtt.connection.MqttSubscriber;

public class NurseNotificationApp {
	
	public static void main(String[] args) throws MqttException, InterruptedException {
		MqttSubscriber app =  new MqttSubscriber("NurseClient-Sub");
		app.runClient();
		String[] topics = new String[]{"behaviour"};
		//String[] topics = new String[]{"personInBed", "wardrobeOpened","personUp"};
		app.subscribeTO(topics);
	}
}
