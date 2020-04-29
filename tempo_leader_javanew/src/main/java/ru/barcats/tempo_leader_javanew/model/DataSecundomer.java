package ru.barcats.tempo_leader_javanew.model;

public class DataSecundomer {

    String number;
    String time;
    String delta;

    public DataSecundomer(String number, String time, String delta) {
        this.number = number;
        this.time = time;
        this.delta = delta;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDelta() {
        return delta;
    }

    public void setDelta(String delta) {
        this.delta = delta;
    }
}
