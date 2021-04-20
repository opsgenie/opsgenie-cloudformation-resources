
package com.atlassian.opsgenie.integration.model;

import java.util.List;
import lombok.Data;

@Data
public class ListIntegrationResponse {

    private List<DataModel> data;
    private String requestId;
    private Double took;

}
