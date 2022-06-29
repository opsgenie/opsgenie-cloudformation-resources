package com.atlassian.opsgenie.integration.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CreateIntegrationResponse {

    @JsonProperty("data")
    private DataModel dataModel;
    private String requestId;
    private double took;
}
