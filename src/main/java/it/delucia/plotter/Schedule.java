package it.delucia.plotter;

import java.util.List;

public class Schedule {

    private List<ScheduledEvent> events = new java.util.LinkedList<>();

    public Schedule() {
    }

    public Schedule(List<ScheduledEvent> events) {
        this.events = events;
    }

    public List<ScheduledEvent> getEvents() {
        return events;
    }

    public void setEvents(List<ScheduledEvent> events) {
        this.events = events;
    }

    public void addEvent(ScheduledEvent event) {
        events.add(event);
    }

    public void clear() {
        events.clear();
    }


}

//jobs = [{'name': 'Job1', 'type': 'job', 'machine': 1, 'start': 0, 'duration': 3},
//        {'name': 'Job2', 'type': 'job', 'machine': 3, 'start': 3, 'duration': 5},
//        {'name': 'Event1', 'type': 'event', 'machine': 1, 'start': 2, 'payload': 'some text'},
//        {'name': 'Event2', 'type': 'event', 'machine': 3, 'start': 5, 'payload': 'some other text'}]