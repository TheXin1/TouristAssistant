package org.datateam.touristassistant.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Itinerary {
    @JsonProperty("itinerary")
    private List<DayRoute> itinerary;

    public List<DayRoute> getItinerary() {
        return itinerary;
    }

    public void setItinerary(List<DayRoute> itinerary) {
        this.itinerary = itinerary;
    }

    @Override
    public String toString() {
        return "Itinerary{" +
                "itinerary=" + itinerary +
                '}';
    }
}
