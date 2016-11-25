package com.grovepi.physical.sensors.jgrove;

import grovepi.GrovePi;
import grovepi.Pin;
import grovepi.sensors.ButtonSensor;

//public class groveButtonSensor extends GroveSensor {
public class groveButtonSensor {
	private ButtonSensor button;

	public groveButtonSensor(int pin) {
		GrovePi grovePi = new GrovePi();
		//button = grovePi.getDeviceFactory().createButtonSensor(Pin.DIGITAL_PIN_4);
		button = grovePi.getDeviceFactory().createButtonSensor(pin);
	}
	
	public boolean buttonPressed(){		
		if (button.isPressed())
			return true;
		else
			return false;		
	}
	public boolean buttonNotPressed(){		
		if (button.isPressed())
			return false;
		else
			return true;		
	}
}
