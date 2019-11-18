package com.atlassian.opsgenie.integration.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OwnerTeam {
    private String name;
    private String id;
}
