package it.delucia.plotter;

public class ScheduledJobArrival extends ScheduledEvent {

    public ScheduledJobArrival(String name, int start, String payload) {
        super(name, 0, start, 0, payload, EventType.JOB_ARRIVAL);
    }
}
