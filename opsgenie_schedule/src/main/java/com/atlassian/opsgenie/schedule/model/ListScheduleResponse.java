package com.atlassian.opsgenie.schedule.model;

import java.util.List;
import lombok.Data;

@Data
public class ListScheduleResponse {

    private List<DataModel> data;
    private String requestId;
    private Double took;

}
