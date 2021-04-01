package com.atlassian.opsgenie.user;

import software.amazon.cloudformation.proxy.*;
import com.atlassian.opsgenie.user.client.OpsgenieClient;
import com.atlassian.opsgenie.user.client.OpsgenieClientException;
import com.atlassian.opsgenie.user.model.AddUserRequest;
import com.atlassian.opsgenie.user.model.AddUserResponse;
import com.atlassian.opsgenie.user.model.UserRole;

import java.io.IOException;

public class CreateHandler extends BaseHandler<CallbackContext> {


    @Override
    public ProgressEvent<ResourceModel, CallbackContext> handleRequest(
            final AmazonWebServicesClientProxy proxy,
            final ResourceHandlerRequest<ResourceModel> request,
            final CallbackContext callbackContext,
            final Logger logger) {

        final ResourceModel model = request.getDesiredResourceState();
        OpsgenieClient OGClient = new OpsgenieClient(model.getOpsgenieApiEndpoint(), model.getOpsgenieApiKey());

        UserRole tmp = new UserRole();
        tmp.setName(model.getRole());

        AddUserRequest usr = new AddUserRequest();
        usr.setFullname(model.getFullName());
        usr.setRole(tmp);
        usr.setUsername(model.getUsername());
        //todo add other fields in future
        try {
            logger.log("Sending request for creating user: " + usr.toString());
            AddUserResponse resp = OGClient.AddUser(usr);
            model.setUserId(resp.getDataModel().getId());
            logger.log("ResourceModel: " + model.toString());
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
                    .resourceModel(model)
                    .status(OperationStatus.FAILED)
                    .build();
        } catch (IOException e) {
            return ProgressEvent.<ResourceModel, CallbackContext>builder()
                    .errorCode(HandlerErrorCode.InternalFailure)
                    .resourceModel(model)
                    .status(OperationStatus.FAILED)
                    .build();
        }

        logger.log("[CREATE] " + model.getUserId());
        return ProgressEvent.<ResourceModel, CallbackContext>builder()
                .resourceModel(model)
                .status(OperationStatus.SUCCESS)
                .build();
    }
}
