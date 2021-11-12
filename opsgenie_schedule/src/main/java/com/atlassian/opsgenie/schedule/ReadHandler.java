package com.atlassian.opsgenie.schedule;

import software.amazon.cloudformation.proxy.*;
import com.atlassian.opsgenie.schedule.client.OpsgenieClient;
import com.atlassian.opsgenie.schedule.client.OpsgenieClientException;
import com.atlassian.opsgenie.schedule.model.GetScheduleResponse;

import java.io.IOException;

public class ReadHandler extends BaseHandler<CallbackContext> {

    @Override
    public ProgressEvent<ResourceModel, CallbackContext> handleRequest(
            final AmazonWebServicesClientProxy proxy,
            final ResourceHandlerRequest<ResourceModel> request,
            final CallbackContext callbackContext,
            final Logger logger) {

        final ResourceModel model = request.getDesiredResourceState();

        OpsgenieClient OGClient = new OpsgenieClient(model.getOpsgenieApiEndpoint(), model.getOpsgenieApiKey());
        try {
            GetScheduleResponse resp = OGClient.GetSchedule(model.getScheduleId());
            model.setName(resp.getData().getName());
            model.setDescription(resp.getData().getDescription());
            model.setTimezone(resp.getData().getTimezone());
            model.setEnabled(resp.getData().isEnabled());
            if (resp.getData().getOwnerTeam() != null) {
                model.setOwnerTeamId(resp.getData().getOwnerTeam().getId());
                model.setOwnerTeamName(resp.getData().getOwnerTeam().getName());
            }


        } catch (OpsgenieClientException e) {
            HandlerErrorCode errorCode = HandlerErrorCode.GeneralServiceException;
            logger.log(e.getMessage());
            e.printStackTrace();
            if (e.getCode() == 429) {
                errorCode = HandlerErrorCode.Throttling;
            }
            if (e.getCode() == 404) {
                errorCode = HandlerErrorCode.NotFound;
            }

            return ProgressEvent.<ResourceModel, CallbackContext>builder()
                    .message(e.getMessage())
                    .errorCode(errorCode)
                    .status(OperationStatus.FAILED)
                    .build();
        } catch (IOException e) {
            e.printStackTrace();
            return ProgressEvent.<ResourceModel, CallbackContext>builder()
                    .errorCode(HandlerErrorCode.InternalFailure)
                    .status(OperationStatus.FAILED)
                    .build();
        }

        logger.log("[READ] " + model.getScheduleId());
        return ProgressEvent.<ResourceModel, CallbackContext>builder()
                .resourceModel(model)
                .status(OperationStatus.SUCCESS)
                .build();
    }
}
