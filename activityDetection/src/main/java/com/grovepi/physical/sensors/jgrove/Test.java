package com.grovepi.physical.sensors.jgrove;
import grovepi.GrovePi;
import grovepi.Pin;
import grovepi.sensors.*;


public class Test {
	public static void main(String[] args) {
		GrovePi grovePi = new GrovePi();
		UltrasonicRangerSensor ultraSens = grovePi.getDeviceFactory().createUltraSonicSensor(Pin.DIGITAL_PIN_4);
		ButtonSensor button = grovePi.getDeviceFactory().createButtonSensor(3);
		
		for(;;) {
			if (ultraSens.getDistance() < 30) {
				System.out.println("obstacle at: " + ultraSens.getDistance());
			}
			if(button.isPressed()){
						System.out.println("button pressed");
					}
			
		}

	}
}