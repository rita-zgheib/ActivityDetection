package com.grovepi.mqtt.virtualSemanticSensor;

import java.util.HashMap;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import com.grovepi.semantic.enrichment.LocalOntology;
import com.grovepi.semantic.enrichment.VirtualSemanticSensor;


public class VirtualSemanticSubscriber implements MqttCallback {
    MqttClient client;
    private MqttConnectOptions connectOptions;
    
    private static final String BROKER_URL = "tcp://m2m.eclipse.org:1883";
	//private static final String BROKER_URL = "localhost:1883";
	 // private static final String BROKER_URL = "192.168.10.8:1883";
	private static final String MY_MQTT_CLIENT_ID = "Rita-SemanticMQTT-sub";
	//private static final Logger LOG = LoggerFactory.getLogger(SemanticPublisher.class);
	private static final boolean PUBLISHER = true;
	private static final boolean SUBSCRIBER = true;
//	private HashMap<String, MqttMessage > result;
	
	private static final int RETRIES = 3;
	
	
	public VirtualSemanticSubscriber() throws MqttException {
		client = new MqttClient(BROKER_URL, MY_MQTT_CLIENT_ID);
	    client.setCallback(this);
	//    result = new HashMap<String, MqttMessage >();
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
	public void subscribeTO(String[] topic) throws MqttException{		
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
	//	LocalOntology.updateOntology(topic, "activity", new String(message.getPayload()));
		System.out.println("New message on topic: " + topic + " is: " + message);
		System.out.println("-------------------------------------------------");
		System.out.println("| Topic:" + topic);
		System.out.println("| Message: " + new String(message.getPayload()));
		System.out.println("-------------------------------------------------");
	//	result.put(topic, message);
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

	public static void main(String[] args) throws MqttException {
		VirtualSemanticSubscriber app =  new VirtualSemanticSubscriber();
		app.runClient();
		String[] topics = new String[]{"personInBed","personUp","wardrobeOpened"};
		app.subscribeTO(topics);
		//app.subscribeTO("personInBed");
		//app.subscribeTO("personUp");
		//app.subscribeTO("wardrobeOpened");
	}



}
