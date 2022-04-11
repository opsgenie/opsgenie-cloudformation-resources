package com.atlassian.opsgenie.user;

import com.atlassian.opsgenie.user.client.OpsgenieClient;
import com.atlassian.opsgenie.user.client.OpsgenieClientException;
import com.atlassian.opsgenie.user.model.AddUserRequest;
import com.atlassian.opsgenie.user.model.AddUserResponse;
import com.atlassian.opsgenie.user.model.UserRole;
import software.amazon.cloudformation.proxy.*;

import java.io.IOException;

import static com.atlassian.opsgenie.user.Helper.*;

public class CreateHandler extends BaseHandler<CallbackContext, TypeConfigurationModel> {

    @Override
    public ProgressEvent<ResourceModel, CallbackContext> handleRequest(AmazonWebServicesClientProxy proxy, ResourceHandlerRequest<ResourceModel> request, CallbackContext callbackContext,
                                                                       Logger logger, TypeConfigurationModel typeConfiguration) {

        final ResourceModel model = request.getDesiredResourceState();
        try {
            OpsgenieClient OGClient = CreateOGClient(typeConfiguration);
            UserRole userRole = new UserRole();
            userRole.setName(model.getRole());

            AddUserRequest addUserRequest = new AddUserRequest();
            addUserRequest.setFullname(model.getFullName());
            addUserRequest.setRole(userRole);
            addUserRequest.setUsername(model.getUsername());

            AddUserResponse addUserResponse = OGClient.AddUser(addUserRequest);
            model.setUserId(addUserResponse.getDataModel()
                                           .getId());
        } catch (OpsgenieClientException e) {
            return GetServiceFailureResponse(e.getCode(), e.getMessage());
        } catch (IOException e) {
            return GetInternalFailureResponse(e.getMessage());
        }

        logger.log("[CREATE] " + model.getUserId());
        return ProgressEvent.<ResourceModel, CallbackContext>builder()
                            .resourceModel(model)
                            .status(OperationStatus.SUCCESS)
                            .build();
    }
}
