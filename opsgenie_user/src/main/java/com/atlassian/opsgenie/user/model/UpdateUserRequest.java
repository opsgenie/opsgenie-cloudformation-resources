package com.atlassian.opsgenie.user.model;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class UpdateUserRequest {
    @JsonProperty("fullName")
    private String fullname;
    private String username;
    private UserRolerRequest role;

}
