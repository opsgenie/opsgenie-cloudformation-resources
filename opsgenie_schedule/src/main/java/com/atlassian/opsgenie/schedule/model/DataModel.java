package com.atlassian.opsgenie.schedule.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class DataModel {

    private String Id;
    private String name;
    private String description;
    private String timezone;
    private boolean enabled;
    private OwnerTeam ownerTeam;
    @JsonProperty("rotations")
    private List<Rotation> rotations;


}
