package com.atlassian.opsgenie.user;

import software.amazon.cloudformation.proxy.*;
import com.atlassian.opsgenie.user.client.OpsgenieClient;
import com.atlassian.opsgenie.user.client.OpsgenieClientException;

import java.io.IOException;

public class DeleteHandler extends BaseHandler<CallbackContext> {

    @Override
    public ProgressEvent<ResourceModel, CallbackContext> handleRequest(
            final AmazonWebServicesClientProxy proxy,
            final ResourceHandlerRequest<ResourceModel> request,
            final CallbackContext callbackContext,
            final Logger logger) {
        ResourceModel model = request.getDesiredResourceState();
        OpsgenieClient OGClient = new OpsgenieClient(model.getOpsgenieApiEndpoint(), model.getOpsgenieApiKey());
        try {
            if (model.getUserId() != null && !model.getUserId().equals("")) {
                OGClient.DeleteUser(model.getUserId());
            } else {
                OGClient.DeleteUser(model.getUsername());
            }
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
        } catch (IOException e) {
            return ProgressEvent.<ResourceModel, CallbackContext>builder()
                    .errorCode(HandlerErrorCode.InternalFailure)
                    .status(OperationStatus.FAILED)
                    .build();
        }

        logger.log("[DELETE] " + model.getUserId());
        return ProgressEvent.<ResourceModel, CallbackContext>builder()
                .status(OperationStatus.SUCCESS)
                .build();
    }
}
