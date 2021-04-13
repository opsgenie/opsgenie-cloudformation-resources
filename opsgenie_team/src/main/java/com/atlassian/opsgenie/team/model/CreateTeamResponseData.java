package com.atlassian.opsgenie.team.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@EqualsAndHashCode(callSuper = false)
public class CreateTeamResponseData {

    @JsonProperty("id")
    private String id;

    @JsonProperty("name")
    private String name;
}
