package com.atlassian.opsgenie.integration.model;

import com.atlassian.opsgenie.integration.RespondersProperty;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class UpdateIntegrationRequest {
    private String Id;
    private String name;
    private String type;
    private OwnerTeam ownerTeam;
    private boolean enabled;
    private boolean suppressNotifications;
    private boolean ignoreRespondersFromPayload;
    private boolean ignoreTeamsFromPayload;
    private List<RespondersProperty> respondersPropertyList;
}
