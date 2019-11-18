package com.atlassian.opsgenie.integration;

import software.amazon.cloudformation.proxy.*;
import com.atlassian.opsgenie.integration.client.OpsgenieClient;
import com.atlassian.opsgenie.integration.client.OpsgenieClientException;
import com.atlassian.opsgenie.integration.model.*;

import java.io.IOException;

public class UpdateHandler extends BaseHandler<CallbackContext> {

    @Override
    public ProgressEvent<ResourceModel, CallbackContext> handleRequest(
            final AmazonWebServicesClientProxy proxy,
            final ResourceHandlerRequest<ResourceModel> request,
            final CallbackContext callbackContext,
            final Logger logger) {

        final ResourceModel model = request.getDesiredResourceState();

        OpsgenieClient OGClient = new OpsgenieClient(model.getOpsgenieApiEndpoint(), model.getOpsgenieApiKey());
        UpdateIntegrationRequest req = new UpdateIntegrationRequest();

        model.setIntegrationApiKey(request.getPreviousResourceState().getIntegrationApiKey());
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
        req.setType(model.getIntegrationType());
        req.setRespondersPropertyList(model.getResponders());
        req.setId(model.getId());
        req.setEnabled(model.getEnabled());

        try {
            OGClient.UpdateIntegration(req);

        } catch (OpsgenieClientException e) {
            logger.log(e.getMessage());
            HandlerErrorCode errorCode = HandlerErrorCode.GeneralServiceException;
            if (e.getCode() == 429) {
                errorCode = HandlerErrorCode.Throttling;
            }
            if (e.getCode() == 404) {
                errorCode = HandlerErrorCode.NotFound;
            }

            if (e.getCode() == 409 || e.getCode() == 422) {
                errorCode = HandlerErrorCode.ResourceConflict;
            }

            return ProgressEvent.<ResourceModel, CallbackContext>builder()
                    .errorCode(errorCode)
                    .status(OperationStatus.FAILED)
                    .build();
        } catch (IOException e) {
            logger.log(e.getMessage());
            return ProgressEvent.<ResourceModel, CallbackContext>builder()
                    .message(e.getMessage())
                    .errorCode(HandlerErrorCode.InternalFailure)
                    .status(OperationStatus.FAILED)
                    .build();
        }
        return ProgressEvent.<ResourceModel, CallbackContext>builder()
                .resourceModel(model)
                .status(OperationStatus.SUCCESS)
                .build();
    }
}
