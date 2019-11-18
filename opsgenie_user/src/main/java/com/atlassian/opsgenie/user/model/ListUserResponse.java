package com.atlassian.opsgenie.user.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ListUserResponse extends BaseResult {
    @JsonProperty("data")
    private List<DataModel> dataModel;
    private int totalCount;

}
