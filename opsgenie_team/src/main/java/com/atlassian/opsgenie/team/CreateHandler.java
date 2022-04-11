package com.atlassian.opsgenie.team;

import com.atlassian.opsgenie.team.client.OpsgenieClient;
import com.atlassian.opsgenie.team.client.OpsgenieClientException;
import com.atlassian.opsgenie.team.model.CreateTeamRequest;
import com.atlassian.opsgenie.team.model.CreateTeamResponse;
import com.atlassian.opsgenie.team.model.MemberModel;
import com.atlassian.opsgenie.team.model.UserModel;
import software.amazon.cloudformation.proxy.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.atlassian.opsgenie.team.Helper.*;

public class CreateHandler extends BaseHandler<CallbackContext, TypeConfigurationModel> {

    @Override
    public ProgressEvent<ResourceModel, CallbackContext> handleRequest(AmazonWebServicesClientProxy proxy, ResourceHandlerRequest<ResourceModel> request, CallbackContext callbackContext,
                                                                       Logger logger, TypeConfigurationModel typeConfiguration) {

        final ResourceModel model = request.getDesiredResourceState();
        try {
            OpsgenieClient OGClient = CreateOGClient(typeConfiguration);

            List<MemberModel> members = new ArrayList<>();

            if (model.getMembers() != null) {
                for (Member member : model.getMembers()) {
                    if (member.getRole() == null) {
                        member.setRole("user");
                    }
                }
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

            CreateTeamRequest createTeamRequest = CreateTeamRequest.builder()
                                                                   .name(model.getName())
                                                                   .description(model.getDescription())
                                                                   .members(members)
                                                                   .build();

            CreateTeamResponse createTeamResponse = OGClient.CreateTeam(createTeamRequest);
            model.setTeamId(createTeamResponse.getCreateTeamResponseData()
                                              .getId());
        } catch (OpsgenieClientException e) {
            return GetServiceFailureResponse(e.getCode(), e.getMessage());
        } catch (IOException e) {
            return GetInternalFailureResponse(e.getMessage());
        }

        logger.log("[CREATE] " + model.getTeamId());
        return ProgressEvent.<ResourceModel, CallbackContext>builder()
                .resourceModel(model)
                .status(OperationStatus.SUCCESS)
                .build();
    }
}
