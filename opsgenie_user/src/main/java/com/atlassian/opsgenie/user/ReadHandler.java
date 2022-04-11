package com.atlassian.opsgenie.user;

import com.atlassian.opsgenie.user.client.OpsgenieClient;
import com.atlassian.opsgenie.user.client.OpsgenieClientException;
import com.atlassian.opsgenie.user.model.GetUserResponse;
import org.apache.commons.lang3.StringUtils;
import software.amazon.cloudformation.proxy.*;

import java.io.IOException;

import static com.atlassian.opsgenie.user.Helper.*;

public class ReadHandler extends BaseHandler<CallbackContext, TypeConfigurationModel> {

    @Override
    public ProgressEvent<ResourceModel, CallbackContext> handleRequest(AmazonWebServicesClientProxy proxy, ResourceHandlerRequest<ResourceModel> request, CallbackContext callbackContext,
                                                                       Logger logger, TypeConfigurationModel typeConfiguration) {

        final ResourceModel model = request.getDesiredResourceState();
        try {
            OpsgenieClient OGClient = CreateOGClient(typeConfiguration);
            String id;
            if (StringUtils.isNotEmpty(model.getUserId())) {
                id = model.getUserId();
            } else if (StringUtils.isNotEmpty(model.getUsername())) {
                id = model.getUsername();
            } else {
                return GetServiceFailureResponse(404, "UserId or UserName must be provided.");
            }
            GetUserResponse getUserResponse = OGClient.GetUser(id);
            model.setFullName(getUserResponse.getDataModel()
                                             .getFullName());
            model.setUsername(getUserResponse.getDataModel()
                                             .getUsername());
            model.setRole(getUserResponse.getDataModel()
                                         .getRole()
                                         .getName());
            model.setUserId(getUserResponse.getDataModel()
                                           .getId());
        } catch (OpsgenieClientException e) {
            return GetServiceFailureResponse(e.getCode(), e.getMessage());
        } catch (IOException e) {
            return GetInternalFailureResponse(e.getMessage());
        }

        logger.log("[READ] " + model.getUserId());
        return ProgressEvent.<ResourceModel, CallbackContext>builder()
                .resourceModel(model)
                .status(OperationStatus.SUCCESS)
                .build();
    }
}
