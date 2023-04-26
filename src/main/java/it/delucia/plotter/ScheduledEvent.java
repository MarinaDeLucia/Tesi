package it.delucia.plotter;

//public class ScheduledJob {
//
//    private String name;
//    private int machine;
//    private int start;
//    private int duration;
//
//
//}


//jobs = [{'name': 'Job1', 'type': 'job', 'machine': 1, 'start': 0, 'duration': 3},
//        {'name': 'Job2', 'type': 'job', 'machine': 3, 'start': 3, 'duration': 5},
//        {'name': 'Event1', 'type': 'event', 'machine': 1, 'start': 2, 'payload': 'some text'},
//        {'name': 'Event2', 'type': 'event', 'machine': 3, 'start': 5, 'payload': 'some other text'}]

//create a class like the commented one that is able to be parsed to a json object using gson

public abstract class ScheduledEvent {

    private String name;
    private EventType type;
    private int machine;
    private int start;
    private int duration;
    private String payload;

    public ScheduledEvent(String name, int machine, int start, int duration, String payload, EventType type) {
        this.name = name;
        this.machine = machine;
        this.start = start;
        this.duration = duration;
        this.payload = payload;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public int getMachine() {
        return machine;
    }

    public int getStart() {
        return start;
    }

    public int getDuration() {
        return duration;
    }

    public String getPayload() {
        return payload;
    }

    public EventType getType() {
        return type;
    }
}