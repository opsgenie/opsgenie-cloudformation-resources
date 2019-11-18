package com.atlassian.opsgenie.user.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserRole {
    private String Id;
    private String name;
}
