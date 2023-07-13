package com.atlassian.opsgenie.schedule.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class UpdateScheduleRequest {

    private String Id;
    private String name;
    private String description;
    private OwnerTeam ownerTeam;
    private String timezone;
    private boolean enabled;
}

