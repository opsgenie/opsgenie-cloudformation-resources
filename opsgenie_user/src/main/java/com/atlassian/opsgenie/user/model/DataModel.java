package com.atlassian.opsgenie.user.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class DataModel {

    private String result;
    @JsonProperty("fullName")
    private String fullName;
    private String username;
    @JsonProperty("id")
    private String Id;
    private UserRole role;




}
