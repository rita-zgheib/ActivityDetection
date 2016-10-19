package usefulClasses;

import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttTopic;

public class publisherSubscriber implements MqttCallback{
	
	MqttClient myClient;
	MqttConnectOptions connOpt;
	
	static final String broker = "tcp://localhost:1883";
	static final Boolean subscriber = true;
	static final Boolean publisher = true;
	
	@Override 
	public void connectionLost (Throwable t)
	{
		System.out.println("Connection Lost");
	}

	@Override
	public void messageArrived(String topic, MqttMessage message) throws Exception {
		System.out.println("----------------------");
		System.out.println("|Topic: "+topic);
		System.out.println("|Message: "+new String(message.getPayload()));
		System.out.println("-----------------------");
	}

	@Override
	public void deliveryComplete(IMqttDeliveryToken token) {
		// TODO Auto-generated method stub
	}
	public static void main(String args[])
	{
		publisherSubscriber smc = new publisherSubscriber();
		smc.runClient();
	}
	
	public void runClient()
	{
		String clientID = "MqttExample";
		connOpt = new MqttConnectOptions();
		connOpt.setCleanSession(true);
		connOpt.setKeepAliveInterval(30);
		try
		{
			myClient = new MqttClient(broker, clientID);
			myClient.setCallback(this);
			myClient.connect(connOpt);
		} catch (MqttException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		System.out.println("Connected to " + broker);
		String myTopic = "pahodemo/test";
		MqttTopic topic = myClient.getTopic(myTopic);
		
		if (subscriber) {
			try {
				int subQoS = 0;
				myClient.subscribe(myTopic, subQoS);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		/*if (publisher) {
			for (int i=1; i<=10; i++) {
		   		String pubMsg = "{\"pubmsg\":" + i + "}";
		   		int pubQoS = 0;
				MqttMessage message = new MqttMessage(pubMsg.getBytes());
		    	message.setQos(pubQoS);
		    	message.setRetained(false);
		    	// Publish the message
		    	System.out.println("Publishing to topic \"" + topic + "\" qos " + pubQoS);
		    	MqttDeliveryToken token = null;
		    	try {
		    		// publish message to broker
					token = topic.publish(message);
			    	// Wait until the message has been delivered to the broker
					token.waitForCompletion();
					Thread.sleep(100);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}			
		}*/
		try {
			// wait to ensure subscribed messages are delivered
			if (subscriber) {
				Thread.sleep(50000);
			}
			myClient.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
