package com.atlassian.opsgenie.user;

import software.amazon.cloudformation.proxy.*;
import com.atlassian.opsgenie.user.client.OpsgenieClient;
import com.atlassian.opsgenie.user.client.OpsgenieClientException;
import com.atlassian.opsgenie.user.model.*;

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
        UserRolerRequest tmp = new UserRolerRequest();
        UpdateUserRequest usr = new UpdateUserRequest();
        tmp.setName(model.getRole());
        usr.setFullname(model.getFullName());
        usr.setRole(tmp);
        usr.setUsername(model.getUsername());
        try {
            UpdateUserResponse resp = OGClient.UpdateUser(request.getDesiredResourceState().getUserId(), usr);
            model.setUserId(request.getDesiredResourceState().getUserId());

        } catch (OpsgenieClientException e) {
            logger.log(e.getMessage());
            HandlerErrorCode errorCode = HandlerErrorCode.GeneralServiceException;
            if (e.getCode() == 409) {
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

        logger.log("[UPDATE] " + model.getUserId());
        return ProgressEvent.<ResourceModel, CallbackContext>builder()
                .resourceModel(model)
                .status(OperationStatus.SUCCESS)
                .build();
    }
}
