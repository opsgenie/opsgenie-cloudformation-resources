package com.atlassian.opsgenie.team;

import com.atlassian.opsgenie.team.client.OpsgenieClient;
import com.atlassian.opsgenie.team.client.OpsgenieClientException;
import com.atlassian.opsgenie.team.model.MemberModel;
import com.atlassian.opsgenie.team.model.ReadTeamResponse;
import com.atlassian.opsgenie.team.model.UpdateTeamRequest;
import com.atlassian.opsgenie.team.model.UserModel;
import org.apache.commons.lang3.StringUtils;
import software.amazon.cloudformation.proxy.*;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static com.atlassian.opsgenie.team.Helper.*;

public class UpdateHandler extends BaseHandler<CallbackContext, TypeConfigurationModel> {

    @Override
    public ProgressEvent<ResourceModel, CallbackContext> handleRequest(AmazonWebServicesClientProxy proxy, ResourceHandlerRequest<ResourceModel> request, CallbackContext callbackContext,
                                                                       Logger logger, TypeConfigurationModel typeConfiguration) {

        final ResourceModel model = request.getDesiredResourceState();
        try {
            OpsgenieClient OGClient = CreateOGClient(typeConfiguration);

            List<MemberModel> members = model.getMembers()
                                             .stream()
                                             .map(member -> MemberModel.builder()
                                                                       .user(UserModel.builder()
                                                                                      .id(member.getUserId())
                                                                                      .build())
                                                                       .role(member.getRole())
                                                                       .build())
                                             .collect(Collectors.toList());


            if (StringUtils.isEmpty(model.getTeamId())) {
                return GetServiceFailureResponse(404, "TeamId must be provided.");
            }

            OGClient.UpdateTeam(model.getTeamId(), UpdateTeamRequest.builder()
                                                                    .name(model.getName())
                                                                    .description(model.getDescription())
                                                                    .members(members)
                                                                    .build());

            ReadTeamResponse readTeamResponse = OGClient.ReadTeam(model.getTeamId());

            model.setTeamId(readTeamResponse.getTeamDataModel()
                                            .getId());
            model.setName(readTeamResponse.getTeamDataModel()
                                          .getName());
            model.setDescription(readTeamResponse.getTeamDataModel()
                                                 .getDescription());

            if (readTeamResponse.getTeamDataModel()
                                .getMembers() != null) {
                List<Member> membersTemp = readTeamResponse.getTeamDataModel()
                                                           .getMembers()
                                                           .stream()
                                                           .map(memberModel -> Member.builder()
                                                                                     .userId(memberModel.getUser()
                                                                                                        .getId())
                                                                                     .role(memberModel.getRole())
                                                                                     .build())
                                                           .collect(Collectors.toList());

                model.setMembers(membersTemp);
            }

        } catch (OpsgenieClientException e) {
            return GetServiceFailureResponse(e.getCode(), e.getMessage());
        } catch (IOException e) {
            return GetInternalFailureResponse(e.getMessage());
        }

        logger.log("[UPDATE] " + model.getTeamId());
        return ProgressEvent.<ResourceModel, CallbackContext>builder()
                .resourceModel(model)
                .status(OperationStatus.SUCCESS)
                .build();
    }
}
