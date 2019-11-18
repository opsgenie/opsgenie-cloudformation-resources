package com.atlassian.opsgenie.user.model;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class AddUserRequest{
    @JsonProperty("fullName")
    private String fullname;
    private String username;
    private String locale;
    private String timezone;
    private UserRole role;


}
