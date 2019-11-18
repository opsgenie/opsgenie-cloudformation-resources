package com.atlassian.opsgenie.integration.model;

import lombok.Data;

@Data
public class GetIntegrationResponse {

    private DataModel Data;
    private String requestId;
    private double took;
}
