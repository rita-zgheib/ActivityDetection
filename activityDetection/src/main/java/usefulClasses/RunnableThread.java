package usefulClasses;

import java.io.FileNotFoundException;

import org.eclipse.paho.client.mqttv3.MqttException;

import com.grovepi.mqtt.semantic.sensor.groveButtonSensorThread;
import com.grovepi.mqtt.semantic.sensor.groveUltrasonicSensorThread;

class SensorsThreads {

	public static void main(String[] args) throws FileNotFoundException, MqttException {
		
		Thread threadButton = new Thread(new groveButtonSensorThread("ButtonThread"), "thread1");
		
		Thread threadUltrasonic = new Thread(new groveUltrasonicSensorThread("UltrasonicTherad"), "thread2");
		//Thread threadForce = new RunnableThread("thread3");
		//Start the threads
		threadButton.start();
		threadUltrasonic.start();
		try {
			//delay for one second
			Thread.currentThread().sleep(1000);
		} catch (InterruptedException e) {
		}
		//Display info about the main thread
		System.out.println(Thread.currentThread());
	}
}
