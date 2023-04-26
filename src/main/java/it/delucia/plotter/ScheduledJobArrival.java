package it.delucia.plotter;

public class ScheduledJobArrival extends ScheduledEvent {

    public ScheduledJobArrival(String name, int machine, int start, String payload) {
        super(name, machine, start, 0, payload, EventType.JOB_ARRIVAL);
    }
}
