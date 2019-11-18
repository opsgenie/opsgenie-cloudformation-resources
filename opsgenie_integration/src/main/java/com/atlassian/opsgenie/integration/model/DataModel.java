package com.atlassian.opsgenie.integration.model;


import com.atlassian.opsgenie.integration.RespondersProperty;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class DataModel {

    private String Id;
    private String name;
    private String type;
    private String ownerTeamId;
    private String apiKey;
    private OwnerTeam ownerTeam;
    private boolean enabled;
    private boolean allowReadAccess;
    private boolean allowWriteAccess;
    private boolean allowDeleteAccess;
    private boolean allowConfigurationAccess;
    private boolean suppressNotifications;
    private boolean ignoreRespondersFromPayload;
    private boolean ignoreTeamsFromPayload;
    @JsonProperty("responders")
    private List<RespondersProperty> respondersPropertyList;


}
