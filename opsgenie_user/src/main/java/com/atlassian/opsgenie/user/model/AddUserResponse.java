package com.atlassian.opsgenie.user.model;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class AddUserResponse extends BaseResult{

    @JsonProperty("data")
    private DataModel dataModel;

}
