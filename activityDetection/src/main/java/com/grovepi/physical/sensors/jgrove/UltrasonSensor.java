package com.grovepi.physical.sensors.jgrove;

import grovepi.GrovePi;
import grovepi.Pin;
import grovepi.sensors.UltrasonicRangerSensor;


public class UltrasonSensor {
	private UltrasonicRangerSensor ultrasonicSensor;
	
	public UltrasonSensor(int pin) {
		GrovePi grovePi = new GrovePi();
		//button = grovePi.getDeviceFactory().createButtonSensor(Pin.DIGITAL_PIN_4);
		ultrasonicSensor = grovePi.getDeviceFactory().createUltraSonicSensor(pin);
	}
	
	public int getDistance (){
		int distance = 0;
		distance = ultrasonicSensor.getDistance();
		return distance;
		
	}
/*	
	public static void main(String[] args) {
		UltrasonSensor ultraSens = new UltrasonSensor(Pin.DIGITAL_PIN_4);

		for(;;) {
			if (ultraSens.getDistance() < 30) {
 
				System.out.println("obstacle at: " + ultraSens.getDistance());
			}
			
		}
	}*/
}
