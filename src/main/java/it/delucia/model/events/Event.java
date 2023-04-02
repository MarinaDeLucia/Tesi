package it.delucia.model.events;

public abstract class Event {

    private int step;

    public Event(int step) {
        this.step = step;
    }

    public int getStep() {
        return step;
    }
}
