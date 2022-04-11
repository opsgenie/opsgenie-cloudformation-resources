package com.atlassian.opsgenie.integration;

import com.atlassian.opsgenie.integration.client.OpsgenieClient;
import com.atlassian.opsgenie.integration.client.OpsgenieClientException;
import com.atlassian.opsgenie.integration.model.GetIntegrationResponse;
import com.atlassian.opsgenie.integration.model.OwnerTeam;
import com.atlassian.opsgenie.integration.model.UpdateIntegrationRequest;
import org.apache.commons.lang3.StringUtils;
import software.amazon.cloudformation.proxy.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.atlassian.opsgenie.integration.Helper.*;

public class UpdateHandler extends BaseHandler<CallbackContext, TypeConfigurationModel> {

    @Override
    public ProgressEvent<ResourceModel, CallbackContext> handleRequest(AmazonWebServicesClientProxy proxy, ResourceHandlerRequest<ResourceModel> request, CallbackContext callbackContext,
                                                                       Logger logger, TypeConfigurationModel typeConfiguration) {

        final List<ResourceModel> models = new ArrayList<>();
        final ResourceModel model = request.getDesiredResourceState();
        try {
            OpsgenieClient OGClient = CreateOGClient(typeConfiguration);
            if (StringUtils.isEmpty(model.getIntegrationId())) {
                return GetServiceFailureResponse(404, "IntegrationId must be provided.");
            }

            GetIntegrationResponse getIntegrationResponse = OGClient.GetIntegration(model.getIntegrationId());
            UpdateIntegrationRequest updateIntegrationRequest = new UpdateIntegrationRequest();

            updateIntegrationRequest.setEnabled(getIntegrationResponse.getData()
                                                                      .isEnabled());
            updateIntegrationRequest.setId(getIntegrationResponse.getData()
                                                                 .getId());
            updateIntegrationRequest.setOwnerTeam(getIntegrationResponse.getData()
                                                                        .getOwnerTeam());
            updateIntegrationRequest.setName(getIntegrationResponse.getData()
                                                                   .getName());
            updateIntegrationRequest.setType(getIntegrationResponse.getData()
                                                                   .getType());
            updateIntegrationRequest.setIgnoreRespondersFromPayload(getIntegrationResponse.getData()
                                                                                          .isIgnoreRespondersFromPayload());
            updateIntegrationRequest.setIgnoreTeamsFromPayload(getIntegrationResponse.getData()
                                                                                     .isIgnoreTeamsFromPayload());
            updateIntegrationRequest.setRespondersPropertyList(getIntegrationResponse.getData()
                                                                                     .getRespondersPropertyList());
            updateIntegrationRequest.setSuppressNotifications(getIntegrationResponse.getData()
                                                                                    .isSuppressNotifications());

            updateIntegrationRequest.setId(model.getIntegrationId());

            if (model.getOwnerTeamId() != null) {
                OwnerTeam ownerTeam = new OwnerTeam();
                ownerTeam.setId(model.getOwnerTeamId());
                updateIntegrationRequest.setOwnerTeam(ownerTeam);
            } else if (model.getOwnerTeamName() != null) {
                OwnerTeam ownerTeam = new OwnerTeam();
                ownerTeam.setName(model.getOwnerTeamName());
                updateIntegrationRequest.setOwnerTeam(ownerTeam);
            }
            if (model.getName() != null) {
                updateIntegrationRequest.setName(model.getName());
            }

            if (model.getIntegrationType() != null) {
                updateIntegrationRequest.setType(model.getIntegrationType());
            }

            if (model.getResponders() != null) {
                updateIntegrationRequest.setRespondersPropertyList(model.getResponders());
            }

            if (model.getEnabled() != null) {
                updateIntegrationRequest.setEnabled(model.getEnabled());
            }

            OGClient.UpdateIntegration(updateIntegrationRequest);

        } catch (OpsgenieClientException e) {
            return GetServiceFailureResponse(e.getCode(), e.getMessage());
        } catch (IOException e) {
            return GetInternalFailureResponse(e.getMessage());
        }

        logger.log("[UPDATE] " + model.getIntegrationId());
        return ProgressEvent.<ResourceModel, CallbackContext>builder()
                .resourceModel(model)
                .status(OperationStatus.SUCCESS)
                .build();
    }
}
