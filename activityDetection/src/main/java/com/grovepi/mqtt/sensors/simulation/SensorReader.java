package com.grovepi.mqtt.sensors.simulation;

public class SensorReader implements Runnable {

   // private Publisher publisher;
    private Sensor sensor;
    private String topic;
    private int delay;

    public SensorReader() {}
/*
    public SensorReader(Publisher publisher, Sensor sensor, String topic, int delay) {
        this.publisher = publisher;
        this.sensor = sensor;
        this.topic = topic;
        this.delay = delay;
    }
*/
    @Override
    public void run() {
        while (true) {

           // publisher.publish(topic, () -> sensor.read().getBytes());

            try {
                Thread.sleep(delay);
            } catch (InterruptedException e) {}
        }
    }
}