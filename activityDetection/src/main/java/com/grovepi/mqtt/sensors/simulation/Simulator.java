package com.grovepi.mqtt.sensors.simulation;

import java.util.Random;

public class Simulator implements Sensor {

    private Random random;

    public Simulator() {
        random = new Random(System.currentTimeMillis());
    }

    @Override
    public String read() {
        return "" + random.nextInt(1000);
    }

}
