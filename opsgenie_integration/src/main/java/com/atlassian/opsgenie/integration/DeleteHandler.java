package com.atlassian.opsgenie.integration;

import software.amazon.cloudformation.proxy.*;
import com.atlassian.opsgenie.integration.client.OpsgenieClient;
import com.atlassian.opsgenie.integration.client.OpsgenieClientException;

import java.io.IOException;

public class DeleteHandler extends BaseHandler<CallbackContext> {

    @Override
    public ProgressEvent<ResourceModel, CallbackContext> handleRequest(
        final AmazonWebServicesClientProxy proxy,
        final ResourceHandlerRequest<ResourceModel> request,
        final CallbackContext callbackContext,
        final Logger logger) {

        final ResourceModel model = request.getDesiredResourceState();
        OpsgenieClient OGClient = new OpsgenieClient(model.getOpsgenieApiEndpoint(),model.getOpsgenieApiKey());

        try {
            OGClient.DeleteIntegration(model.getId());
        } catch (IOException e) {
            logger.log(e.getMessage());
            return ProgressEvent.<ResourceModel, CallbackContext>builder()
                    .resourceModel(model)
                    .status(OperationStatus.FAILED)
                    .build();
        } catch (OpsgenieClientException e) {
            logger.log(e.getMessage());
            HandlerErrorCode errorCode = HandlerErrorCode.GeneralServiceException;
            if (e.getCode() == 404) {
                errorCode = HandlerErrorCode.NotFound;
            }
            return ProgressEvent.<ResourceModel, CallbackContext>builder()
                    .errorCode(errorCode)
                    .status(OperationStatus.FAILED)
                    .build();
        }

        return ProgressEvent.<ResourceModel, CallbackContext>builder()
            .resourceModel(model)
            .status(OperationStatus.SUCCESS)
            .build();
    }
}
