package com.atlassian.opsgenie.schedule.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Rotation {

    private String Id;
    private String name;
    private String startDate;
    private String endDate;
    private String type;
    private int length;
    @JsonProperty("participants")
    private List<Participant> participants;
    private TimeRestriction timeRestriction;

}
