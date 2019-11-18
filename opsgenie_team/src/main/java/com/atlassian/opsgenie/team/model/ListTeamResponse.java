package com.atlassian.opsgenie.team.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
public class ListTeamResponse extends OGResponse {
    
    @JsonProperty("data")
    private List<TeamDataModel> teamDataModel;
}
