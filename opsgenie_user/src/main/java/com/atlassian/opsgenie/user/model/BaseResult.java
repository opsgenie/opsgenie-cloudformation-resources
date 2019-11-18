package com.atlassian.opsgenie.user.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

@JsonPropertyOrder(alphabetic = true)
@Data
public abstract class BaseResult {

    private String result;

    @JsonProperty("took")
    private double took;

    private String requestId;
}