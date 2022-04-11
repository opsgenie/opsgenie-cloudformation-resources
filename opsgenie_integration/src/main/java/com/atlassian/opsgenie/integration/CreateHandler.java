package com.atlassian.opsgenie.integration;

import com.atlassian.opsgenie.integration.client.OpsgenieClient;
import com.atlassian.opsgenie.integration.client.OpsgenieClientException;
import com.atlassian.opsgenie.integration.model.CreateIntegrationRequest;
import com.atlassian.opsgenie.integration.model.CreateIntegrationResponse;
import com.atlassian.opsgenie.integration.model.OwnerTeam;
import software.amazon.cloudformation.proxy.*;

import java.io.IOException;

import static com.atlassian.opsgenie.integration.Helper.*;

public class CreateHandler extends BaseHandler<CallbackContext, TypeConfigurationModel> {

    @Override
    public ProgressEvent<ResourceModel, CallbackContext> handleRequest(AmazonWebServicesClientProxy proxy, ResourceHandlerRequest<ResourceModel> request, CallbackContext callbackContext,
                                                                       Logger logger, TypeConfigurationModel typeConfiguration) {

        final ResourceModel model = request.getDesiredResourceState();
        try {
            OpsgenieClient OGClient = CreateOGClient(typeConfiguration);

            CreateIntegrationRequest createIntegrationRequest = new CreateIntegrationRequest();

            if (model.getAllowConfigurationAccess() == null) {
                model.setAllowConfigurationAccess(false);
            }
            createIntegrationRequest.setAllowConfigurationAccess(model.getAllowConfigurationAccess());

            if (model.getAllowDeleteAccess() == null) {
                model.setAllowDeleteAccess(true);
            }
            createIntegrationRequest.setAllowDeleteAccess(model.getAllowDeleteAccess());

            if (model.getAllowReadAccess() == null) {
                model.setAllowReadAccess(true);
            }
            createIntegrationRequest.setAllowReadAccess(model.getAllowReadAccess());

            if (model.getAllowWriteAccess() == null) {
                model.setAllowWriteAccess(true);
            }
            createIntegrationRequest.setAllowWriteAccess(model.getAllowWriteAccess());

            if (model.getOwnerTeamId() != null) {
                OwnerTeam ownerTeam = new OwnerTeam();
                ownerTeam.setId(model.getOwnerTeamId());
                createIntegrationRequest.setOwnerTeam(ownerTeam);
            } else if (model.getOwnerTeamName() != null) {
                OwnerTeam ownerTeam = new OwnerTeam();
                ownerTeam.setName(model.getOwnerTeamName());
                ownerTeam.setId(null);
                createIntegrationRequest.setOwnerTeam(ownerTeam);
            }
            createIntegrationRequest.setName(model.getName());

            if (model.getResponders() != null) {
                if (model.getResponders()
                         .size() > 0) {
                    createIntegrationRequest.setRespondersPropertyList(model.getResponders());
                }
            }
            if (model.getEnabled() == null) {
                model.setEnabled(true);
            }

            createIntegrationRequest.setType(model.getIntegrationType());

            createIntegrationRequest.setEnabled(model.getEnabled());

            CreateIntegrationResponse createIntegrationResponse = OGClient.CreateIntegration(createIntegrationRequest);
            model.setIntegrationId(createIntegrationResponse.getData()
                                                            .getId());
            model.setIntegrationApiKey(createIntegrationResponse.getData()
                                                                .getApiKey());
        } catch (OpsgenieClientException e) {
            return GetServiceFailureResponse(e.getCode(), e.getMessage());
        } catch (IOException e) {
            return GetInternalFailureResponse(e.getMessage());
        }

        logger.log("[CREATE] " + model.getIntegrationId());
        return ProgressEvent.
                <ResourceModel, CallbackContext>builder()
                .resourceModel(model)
                .status(OperationStatus.SUCCESS)
                .build();
    }
}
