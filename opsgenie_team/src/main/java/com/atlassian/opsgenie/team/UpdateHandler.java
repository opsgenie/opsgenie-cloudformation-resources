package com.atlassian.opsgenie.team;

import software.amazon.cloudformation.proxy.*;
import com.atlassian.opsgenie.team.client.OpsgenieClient;
import com.atlassian.opsgenie.team.client.OpsgenieClientException;
import com.atlassian.opsgenie.team.model.*;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class UpdateHandler extends BaseHandler<CallbackContext> {

    @Override
    public ProgressEvent<ResourceModel, CallbackContext> handleRequest(
            final AmazonWebServicesClientProxy proxy,
            final ResourceHandlerRequest<ResourceModel> request,
            final CallbackContext callbackContext,
            final Logger logger) {

        final ResourceModel previousModel = request.getPreviousResourceState();
        final ResourceModel model = request.getDesiredResourceState();


        // TODO : put your code here

        OpsgenieClient ogClient = new OpsgenieClient(model.getOpsgenieApiKey(), model.getOpsgenieApiEndpoint());

        List<MemberModel> members = model.getMembers()
                .stream()
                .map(member -> MemberModel.builder()
                        .user(UserModel.builder()
                                .id(member.getUserId())
                                .build())
                        .role(member.getRole())
                        .build())
                .collect(Collectors.toList());

        try {
            UpdateTeamResponse updateTeamResponse = ogClient.UpdateTeam(model.getId(), UpdateTeamRequest.builder()
                    .name(model.getName())
                    .description(model.getDescription())
                    .members(members)
                    .build());

            ReadTeamResponse readTeamResponse = ogClient.ReadTeam(updateTeamResponse.getTeamDataModel().getId());

            model.setId(readTeamResponse.getTeamDataModel().getId());
            model.setName(readTeamResponse.getTeamDataModel().getName());
            model.setDescription(readTeamResponse.getTeamDataModel().getDescription());

            if(readTeamResponse.getTeamDataModel().getMembers() != null){
                List<Member> membersTemp = readTeamResponse.getTeamDataModel().getMembers().stream()
                        .map(memberModel -> Member.builder()
                                .userId(memberModel.getUser().getId())
                                .role(memberModel.getRole())
                                .build())
                        .collect(Collectors.toList());

                model.setMembers(membersTemp);
            }

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


        return ProgressEvent.<ResourceModel, CallbackContext>builder()
                .resourceModel(model)
                .status(OperationStatus.SUCCESS)
                .build();
    }
}
