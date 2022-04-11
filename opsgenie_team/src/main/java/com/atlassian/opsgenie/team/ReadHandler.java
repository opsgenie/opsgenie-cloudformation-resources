package com.atlassian.opsgenie.team;

import com.atlassian.opsgenie.team.client.OpsgenieClient;
import com.atlassian.opsgenie.team.client.OpsgenieClientException;
import com.atlassian.opsgenie.team.model.ReadTeamResponse;
import org.apache.commons.lang3.StringUtils;
import software.amazon.cloudformation.proxy.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.atlassian.opsgenie.team.Helper.*;

public class ReadHandler extends BaseHandler<CallbackContext, TypeConfigurationModel> {

    @Override
    public ProgressEvent<ResourceModel, CallbackContext> handleRequest(AmazonWebServicesClientProxy proxy, ResourceHandlerRequest<ResourceModel> request, CallbackContext callbackContext,
                                                                       Logger logger, TypeConfigurationModel typeConfiguration) {

        final ResourceModel model = request.getDesiredResourceState();
        try {
            OpsgenieClient OGClient = CreateOGClient(typeConfiguration);

            if (StringUtils.isEmpty(model.getTeamId())) {
                return GetServiceFailureResponse(404, "TeamId must be provided.");
            }

            ReadTeamResponse readTeamResponse = OGClient.ReadTeam(model.getTeamId());

            model.setName(readTeamResponse.getTeamDataModel()
                                          .getName());
            model.setDescription(readTeamResponse.getTeamDataModel()
                                                 .getDescription());

            if (readTeamResponse.getTeamDataModel()
                                .getMembers() != null) {

                List<Member> members = readTeamResponse.getTeamDataModel()
                                                       .getMembers()
                                                       .stream()
                                                       .map(memberModel -> Member.builder()
                                                                                 .userId(memberModel.getUser()
                                                                                                    .getId())
                                                                                 .role(memberModel.getRole())
                                                                                 .build())
                                                       .collect(Collectors.toList());
                model.setMembers(members);
            } else {
                model.setMembers(Arrays.asList());
            }


        } catch (OpsgenieClientException e) {
            return GetServiceFailureResponse(e.getCode(), e.getMessage());
        } catch (IOException e) {
            return GetInternalFailureResponse(e.getMessage());
        }

        logger.log("[READ] " + model.getTeamId());
        return ProgressEvent.<ResourceModel, CallbackContext>builder()
            .resourceModel(model)
            .status(OperationStatus.SUCCESS)
            .build();
    }
}
