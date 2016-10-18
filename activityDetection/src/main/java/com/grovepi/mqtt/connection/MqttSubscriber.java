package com.grovepi.mqtt.connection;

import java.util.HashMap;
import java.util.LinkedHashSet;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
//import org.semom.semantic.publisher.SemanticPublisher;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

public class MqttSubscriber implements MqttCallback {
    MqttClient client;
    private MqttConnectOptions connectOptions;
    
    private static final String BROKER_URL = "tcp://m2m.eclipse.org:1883";
	//private static final String BROKER_URL = "localhost:1883";
	 // private static final String BROKER_URL = "192.168.10.8:1883";
	private static final String MY_MQTT_CLIENT_ID = "Rita-SemanticMQTT-sub";
	//private static final Logger LOG = LoggerFactory.getLogger(SemanticPublisher.class);
	private static final boolean PUBLISHER = false;
	private static final boolean SUBSCRIBER = true;
	
	private static final int RETRIES = 3;
	
	
	public MqttSubscriber() throws MqttException {
		client = new MqttClient(BROKER_URL, MY_MQTT_CLIENT_ID);
	    client.setCallback(this);

	}

	  public void runClient() {

		    connectOptions = new MqttConnectOptions();
		    connectOptions.setCleanSession(true);
		    connectOptions.setKeepAliveInterval(100);

		    try {

		      System.out.println("Attempting Connection to " + BROKER_URL);
		      client.connect(connectOptions);
		      System.out.println("Connected to " + BROKER_URL);
		      

		    } catch (MqttException me) {

		      System.err.println(me.getMessage());
		      System.err.println(me.getStackTrace());
		      System.exit(-1);
		    }

		  }
	public void subscribeTO(String topic) throws MqttException{		
		client.subscribe(topic);
		MqttMessage m = connectOptions.getWillMessage();
	   // System.out.println("message is " + m.toString());

	    //m.addToOntology() a ajouter
	}

	
	public void connectionLost(Throwable cause) {
	    // TODO Auto-generated method stub
		 System.out.println("Connection lost!");	
	}

	
	public void messageArrived(String topic, MqttMessage message)
	        throws Exception {
		System.out.println("New message on topic: " + topic + "is: " + message);
		System.out.println("-------------------------------------------------");
		System.out.println("| Topic:" + topic.getBytes());
		System.out.println("| Message: " + new String(message.getPayload()));
		System.out.println("-------------------------------------------------");   
	//	updateLocalOntology
	}

	public void deliveryComplete(IMqttDeliveryToken token) {
	    // TODO Auto-generated method stub
	}
	public void stopClient() throws MqttException {
		System.out.println("Trying to disconnect from " + BROKER_URL);  
	    client.disconnect();
	    System.out.println("Disonnected from " + BROKER_URL);
	    System.exit(0);
	}
/*
	public static void main(String[] args) throws MqttException {
		MqttSubscriber app =  new MqttSubscriber();
		app.runClient();
		app.subscribeTO("test");
	}

*/

}
