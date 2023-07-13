package com.atlassian.opsgenie.schedule;

import software.amazon.cloudformation.proxy.*;
import com.atlassian.opsgenie.schedule.client.OpsgenieClient;
import com.atlassian.opsgenie.schedule.client.OpsgenieClientException;
import com.atlassian.opsgenie.schedule.model.*;

import java.io.IOException;

public class UpdateHandler extends BaseHandler<CallbackContext> {

    @Override
    public ProgressEvent<ResourceModel, CallbackContext> handleRequest(
            final AmazonWebServicesClientProxy proxy,
            final ResourceHandlerRequest<ResourceModel> request,
            final CallbackContext callbackContext,
            final Logger logger) {

        final ResourceModel model = request.getDesiredResourceState();

        OpsgenieClient OGClient = new OpsgenieClient(model.getOpsgenieApiEndpoint(), model.getOpsgenieApiKey());
        UpdateScheduleRequest req = new UpdateScheduleRequest();

        if (model.getOwnerTeamId() != null) {
            OwnerTeam ownerTeam = new OwnerTeam();
            ownerTeam.setId(model.getOwnerTeamId());
            req.setOwnerTeam(ownerTeam);
        } else if (model.getOwnerTeamName() != null) {
            OwnerTeam ownerTeam = new OwnerTeam();
            ownerTeam.setName(model.getOwnerTeamName());
            req.setOwnerTeam(ownerTeam);
        }
        req.setName(model.getName());
        req.setDescription(model.getDescription());
        req.setTimezone(model.getTimezone());
        req.setId(model.getScheduleId());
        req.setEnabled(model.getEnabled());

        try {
            OGClient.UpdateSchedule(req);

        } catch (OpsgenieClientException e) {
            logger.log(e.getMessage()+e.getCode());
            HandlerErrorCode errorCode = HandlerErrorCode.GeneralServiceException;
            if (e.getCode() == 429) {
                errorCode = HandlerErrorCode.Throttling;
            }
            if (e.getCode() == 404 || e.getCode() == 422 || e.getCode() == 403) {
                errorCode = HandlerErrorCode.NotFound;
            }

            if (e.getCode() == 409 ) {
                errorCode = HandlerErrorCode.ResourceConflict;
            }

            return ProgressEvent.<ResourceModel, CallbackContext>builder()
                    .errorCode(errorCode)
                    .message(e.getMessage())
                    .status(OperationStatus.FAILED)
                    .build();
        } catch (IOException e) {
            logger.log(e.getMessage());
            return ProgressEvent.<ResourceModel, CallbackContext>builder()
                    .message(e.getMessage())
                    .errorCode(HandlerErrorCode.InternalFailure)
                    .status(OperationStatus.FAILED)
                    .build();
        }

        logger.log("[UPDATE] " + model.getScheduleId());
        return ProgressEvent.<ResourceModel, CallbackContext>builder()
                .resourceModel(model)
                .status(OperationStatus.SUCCESS)
                .build();
    }
}
