package com.atlassian.opsgenie.schedule.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CreateScheduleRequest {

    private String name;
    private String description;
    private OwnerTeam ownerTeam;
    private String timezone;
    private boolean enabled;

}
