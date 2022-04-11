package com.atlassian.opsgenie.user;

import com.atlassian.opsgenie.user.client.OpsgenieClient;
import com.atlassian.opsgenie.user.client.OpsgenieClientException;
import com.atlassian.opsgenie.user.model.UpdateUserRequest;
import com.atlassian.opsgenie.user.model.UserRolerRequest;
import org.apache.commons.lang3.StringUtils;
import software.amazon.cloudformation.proxy.*;

import java.io.IOException;

import static com.atlassian.opsgenie.user.Helper.*;

public class UpdateHandler extends BaseHandler<CallbackContext, TypeConfigurationModel> {

    @Override
    public ProgressEvent<ResourceModel, CallbackContext> handleRequest(AmazonWebServicesClientProxy proxy, ResourceHandlerRequest<ResourceModel> request, CallbackContext callbackContext,
                                                                       Logger logger, TypeConfigurationModel typeConfiguration) {

        final ResourceModel model = request.getDesiredResourceState();
        try {
            OpsgenieClient OGClient = CreateOGClient(typeConfiguration);

            UserRolerRequest userRolerRequest = new UserRolerRequest();
            UpdateUserRequest updateUserRequest = new UpdateUserRequest();
            userRolerRequest.setName(model.getRole());
            updateUserRequest.setFullname(model.getFullName());
            updateUserRequest.setRole(userRolerRequest);
            updateUserRequest.setUsername(model.getUsername());

            String id;
            if (StringUtils.isNotEmpty(model.getUserId())) {
                id = model.getUserId();
            } else if (StringUtils.isNotEmpty(model.getUsername())) {
                id = model.getUsername();
            } else {
                return GetServiceFailureResponse(404, "UserId or UserName must be provided.");
            }
            OGClient.UpdateUser(id, updateUserRequest);
        } catch (OpsgenieClientException e) {
            return GetServiceFailureResponse(e.getCode(), e.getMessage());
        } catch (IOException e) {
            return GetInternalFailureResponse(e.getMessage());
        }

        logger.log("[UPDATE] " + model.getUserId());
        return ProgressEvent.<ResourceModel, CallbackContext>builder()
                .resourceModel(model)
                .status(OperationStatus.SUCCESS)
                .build();
    }
}
