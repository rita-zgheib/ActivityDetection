package com.grovepi.physical.sensors.jgrove;

import grovepi.GrovePi;
import grovepi.Pin;
import grovepi.sensors.ButtonSensor;

public class groveButtonSensor {
	private ButtonSensor button;

	public groveButtonSensor() {
		GrovePi grovePi = new GrovePi();
		button = grovePi.getDeviceFactory().createButtonSensor(Pin.DIGITAL_PIN_4);		
	}
	
	public boolean buttonPressed(){		
		if (button.isPressed())
			return true;
		else
			return false;		
	}
}
