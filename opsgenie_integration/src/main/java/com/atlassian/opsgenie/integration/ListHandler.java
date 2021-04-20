package com.atlassian.opsgenie.integration;

import software.amazon.cloudformation.proxy.*;
import com.atlassian.opsgenie.integration.model.ListIntegrationResponse;
import com.atlassian.opsgenie.integration.client.OpsgenieClient;
import com.atlassian.opsgenie.integration.client.OpsgenieClientException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ListHandler extends BaseHandler<CallbackContext> {

    @Override
    public ProgressEvent<ResourceModel, CallbackContext> handleRequest(
            final AmazonWebServicesClientProxy proxy,
            final ResourceHandlerRequest<ResourceModel> request,
            final CallbackContext callbackContext,
            final Logger logger) {

        final List<ResourceModel> models = new ArrayList<>();

        OpsgenieClient OGClient = new OpsgenieClient(request.getDesiredResourceState().getOpsgenieApiEndpoint(), request.getDesiredResourceState().getOpsgenieApiKey());

        try {
            ListIntegrationResponse resp = OGClient.ListIntegrations();
            logger.log(resp.toString());
            resp.getData().stream().forEach(model -> {
                ResourceModel resourceModel = new ResourceModel();
                resourceModel.setName(model.getName());
                resourceModel.setIntegrationType(model.getType());
                resourceModel.setIntegrationId(model.getId());
                resourceModel.setOpsgenieApiEndpoint(request.getDesiredResourceState().getOpsgenieApiEndpoint());
                resourceModel.setOpsgenieApiKey(request.getDesiredResourceState().getOpsgenieApiKey());
                models.add(resourceModel);
            });
        } catch (IOException e) {
            logger.log(e.getMessage());
            e.printStackTrace();
            return ProgressEvent.<ResourceModel, CallbackContext>builder()
                    .status(OperationStatus.FAILED)
                    .build();
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
        }

        return ProgressEvent.<ResourceModel, CallbackContext>builder()
                .resourceModels(models)
                .status(OperationStatus.SUCCESS)
                .build();
    }
}
