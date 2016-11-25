package com.grovepi.physical.sensors.jgrove;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import gnu.io.CommPortIdentifier; 
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent; 
import gnu.io.SerialPortEventListener; 
import java.util.Enumeration;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.rdf4j.model.Model;

import com.grovepi.mqtt.connection.MqttPublisher;
import com.grovepi.semantic.enrichment.SemanticSensor;


public class ForceSensor implements SerialPortEventListener {
	SerialPort serialPort;
        /** The port we're normally going to use. */
	private static final String PORT_NAMES[] = { 
			"/dev/tty.usbserial-A9007UX1", // Mac OS X
                        "/dev/ttyACM0", // Raspberry Pi
			"/dev/ttyUSB0", // Linux
			"COM3", // Windows
	};
	/**
	* A BufferedReader which will be fed by a InputStreamReader 
	* converting the bytes into characters 
	* making the displayed results codepage independent
	*/
	private BufferedReader input;
	/** The output stream to the port */
	private OutputStream output;
	/** Milliseconds to block while waiting for port open */
	private static final int TIME_OUT = 2000;
	/** Default bits per second for COM port. */
	private static final int DATA_RATE = 9600;
	private MqttPublisher app;
	private SemanticSensor ForceSensor;

	public void initialize() throws FileNotFoundException, MqttException {
                // the next line is for Raspberry Pi and 
                // gets us into the while loop and was suggested here was suggested http://www.raspberrypi.org/phpBB3/viewtopic.php?f=81&t=32186
              //  System.setProperty("gnu.io.rxtx.SerialPorts", "/dev/ttyACM0");

		CommPortIdentifier portId = null;
		Enumeration portEnum = CommPortIdentifier.getPortIdentifiers();
		ForceSensor = new SemanticSensor("ForceSensor", //button or groveSensorsId
				"ForceSensorFSR", "personInBed","activity",""); 
		
		//First, Find an instance of serial port as set in PORT_NAMES.
		while (portEnum.hasMoreElements()) {
			CommPortIdentifier currPortId = (CommPortIdentifier) portEnum.nextElement();
			for (String portName : PORT_NAMES) {
				if (currPortId.getName().equals(portName)) {
					portId = currPortId;
					break;
				}
			}
		}
		if (portId == null) {
			System.out.println("Could not find COM port.");
			return;
		}

		try {
			// open serial port, and use class name for the appName.
			serialPort = (SerialPort) portId.open(this.getClass().getName(),
					TIME_OUT);

			// set port parameters
			serialPort.setSerialPortParams(DATA_RATE,
					SerialPort.DATABITS_8,
					SerialPort.STOPBITS_1,
					SerialPort.PARITY_NONE);

			// open the streams
			input = new BufferedReader(new InputStreamReader(serialPort.getInputStream()));
			output = serialPort.getOutputStream();

			// add event listeners
			serialPort.addEventListener(this);
			serialPort.notifyOnDataAvailable(true);
		} catch (Exception e) {
			System.err.println(e.toString());
		}
		ForceSensor.addSensorToOntology();  
		app = new MqttPublisher("ForceClient-Pub");
		app.runClient();
	}

	/**
	 * This should be called when you stop using the port.
	 * This will prevent port locking on platforms like Linux.
	 */
	public synchronized void close() {
		if (serialPort != null) {
			serialPort.removeEventListener();
			serialPort.close();
		}
	}

	/**
	 * Handle an event on the serial port. Read the data and print it.
	 */
	public synchronized void serialEvent(SerialPortEvent oEvent) {
		if (oEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
			try {
				String inputLine=null;
				if (input.ready()) {
	                inputLine = input.readLine();
	               // System.out.println(inputLine);
	                String[] res = inputLine.split(" ");
	                if(res[0].equals("Force:")){
	                	double force = Double.parseDouble(res[res.length-2]);
	                	System.out.println("Force is: " + force);
	                	ForceSensor.addObservation((int)force);
		       			Model result = ForceSensor.getSensorOutput();
		       			String resultat = result.toString();
		       			String forceRes = resultat.substring(2, resultat.length()-2);
		       			MqttMessage msg = new MqttMessage(forceRes.toString().getBytes());
		       			 //MqttMessage msg = new MqttMessage("testBed".toString().getBytes());
		       			 app.sendMessage("personInBed", msg.toString());
	                }
	            };
			} catch (Exception e) {
				//System.err.println(e.toString());
				System.out.println("La force est: 0");
			}
		}
		// Ignore all the other eventTypes, but you should consider the other ones.
	}
	/*
	public double getforce(String input) {
		String[] res = input.split(" ");
		double force = Double.parseDouble(res[res.length-2]);
		return force;
	}
*/
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