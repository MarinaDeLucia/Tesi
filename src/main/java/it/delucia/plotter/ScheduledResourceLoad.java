package it.delucia.plotter;

public class ScheduledResourceLoad extends ScheduledEvent{

        public ScheduledResourceLoad(String name, int machine, int start, String payload) {
            super(name, machine, start, 0, payload, EventType.RESOURCE_LOAD);
        }
}
