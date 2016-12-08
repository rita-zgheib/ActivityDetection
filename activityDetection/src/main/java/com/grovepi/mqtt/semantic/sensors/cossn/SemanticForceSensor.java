package com.grovepi.mqtt.semantic.sensors.cossn;

import com.grovepi.physical.sensors.jgrove.ForceSensor;

public class SemanticForceSensor {
	// runs on arduino 
	public static void main(String[] args) throws Exception {
		ForceSensor main = new ForceSensor();
		main.initialize();
		Thread t=new Thread() {
			public void run() {
				//the following line will keep this app alive for 1000 seconds,
				//waiting for events to occur and responding to them (printing incoming messages to console).
				try {Thread.sleep(1000000);} catch (InterruptedException ie) {}
			}
		};
		t.start();
		System.out.println("Started");
	}

}
