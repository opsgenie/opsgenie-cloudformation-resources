package com.atlassian.opsgenie.integration;

import com.atlassian.opsgenie.integration.client.OpsgenieClient;
import com.atlassian.opsgenie.integration.client.OpsgenieClientException;
import com.atlassian.opsgenie.integration.model.ListIntegrationResponse;
import software.amazon.cloudformation.proxy.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.atlassian.opsgenie.integration.Helper.*;

public class ListHandler extends BaseHandler<CallbackContext, TypeConfigurationModel> {

    @Override
    public ProgressEvent<ResourceModel, CallbackContext> handleRequest(AmazonWebServicesClientProxy proxy, ResourceHandlerRequest<ResourceModel> request, CallbackContext callbackContext,
                                                                       Logger logger, TypeConfigurationModel typeConfiguration) {

        final List<ResourceModel> models = new ArrayList<>();
        final ResourceModel model = request.getDesiredResourceState();
        try {
            OpsgenieClient OGClient = CreateOGClient(typeConfiguration);

            ListIntegrationResponse listIntegrationResponse = OGClient.ListIntegrations();
            listIntegrationResponse.getData()
                                   .stream()
                                   .forEach(dataModel -> {
                                       ResourceModel resourceModel = new ResourceModel();
                                       resourceModel.setName(dataModel.getName());
                                       resourceModel.setIntegrationType(dataModel.getType());
                                       resourceModel.setIntegrationId(dataModel.getId());
                                       models.add(resourceModel);
                                   });
        } catch (OpsgenieClientException e) {
            return GetServiceFailureResponse(e.getCode(), e.getMessage());
        } catch (IOException e) {
            return GetInternalFailureResponse(e.getMessage());
        }

        return ProgressEvent.<ResourceModel, CallbackContext>builder()
                .resourceModels(models)
                .status(OperationStatus.SUCCESS)
                .build();
    }
}
