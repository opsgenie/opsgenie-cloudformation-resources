package com.atlassian.opsgenie.team;

import software.amazon.cloudformation.proxy.*;
import com.atlassian.opsgenie.team.client.OpsgenieClient;
import com.atlassian.opsgenie.team.client.OpsgenieClientException;
import com.atlassian.opsgenie.team.model.DeleteTeamResponse;

import java.io.IOException;

public class DeleteHandler extends BaseHandler<CallbackContext> {

    @Override
    public ProgressEvent<ResourceModel, CallbackContext> handleRequest(
            final AmazonWebServicesClientProxy proxy,
            final ResourceHandlerRequest<ResourceModel> request,
            final CallbackContext callbackContext,
            final Logger logger) {

        final ResourceModel model = request.getDesiredResourceState();

        // TODO : put your code here

        OpsgenieClient ogClient = new OpsgenieClient(model.getOpsgenieApiKey(), model.getOpsgenieApiEndpoint());

        try {
            DeleteTeamResponse deleteTeamResponse = ogClient.DeleteTeam(model.getTeamId());

        } catch (OpsgenieClientException e) {
            if(e.getCode() == 404){
                logger.log(e.getMessage());
                e.printStackTrace();
                return ProgressEvent.<ResourceModel, CallbackContext>builder()
                        .errorCode(HandlerErrorCode.NotFound)
                        .status(OperationStatus.FAILED)
                        .build();
            }
        } catch (IOException e ){
            logger.log("Exception");
            logger.log(e.getMessage());
            e.printStackTrace();
            return ProgressEvent.<ResourceModel, CallbackContext>builder()
                    .errorCode(HandlerErrorCode.InternalFailure)
                    .status(OperationStatus.FAILED)
                    .build();
        }

        logger.log("[DELETE] " + model.getTeamId());
        return ProgressEvent.<ResourceModel, CallbackContext>builder()
                .resourceModel(model)
                .status(OperationStatus.SUCCESS)
                .build();
    }
}
