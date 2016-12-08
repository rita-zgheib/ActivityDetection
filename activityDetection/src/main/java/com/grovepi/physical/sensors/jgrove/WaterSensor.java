package com.grovepi.physical.sensors.jgrove;
 import grovepi.GrovePi;

public class WaterSensor {
	
	grovepi.sensors.WaterSensor groveWaterSens;
	
	public WaterSensor(int pin) {
		
		GrovePi grovePi = new GrovePi();
		groveWaterSens = grovePi.getDeviceFactory().createWaterSensor
				(pin);
		//(Pin.ANALOG_PIN_0);
	}
	
	public int getValue (){
		/*String state = "wet";
		for(;;) {     			
			if (capteur.getValue()<600 && state=="dry") {
				state = "wet";  
				System.out.println(state + capteur.getValue());
			}
			if(capteur.getValue()>600 && state=="wet"){
				state = "dry";
				System.out.println(state + capteur.getValue());
			}
		}*/
		return groveWaterSens.getValue();
	
	}
		
	
}