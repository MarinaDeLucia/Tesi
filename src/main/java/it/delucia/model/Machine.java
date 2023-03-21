package it.delucia.model;

public class Machine {
    private int id;
    private int processingTime;

    public Machine(int id, int processingTime) {
        this.id = id;
        this.processingTime = processingTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProcessingTime() {
        return processingTime;
    }

    public void setProcessingTime(int processingTime) {
        this.processingTime = processingTime;
    }

    public String getMachineName(){
        return "M" + id;
    }

    //toString method
    @Override
    public String toString() {
        return "Machine{" + "id=" + id + ", processingTime=" + processingTime + '}';
    }
}
