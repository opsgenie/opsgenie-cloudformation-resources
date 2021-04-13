package com.atlassian.opsgenie.team;

import software.amazon.cloudformation.proxy.*;
import com.atlassian.opsgenie.team.client.OpsgenieClient;
import com.atlassian.opsgenie.team.client.OpsgenieClientException;
import com.atlassian.opsgenie.team.model.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ListHandler extends BaseHandler<CallbackContext> {

    @Override
    public ProgressEvent<ResourceModel, CallbackContext> handleRequest(
        final AmazonWebServicesClientProxy proxy,
        final ResourceHandlerRequest<ResourceModel> request,
        final CallbackContext callbackContext,
        final Logger logger) {

        final List<ResourceModel> models = new ArrayList<>();

        OpsgenieClient ogClient = new OpsgenieClient(request.getDesiredResourceState().getOpsgenieApiKey(), request.getDesiredResourceState().getOpsgenieApiEndpoint());

        try {
            ListTeamResponse listTeamResponse = ogClient.ListTeam();
            for(TeamDataModel teamDataModel: listTeamResponse.getTeamDataModel()){
                ResourceModel.ResourceModelBuilder resourceModelBuilder = ResourceModel.builder();
                resourceModelBuilder.teamId(teamDataModel.getId());
                resourceModelBuilder.name(teamDataModel.getName());
                resourceModelBuilder.description(teamDataModel.getDescription());


                resourceModelBuilder.opsgenieApiKey(request.getDesiredResourceState().getOpsgenieApiKey());
                resourceModelBuilder.opsgenieApiEndpoint(request.getDesiredResourceState().getOpsgenieApiEndpoint());

                models.add(resourceModelBuilder.build());
            }
        } catch (IOException | OpsgenieClientException e) {
            logger.log("Exception");
            logger.log(e.getMessage());
            e.printStackTrace();
            return ProgressEvent.<ResourceModel, CallbackContext>builder()
                    .errorCode(HandlerErrorCode.InternalFailure)
                    .status(OperationStatus.FAILED)
                    .build();
        }
        return ProgressEvent.<ResourceModel, CallbackContext>builder()
            .resourceModels(models)
            .status(OperationStatus.SUCCESS)
            .build();
    }
}
