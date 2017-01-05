package usefulClasses;

import java.io.FileNotFoundException;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.rdf4j.model.Model;

import com.grovepi.mqtt.connection.MqttPublisher;
import com.grovepi.physical.sensors.jgrove.UltrasonSensor;
import com.grovepi.semantic.enrichment.SemanticSensor;

import grovepi.Pin;

public class groveUltrasonicSensorThread implements Runnable {
	private UltrasonSensor ultrasens;
	private SemanticSensor ultrasemSensor;
	private MqttPublisher app;
	Thread runner;

	public groveUltrasonicSensorThread(String name) throws FileNotFoundException, MqttException {
		ultrasens = new UltrasonSensor(Pin.DIGITAL_PIN_4);
		ultrasemSensor = new SemanticSensor("UltrasonicRangerSensor", //button or groveSensorsId
				"FloorPresenceSensorPin4", "personUp","activity",""); 
		ultrasemSensor.addSensorToOntology();  
		app = new MqttPublisher("UltrasonicClient-Pub");
		app.runClient();
		runner.start();
	}
	public void run() {	
		//if (ultrasens.getDistance() < 30) {
			try {
				ultrasemSensor.addObservation(ultrasens.getDistance(), ultrasemSensor.getDatetime());
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//System.out.println("obstacle at: " + ultraSens.getDistance());
			 Model result = ultrasemSensor.getSensorOutput();
			 String resultat = result.toString();
			 String res = resultat.substring(2, resultat.length()-2);
			 MqttMessage msg = new MqttMessage(res.toString().getBytes());			
			 try {
				app.sendMessage("personUp", msg.toString());
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
}
