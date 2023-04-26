package it.delucia.plotter;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import com.fasterxml.jackson.annotation.JsonValue;

public enum EventType {
    //generate a enum class ready for Jasckon parsing with those items: JOB, EVENT, RESOURCE_LOAD, JOB_ARRIVAL
    JOB("JOB"),
    @JsonEnumDefaultValue
    EVENT("EVENT"),
    RESOURCE_LOAD("RESOURCE_LOAD"),
    JOB_ARRIVAL("JOB_ARRIVAL");

    private final String type;

    EventType(String type) {
        this.type = type;
    }

    @JsonValue
    public String getType() {
        return type;
    }

    public static EventType of(String type) {
        for (EventType eventType : values()) {
            if (eventType.type.equalsIgnoreCase(type)) {
                return eventType;
            }
        }
        throw new IllegalArgumentException("No constant with text " + type + " found");
    }


}
