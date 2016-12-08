package usefulClasses;

import java.io.FileNotFoundException;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.rdf4j.model.Model;

import com.grovepi.mqtt.connection.MqttPublisher;
import com.grovepi.physical.sensors.jgrove.groveButtonSensor;
import com.grovepi.semantic.enrichment.SemanticSensor;

import grovepi.Pin;

//public class groveButtonSensor extends GroveSensor {
public class groveButtonSensorThread implements Runnable{
	
		private groveButtonSensor button;
		private SemanticSensor ButtonSensor;
		private MqttPublisher app;
		Thread runner;
		
	    public groveButtonSensorThread(String name) throws FileNotFoundException, MqttException {
		button = new groveButtonSensor(Pin.DIGITAL_PIN_3);
		ButtonSensor = new SemanticSensor("ButtonSensor", //button or groveSensorsId
				"wardrobeButtonPIN4", "wardrobeOpened","behaviour",""); 
		ButtonSensor.addSensorToOntology();  
		app = new MqttPublisher("ButtonClient-Pub");
		app.runClient();
		runner.start();
	}
	    /*
	groveButtonSensorThread(String threadName) {
		super(threadName); // Initialize thread.
		System.out.println(this);
		start();
	}
	*/
		
	public void run() {	
		if(button.buttonNotPressed()){
		    try {
				ButtonSensor.addObservation(0);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    Model result = ButtonSensor.getSensorOutput();
			String resultat = result.toString();
			String res = resultat.substring(2, resultat.length()-2);
			MqttMessage msg = new MqttMessage(res.toString().getBytes());
			//MqttMessage msg = new MqttMessage(" testButton".toString().getBytes());
			try {
				app.sendMessage("wardrobeOpened", msg.toString());
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
			//Thread.sleep(1000);
		}
	}
/*	
	public static void main(String[] args) throws Exception {
		Thread threadButton = new Thread(new groveButtonSensorThread("ButtonThread"));
		threadButton.start();
		//Thread threadUltrasonic = new Thread(new RunnableThread(), "thread1");		
	}
	*/
}
