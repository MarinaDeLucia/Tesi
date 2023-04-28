package it.delucia.plotter;

public class ScheduledResourceLoad extends ScheduledEvent{

        public ScheduledResourceLoad(String name, int start, String payload) {
            super(name, 0, start, 0, payload, EventType.RESOURCE_LOAD);
        }
}
