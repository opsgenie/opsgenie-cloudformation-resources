package com.atlassian.opsgenie.integration;

import software.amazon.cloudformation.proxy.*;
import com.atlassian.opsgenie.integration.client.OpsgenieClient;
import com.atlassian.opsgenie.integration.client.OpsgenieClientException;
import com.atlassian.opsgenie.integration.model.CreateIntegrationRequest;
import com.atlassian.opsgenie.integration.model.CreateIntegrationResponse;
import com.atlassian.opsgenie.integration.model.OwnerTeam;

import java.io.IOException;
import java.util.Collections;

public class CreateHandler extends BaseHandler<CallbackContext> {

    @Override
    public ProgressEvent<ResourceModel, CallbackContext> handleRequest(
            final AmazonWebServicesClientProxy proxy,
            final ResourceHandlerRequest<ResourceModel> request,
            final CallbackContext callbackContext,
            final Logger logger) {

        final ResourceModel model = request.getDesiredResourceState();
        OpsgenieClient OGClient = new OpsgenieClient(model.getOpsgenieApiEndpoint(), model.getOpsgenieApiKey());
        CreateIntegrationRequest req = new CreateIntegrationRequest();

        if (model.getAllowConfigurationAccess() == null) {
            model.setAllowConfigurationAccess(false);
        }
        req.setAllowConfigurationAccess(model.getAllowConfigurationAccess());

        if (model.getAllowDeleteAccess() == null) {
            model.setAllowDeleteAccess(true);
        }
        req.setAllowDeleteAccess(model.getAllowDeleteAccess());

        if (model.getAllowReadAccess() == null) {
            model.setAllowReadAccess(true);
        }
        req.setAllowReadAccess(model.getAllowReadAccess());

        if (model.getAllowWriteAccess() == null) {
            model.setAllowWriteAccess(true);
        }
        req.setAllowWriteAccess(model.getAllowWriteAccess());

        if (model.getOwnerTeamId() != null) {
            OwnerTeam ownerTeam = new OwnerTeam();
            ownerTeam.setId(model.getOwnerTeamId());
            req.setOwnerTeam(ownerTeam);
        } else if (model.getOwnerTeamName() != null) {
            OwnerTeam ownerTeam = new OwnerTeam();
            ownerTeam.setName(model.getOwnerTeamName());
            req.setOwnerTeam(ownerTeam);
        }
        req.setName(model.getName());

        if (model.getResponders() != null) {
            if (model.getResponders().size() > 0) {
                req.setRespondersPropertyList(model.getResponders());
            }
        }
        if (model.getEnabled() == null) {
            model.setEnabled(true);
        }

        req.setType(model.getIntegrationType());

        req.setEnabled(model.getEnabled());

        try {
            logger.log("[CREATE] Request data: " + req.toString());
            CreateIntegrationResponse resp = OGClient.CreateIntegration(req);
            model.setIntegrationId(resp.getData().getId());
            model.setIntegrationApiKey(resp.getData().getApiKey());
        } catch (OpsgenieClientException e) {
            logger.log(e.getMessage());
            HandlerErrorCode errorCode = HandlerErrorCode.GeneralServiceException;
            if (e.getCode() == 422) {
                errorCode = HandlerErrorCode.AlreadyExists;
            }
            if (e.getCode() == 429) {
                errorCode = HandlerErrorCode.Throttling;
            }
            return ProgressEvent.<ResourceModel, CallbackContext>builder()
                    .errorCode(errorCode)
                    .status(OperationStatus.FAILED)
                    .build();
        } catch (IOException e) {
            logger.log(e.getMessage());
            return ProgressEvent.<ResourceModel, CallbackContext>builder()
                    .errorCode(HandlerErrorCode.InternalFailure)
                    .status(OperationStatus.FAILED)
                    .build();
        }

        logger.log("[CREATE] " + model.getIntegrationId());
        return ProgressEvent.
                <ResourceModel, CallbackContext>builder()
                .resourceModel(model)
                .status(OperationStatus.SUCCESS)
                .build();
    }
}
