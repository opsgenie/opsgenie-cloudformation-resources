package com.atlassian.opsgenie.schedule;

import software.amazon.cloudformation.proxy.*;
import com.atlassian.opsgenie.schedule.model.ListScheduleResponse;
import com.atlassian.opsgenie.schedule.client.OpsgenieClient;
import com.atlassian.opsgenie.schedule.client.OpsgenieClientException;

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
            ListScheduleResponse resp = OGClient.ListSchedules();
            logger.log(resp.toString());
            resp.getData().stream().forEach(model -> {
                ResourceModel resourceModel = new ResourceModel();
                resourceModel.setName(model.getName());
                resourceModel.setDescription(model.getDescription());
                resourceModel.setTimezone(model.getTimezone());
                resourceModel.setEnabled(model.isEnabled());
                if (model.getOwnerTeam() != null) {
                    resourceModel.setOwnerTeamId(model.getOwnerTeam().getId());
                    resourceModel.setOwnerTeamName(model.getOwnerTeam().getName());
                }
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
