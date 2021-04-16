package com.atlassian.opsgenie.user;

import software.amazon.cloudformation.proxy.*;
import com.atlassian.opsgenie.user.client.OpsgenieClient;
import com.atlassian.opsgenie.user.client.OpsgenieClientException;
import com.atlassian.opsgenie.user.model.*;

import java.io.IOException;
import java.util.List;

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
            String id;
            if (model.getUserId() != null && !model.getUserId().equals("")) {
                logger.log(" userId: " + model.getUserId());
                id = model.getUserId();
            } else {
                logger.log(" userName: " + model.getUsername());
                id = model.getUsername();
            }
            GetUserResponse resp = OGClient.GetUser(id);
            model.setFullName(resp.getDataModel().getFullName());
            model.setUsername(resp.getDataModel().getUsername());
            model.setRole(resp.getDataModel().getRole().getName());
        } catch (OpsgenieClientException e) {
            HandlerErrorCode errorCode = HandlerErrorCode.GeneralServiceException;
            if (e.getCode() == 404) {
                errorCode = HandlerErrorCode.NotFound;
            }
            if (e.getCode() == 429) {
                errorCode = HandlerErrorCode.Throttling;
            }
            logger.log(e.getMessage());
            return ProgressEvent.<ResourceModel, CallbackContext>builder()
                    .errorCode(errorCode)
                    .status(OperationStatus.FAILED)
                    .build();
        } catch (IOException e) {
            e.printStackTrace();
            return ProgressEvent.<ResourceModel, CallbackContext>builder()
                    .status(OperationStatus.FAILED)
                    .build();
        }

        logger.log("[READ] " + model.getUserId());

        return ProgressEvent.<ResourceModel, CallbackContext>builder()
                .resourceModel(model)
                .status(OperationStatus.SUCCESS)
                .build();
    }
}
