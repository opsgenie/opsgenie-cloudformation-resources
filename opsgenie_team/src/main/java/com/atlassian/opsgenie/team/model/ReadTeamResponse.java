package com.atlassian.opsgenie.team.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class ReadTeamResponse extends OGResponse {
    
    @JsonProperty("data")
    private TeamDataModel teamDataModel;
}
