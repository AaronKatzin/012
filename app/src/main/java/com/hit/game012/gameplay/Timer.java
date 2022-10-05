package com.hit.game012.gameplay;

public class Timer {
    private long startTime;
    private long stopTime;
    public void start(){
        startTime=System.currentTimeMillis();
    }
    public void stop(){
        stopTime=System.currentTimeMillis();
    }
    public Long getTotalTime(){
        return stopTime-startTime;
    }
}
