package com.atlassian.opsgenie.integration;

import software.amazon.cloudformation.proxy.*;
import com.atlassian.opsgenie.integration.client.OpsgenieClient;
import com.atlassian.opsgenie.integration.client.OpsgenieClientException;
import com.atlassian.opsgenie.integration.model.GetIntegrationResponse;

import java.io.IOException;

public class ReadHandler extends BaseHandler<CallbackContext> {

    @Override
    public ProgressEvent<ResourceModel, CallbackContext> handleRequest(
            final AmazonWebServicesClientProxy proxy,
            final ResourceHandlerRequest<ResourceModel> request,
            final CallbackContext callbackContext,
            final Logger logger) {

        final ResourceModel model = request.getDesiredResourceState();

        OpsgenieClient OGClient = new OpsgenieClient(model.getOpsgenieApiEndpoint(), model.getOpsgenieApiKey());
        try {
            GetIntegrationResponse resp = OGClient.GetIntegration(model.getIntegrationId());
            model.setAllowWriteAccess(resp.getData().isAllowWriteAccess());
            model.setAllowReadAccess(resp.getData().isAllowReadAccess());
            model.setAllowDeleteAccess(resp.getData().isAllowDeleteAccess());
            model.setAllowConfigurationAccess(resp.getData().isAllowConfigurationAccess());
            model.setEnabled(resp.getData().isEnabled());
            if (resp.getData().getOwnerTeam() != null) {
                model.setOwnerTeamId(resp.getData().getOwnerTeam().getId());
                model.setOwnerTeamName(resp.getData().getOwnerTeam().getName());
            }
            model.setResponders(resp.getData().getRespondersPropertyList());


        } catch (OpsgenieClientException e) {
            HandlerErrorCode errorCode = HandlerErrorCode.GeneralServiceException;
            logger.log(e.getMessage());
            e.printStackTrace();
            if (e.getCode() == 429) {
                errorCode = HandlerErrorCode.Throttling;
            }
            if (e.getCode() == 404) {
                errorCode = HandlerErrorCode.NotFound;
            }

            return ProgressEvent.<ResourceModel, CallbackContext>builder()
                    .message(e.getMessage())
                    .errorCode(errorCode)
                    .status(OperationStatus.FAILED)
                    .build();
        } catch (IOException e) {
            e.printStackTrace();
            return ProgressEvent.<ResourceModel, CallbackContext>builder()
                    .errorCode(HandlerErrorCode.InternalFailure)
                    .status(OperationStatus.FAILED)
                    .build();
        }

        logger.log("[READ] " + model.toString());
        logger.log("[READ] PrimaryId: " + model.getPrimaryIdentifier());

        return ProgressEvent.<ResourceModel, CallbackContext>builder()
                .resourceModel(model)
                .status(OperationStatus.SUCCESS)
                .build();
    }
}
