package com.atlassian.opsgenie.team.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

@JsonPropertyOrder(alphabetic = true)
@Data
public class OGResponse {

    @JsonProperty("result")
    private String result;

    @JsonProperty("took")
    private double took;

    @JsonProperty("requestId")
    private String requestId;
}
