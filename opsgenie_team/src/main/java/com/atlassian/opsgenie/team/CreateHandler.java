package com.atlassian.opsgenie.team;

import software.amazon.cloudformation.proxy.*;
import com.atlassian.opsgenie.team.client.OpsgenieClient;
import com.atlassian.opsgenie.team.client.OpsgenieClientException;
import com.atlassian.opsgenie.team.model.CreateTeamRequest;
import com.atlassian.opsgenie.team.model.CreateTeamResponse;
import com.atlassian.opsgenie.team.model.MemberModel;
import com.atlassian.opsgenie.team.model.UserModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CreateHandler extends BaseHandler<CallbackContext> {

    @Override
    public ProgressEvent<ResourceModel, CallbackContext> handleRequest(
            final AmazonWebServicesClientProxy proxy,
            final ResourceHandlerRequest<ResourceModel> request,
            final CallbackContext callbackContext,
            final Logger logger) {

        final ResourceModel model = request.getDesiredResourceState();

        // TODO : put your code here

        OpsgenieClient ogClient = new OpsgenieClient(model.getOpsgenieApiKey(), model.getOpsgenieApiEndpoint());

        if (model.getMembers() != null) {
            for (Member member : model.getMembers()) {
                if (member.getRole() == null) {
                    member.setRole("user");
                }
            }
        }


        List<MemberModel> members = new ArrayList<>();

        if(model.getMembers() != null){
            members = model.getMembers()
                    .stream()
                    .map(member -> MemberModel.builder()
                            .user(UserModel.builder()
                                    .id(member.getUserId())
                                    .build())
                            .role(member.getRole())
                            .build())
                    .collect(Collectors.toList());
        }

        try {
            CreateTeamResponse createTeamResponse = ogClient.CreateTeam(CreateTeamRequest.builder()
                    .name(model.getName())
                    .description(model.getDescription())
                    .members(members)
                    .build());

            model.setId(createTeamResponse.getTeamDataModel().getId());
        } catch (OpsgenieClientException e) {
            if (e.getCode() == 409) {
                return ProgressEvent.<ResourceModel, CallbackContext>builder()
                        .errorCode(HandlerErrorCode.AlreadyExists)
                        .status(OperationStatus.FAILED)
                        .build();
            }
            if (e.getCode() == 429) {
                return ProgressEvent.<ResourceModel, CallbackContext>builder()
                        .errorCode(HandlerErrorCode.Throttling)
                        .status(OperationStatus.FAILED)
                        .build();
            }

            logger.log(e.getMessage());
            e.printStackTrace();

            return ProgressEvent.<ResourceModel, CallbackContext>builder()
                    .errorCode(HandlerErrorCode.InternalFailure)
                    .status(OperationStatus.FAILED)
                    .build();
        } catch (IOException e) {
            logger.log("Exception");
            logger.log(e.getMessage());
            e.printStackTrace();
            return ProgressEvent.<ResourceModel, CallbackContext>builder()
                    .errorCode(HandlerErrorCode.InternalFailure)
                    .status(OperationStatus.FAILED)
                    .build();
        }


        return ProgressEvent.<ResourceModel, CallbackContext>builder()
                .resourceModel(model)
                .status(OperationStatus.SUCCESS)
                .build();
    }
}
