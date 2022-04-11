package com.atlassian.opsgenie.user;

import com.atlassian.opsgenie.user.client.OpsgenieClient;
import com.atlassian.opsgenie.user.client.OpsgenieClientException;
import org.apache.commons.lang3.StringUtils;
import software.amazon.cloudformation.proxy.*;

import java.io.IOException;

import static com.atlassian.opsgenie.user.Helper.*;

public class DeleteHandler extends BaseHandler<CallbackContext, TypeConfigurationModel> {

    @Override
    public ProgressEvent<ResourceModel, CallbackContext> handleRequest(AmazonWebServicesClientProxy proxy, ResourceHandlerRequest<ResourceModel> request, CallbackContext callbackContext,
                                                                       Logger logger, TypeConfigurationModel typeConfiguration) {

        final ResourceModel model = request.getDesiredResourceState();
        try {
            OpsgenieClient OGClient = CreateOGClient(typeConfiguration);
            Delay(typeConfiguration);
            if (StringUtils.isNotEmpty(model.getUserId())) {
                OGClient.DeleteUser(model.getUserId());
            } else if (StringUtils.isNotEmpty(model.getUsername())) {
                OGClient.DeleteUser(model.getUsername());
            } else {
                return GetServiceFailureResponse(404, "UserId or UserName must be provided.");
            }

        } catch (OpsgenieClientException e) {
            return GetServiceFailureResponse(e.getCode(), e.getMessage());
        } catch (IOException e) {
            return GetInternalFailureResponse(e.getMessage());
        } catch (InterruptedException e) {
            return GetInternalFailureResponse(e.getMessage());
        }
        logger.log("[DELETE] " + model.getUserId());
        return ProgressEvent.<ResourceModel, CallbackContext>builder()
                .status(OperationStatus.SUCCESS)
                .build();
    }
}
