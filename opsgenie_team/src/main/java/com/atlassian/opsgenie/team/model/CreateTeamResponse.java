package com.atlassian.opsgenie.team.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class CreateTeamResponse extends OGResponse {
    
    @JsonProperty("data")
    private CreateTeamResponseData createTeamResponseData;
}
