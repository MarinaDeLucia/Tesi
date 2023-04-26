package it.delucia.plotter;

public class ScheduledJob extends ScheduledEvent{

    public ScheduledJob(String name, int machine, int start, int duration, String payload) {
        super(name, machine, start, duration, payload, EventType.JOB);
    }
}
