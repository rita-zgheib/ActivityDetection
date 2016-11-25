package usefulClasses;

import org.eclipse.paho.client.mqttv3.MqttException;

import com.grovepi.mqtt.connection.MqttSubscriber;

/**
 * Hello world!
 *
 */
public class App_sub 
{
	public static void main(String[] args) throws MqttException {
		MqttSubscriber app =  new MqttSubscriber("Rita-SemanticMQTT-sub");
		app.runClient();
	}
}
