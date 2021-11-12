package com.atlassian.opsgenie.schedule.model;

import lombok.Data;

@Data
public class CreateScheduleResponse {

    private DataModel Data;
    private String requestId;
    private double took;
}
