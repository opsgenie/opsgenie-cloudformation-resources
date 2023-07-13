package com.atlassian.opsgenie.schedule;

import software.amazon.cloudformation.proxy.*;
import com.atlassian.opsgenie.schedule.client.OpsgenieClient;
import com.atlassian.opsgenie.schedule.client.OpsgenieClientException;
import com.atlassian.opsgenie.schedule.model.CreateScheduleRequest;
import com.atlassian.opsgenie.schedule.model.CreateScheduleResponse;
import com.atlassian.opsgenie.schedule.model.OwnerTeam;

import java.io.IOException;
import java.util.Collections;

public class CreateHandler extends BaseHandler<CallbackContext> {

    @Override
    public ProgressEvent<ResourceModel, CallbackContext> handleRequest(
            final AmazonWebServicesClientProxy proxy,
            final ResourceHandlerRequest<ResourceModel> request,
            final CallbackContext callbackContext,
            final Logger logger) {

        final ResourceModel model = request.getDesiredResourceState();
        OpsgenieClient OGClient = new OpsgenieClient(model.getOpsgenieApiEndpoint(), model.getOpsgenieApiKey());
        CreateScheduleRequest req = new CreateScheduleRequest();

        req.setName(model.getName());
        req.setDescription(model.getDescription());
        req.setTimezone(model.getTimezone());

        if (model.getEnabled() == null) {
            model.setEnabled(true);
        }

        if (model.getOwnerTeamId() != null) {
            OwnerTeam ownerTeam = new OwnerTeam();
            ownerTeam.setId(model.getOwnerTeamId());
            req.setOwnerTeam(ownerTeam);
        } else if (model.getOwnerTeamName() != null) {
            OwnerTeam ownerTeam = new OwnerTeam();
            ownerTeam.setName(model.getOwnerTeamName());
            ownerTeam.setId(null);
            req.setOwnerTeam(ownerTeam);
        }

        req.setEnabled(model.getEnabled());

        try {
            logger.log("[CREATE] Request data: " + req.toString());
            if(model.getScheduleId()!=null && !model.getScheduleId().equals("")){
                throw new OpsgenieClientException("Invalid request",400);
            }
            CreateScheduleResponse resp = OGClient.CreateSchedule(req);
        } catch (OpsgenieClientException e) {
            logger.log(e.getMessage());
            HandlerErrorCode errorCode = HandlerErrorCode.GeneralServiceException;
            if (e.getCode() == 422) {
                errorCode = HandlerErrorCode.AlreadyExists;
            }
            if (e.getCode() == 429) {
                errorCode = HandlerErrorCode.Throttling;
            }
            if (e.getCode() == 400) {
                errorCode = HandlerErrorCode.InvalidRequest;
            }
            return ProgressEvent.<ResourceModel, CallbackContext>builder()
                    .errorCode(errorCode)
                    .message(e.getMessage())
                    .status(OperationStatus.FAILED)
                    .build();
        } catch (IOException e) {
            logger.log(e.getMessage());
            return ProgressEvent.<ResourceModel, CallbackContext>builder()
                    .errorCode(HandlerErrorCode.InternalFailure)
                    .message(e.getMessage())
                    .status(OperationStatus.FAILED)
                    .build();
        }

        logger.log("[CREATE] " + model.getScheduleId());
        return ProgressEvent.
                <ResourceModel, CallbackContext>builder()
                .resourceModel(model)
                .status(OperationStatus.SUCCESS)
                .build();
    }
}
