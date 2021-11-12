package com.atlassian.opsgenie.schedule.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class TimeRestriction {

    private String type;
    @JsonProperty("restrictions")
    private List<Restriction> restrictions;

}
