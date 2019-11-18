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

        // TODO : put your code here



        OpsgenieClient ogClient = new OpsgenieClient(request.getDesiredResourceState().getOpsgenieApiKey(), request.getDesiredResourceState().getOpsgenieApiEndpoint());

        try {
            ListTeamResponse listTeamResponse = ogClient.ListTeam();
            for(TeamDataModel teamDataModel: listTeamResponse.getTeamDataModel()){
                ResourceModel.ResourceModelBuilder resourceModelBuilder = ResourceModel.builder();

                ReadTeamResponse readTeamResponse = ogClient.ReadTeam(teamDataModel.getId());

                resourceModelBuilder.id(readTeamResponse.getTeamDataModel().getId());
                resourceModelBuilder.name(readTeamResponse.getTeamDataModel().getName());
                resourceModelBuilder.description(readTeamResponse.getTeamDataModel().getDescription());

                if(readTeamResponse.getTeamDataModel().getMembers() != null){
                    List<Member> members = readTeamResponse.getTeamDataModel().getMembers().stream()
                            .map(memberModel -> Member.builder()
                                    .userId(memberModel.getUser().getId())
                                    .role(memberModel.getRole())
                                    .build())
                            .collect(Collectors.toList());

                    resourceModelBuilder.members(members);
                }

                resourceModelBuilder.opsgenieApiKey(request.getDesiredResourceState().getOpsgenieApiKey());
                resourceModelBuilder.opsgenieApiEndpoint(request.getDesiredResourceState().getOpsgenieApiEndpoint());

                /*models.add(ResourceModel.builder()
                        .id(teamDataModel.getId())
                        .name(teamDataModel.getName())
                        .description(teamDataModel.getDescription())
                        *//*.members(teamDataModel.getMembers().stream()
                                .map(memberModel -> Member.builder()
                                        .userId(memberModel.getUser().getId())
                                        .role(memberModel.getRole())
                                        .build())
                                .collect(Collectors.toList()))*//*
                        .apiKey(request.getDesiredResourceState().getApiKey())
                        .apiEndpoint(request.getDesiredResourceState().getApiEndpoint())
                        .build());*/
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
