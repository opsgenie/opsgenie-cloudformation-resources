package com.atlassian.opsgenie.integration;

import com.atlassian.opsgenie.integration.client.OpsgenieClient;
import com.atlassian.opsgenie.integration.client.OpsgenieClientException;
import org.apache.commons.lang3.StringUtils;
import software.amazon.cloudformation.proxy.*;

import java.io.IOException;

import static com.atlassian.opsgenie.integration.Helper.*;

public class DeleteHandler extends BaseHandler<CallbackContext, TypeConfigurationModel> {

    @Override
    public ProgressEvent<ResourceModel, CallbackContext> handleRequest(AmazonWebServicesClientProxy proxy, ResourceHandlerRequest<ResourceModel> request, CallbackContext callbackContext,
                                                                       Logger logger, TypeConfigurationModel typeConfiguration) {

        final ResourceModel model = request.getDesiredResourceState();
        try {
            OpsgenieClient OGClient = CreateOGClient(typeConfiguration);
            if (StringUtils.isEmpty(model.getIntegrationId())) {
                return GetServiceFailureResponse(404, "IntegrationId must be provided.");
            }

            OGClient.DeleteIntegration(model.getIntegrationId());

        } catch (OpsgenieClientException e) {
            return GetServiceFailureResponse(e.getCode(), e.getMessage());
        } catch (IOException e) {
            return GetInternalFailureResponse(e.getMessage());
        }

        logger.log("[DELETE] " + model.getIntegrationId());
        return ProgressEvent.<ResourceModel, CallbackContext>builder()
                            .status(OperationStatus.SUCCESS)
                            .build();
    }
}
