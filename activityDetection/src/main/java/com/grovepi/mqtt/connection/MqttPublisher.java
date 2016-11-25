package com.grovepi.mqtt.connection;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttTopic;
import org.eclipse.paho.client.mqttv3.MqttDeliveryToken;

public class MqttPublisher implements MqttCallback {

	  private MqttClient myClient;
	  private MqttConnectOptions connectOptions;

	  private static final String BROKER_URL = "tcp://m2m.eclipse.org:1883";
	 // private static final String BROKER_URL = "localhost:1883";
	 // private static final String BROKER_URL = "192.168.10.8:1883";
	 // private static final String MY_MQTT_CLIENT_ID = "Rita-SemanticMQTT-pub";
	  
	 // private static final String DEFAULT_TOPIC = "Activity/button";
	  //private static final Logger LOG = LoggerFactory.getLogger(SemanticPublisher.class);
	 // private String sensorTopic;
	  private static final boolean PUBLISHER = true;
	  private static final boolean SUBSCRIBER = false;

	  private static final int RETRIES = 3;
	  
	  public MqttPublisher(String MY_MQTT_CLIENT_ID) throws MqttException {
		    myClient = new MqttClient(BROKER_URL, MY_MQTT_CLIENT_ID);
		    myClient.setCallback(this);
		    
	  }

	  public void connectionLost(Throwable arg0) {
		// TODO Auto-generated method stub
		 System.out.println("Connection lost!");		
	 }

	  public void deliveryComplete(IMqttDeliveryToken token) {
		// TODO Auto-generated method stub
		 System.out.println("Devliery completed with token ::");
		// System.out.println("Message Id :: " + token.getMessageId());
		// System.out.println("Response :: " + token.getResponse().toString());
		try {
			//System.out.println("Pub complete" + new String(token.getMessage().getPayload()));
			System.out.println("Pub complete for message: " + new String(token.getMessage().getPayload()));
		} catch (MqttException e) {
		// TODO Auto-generated catch block
				e.printStackTrace();
		}
		
	 }

	 public void messageArrived(String arg0, MqttMessage arg1) throws Exception {
		    System.out.println("Recieved Message :: -----------------------------");
		    System.out.println("| Topic:" + arg0);
		    System.out.println("| Message: " + new String(arg1.getPayload()));
		    System.out.println("End ---------------------------------------------");
		  }
	 
	  public void runClient() {

		    connectOptions = new MqttConnectOptions();
		    connectOptions.setCleanSession(true);
		    connectOptions.setKeepAliveInterval(100);

		    try {

		      System.out.println("Attempting Connection to " + BROKER_URL);
		      myClient.connect(connectOptions);
		      System.out.println("Connected to " + BROKER_URL);
		      

		    } catch (MqttException me) {

		      System.err.println(me.getMessage());
		      System.err.println(me.getStackTrace());
		      System.exit(-1);
		    }

		  }
	  public void sendMessage(String topic, String message) throws InterruptedException {

		    System.out.println("Building message with " + message.getBytes().length + " bytes of payload");
		    MqttMessage mqttMessage = new MqttMessage(message.getBytes());
		    mqttMessage.setQos(0);
		    mqttMessage.setRetained(false);

		    MqttTopic mqttTopic = myClient.getTopic(topic);

		    MqttDeliveryToken token = null;

		    try {
		      token = mqttTopic.publish(mqttMessage);
		      Thread.sleep(100);
		      token.waitForCompletion();
		    } catch (MqttException e) {
		      // TODO Auto-generated catch block
		      e.printStackTrace();
		    }

		    if (null != token) {
		      System.out.println("Message for topic :: " + mqttTopic);
		      //System.out.println("Published with Token :: " +token);
		      //System.out.println("Message id is: "+token.getMessageId());
		      System.out.println("Message is: "+mqttMessage.toString());
		    }
		  }
	  
	 public void stopClient() throws MqttException {
			System.out.println("Trying to disconnect from " + BROKER_URL);  
		    myClient.disconnect();
		    System.out.println("Disonnected from " + BROKER_URL);
		    System.exit(0);
		  }
/*
	public static void main(String[] args) throws Exception {
		GrovePi grovePi = new GrovePi();
		ButtonSensor button = grovePi.getDeviceFactory().createButtonSensor(Pin.DIGITAL_PIN_4);
		groveMqttPublisher app = new groveMqttPublisher();
		String message = "ButtonPressed";
		MqttMessage msg = new MqttMessage(message.getBytes());
		app.runClient();
		for(;;) {
			if(button.isPressed()){
			   app.sendMessage(DEFAULT_TOPIC, msg.toString());
			}
            		//System.out.print(button.isPressed() ? 1 : 0);
		}
		// app.stopClient();
	}
*/
}