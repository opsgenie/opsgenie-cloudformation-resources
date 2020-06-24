package com.atlassian.opsgenie.integration;

import software.amazon.cloudformation.proxy.*;
import com.atlassian.opsgenie.integration.model.GetIntegrationResponse;
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
        ResourceModel model = new ResourceModel();
        try {
            GetIntegrationResponse resp = OGClient.GetIntegration(request.getDesiredResourceState().getIntegrationId());
            model.setAllowWriteAccess(resp.getData().isAllowWriteAccess());
            model.setAllowReadAccess(resp.getData().isAllowReadAccess());
            model.setAllowDeleteAccess(resp.getData().isAllowDeleteAccess());
            model.setAllowConfigurationAccess(resp.getData().isAllowConfigurationAccess());
            model.setEnabled(resp.getData().isEnabled());
            model.setIntegrationType(resp.getData().getType());
            model.setName(resp.getData().getName());
            model.setIntegrationId(request.getDesiredResourceState().getIntegrationId());
            model.setOpsgenieApiKey(request.getDesiredResourceState().getOpsgenieApiKey());
            model.setOpsgenieApiEndpoint(request.getDesiredResourceState().getOpsgenieApiEndpoint());
            model.setIntegrationApiKey(request.getDesiredResourceState().getIntegrationApiKey());
            model.setResponders(resp.getData().getRespondersPropertyList());
            if (resp.getData().getOwnerTeam() != null) {
                model.setOwnerTeamId(resp.getData().getOwnerTeam().getId());
                model.setOwnerTeamName(resp.getData().getOwnerTeam().getName());
            }
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
        models.add(model);

        for (ResourceModel _model: models) {
            logger.log("[CREATE] " + _model.getIntegrationId());
        }

        return ProgressEvent.<ResourceModel, CallbackContext>builder()
                .resourceModels(models)
                .status(OperationStatus.SUCCESS)
                .build();
    }
}
