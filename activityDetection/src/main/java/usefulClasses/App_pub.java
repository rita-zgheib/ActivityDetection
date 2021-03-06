package usefulClasses;

import org.eclipse.paho.client.mqttv3.MqttMessage;

import com.grovepi.mqtt.connection.MqttPublisher;
import com.grovepi.physical.sensors.jgrove.groveButtonSensor;

import grovepi.Pin;


/**
 * Hello world!
 *
 */
public class App_pub 
{
	public static void main(String[] args) throws Exception {
		String topic = "Activity/button"; 
		groveButtonSensor button = new groveButtonSensor(Pin.DIGITAL_PIN_4);	
		MqttPublisher app = new MqttPublisher("Rita-SemanticMQTT-pub");
		String message = "ButtonPressed";
		//To semantic
		MqttMessage msg = new MqttMessage(message.getBytes());
		app.runClient();
		for(;;) {
			if(button.buttonPressed()){
			   app.sendMessage(topic, msg.toString());
			}
            		//System.out.print(button.isPressed() ? 1 : 0);
		}
		// app.stopClient();
	}
}
