package com.atlassian.opsgenie.schedule.model;

import lombok.Data;

@Data
public class GetScheduleResponse {

    private DataModel Data;
    private String requestId;
    private double took;
}
