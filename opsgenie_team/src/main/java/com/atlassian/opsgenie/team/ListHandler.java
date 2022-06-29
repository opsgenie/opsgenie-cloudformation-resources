package com.atlassian.opsgenie.team;

import com.atlassian.opsgenie.team.client.OpsgenieClient;
import com.atlassian.opsgenie.team.client.OpsgenieClientException;
import com.atlassian.opsgenie.team.model.ListTeamResponse;
import com.atlassian.opsgenie.team.model.TeamDataModel;
import software.amazon.cloudformation.proxy.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.atlassian.opsgenie.team.Helper.*;

public class ListHandler extends BaseHandler<CallbackContext, TypeConfigurationModel> {

    @Override
    public ProgressEvent<ResourceModel, CallbackContext> handleRequest(AmazonWebServicesClientProxy proxy, ResourceHandlerRequest<ResourceModel> request, CallbackContext callbackContext,
                                                                       Logger logger, TypeConfigurationModel typeConfiguration) {

        final List<ResourceModel> models = new ArrayList<>();
        final ResourceModel desiredResourceState = request.getDesiredResourceState();
        try {
            OpsgenieClient OGClient = CreateOGClient(typeConfiguration);

            ListTeamResponse listTeamResponse = OGClient.ListTeam();
            for (TeamDataModel teamDataModel : listTeamResponse.getTeamDataModel()) {
                if (teamDataModel.getId().equals(desiredResourceState.getTeamId())){
                    models.add(ResourceModel.builder()
                            .teamId(teamDataModel.getId())
                            .name(teamDataModel.getName())
                            .description(teamDataModel.getDescription())
                            .build());
                }
            }
        } catch (OpsgenieClientException e) {
            return GetServiceFailureResponse(e.getCode(), e.getMessage());
        } catch (IOException e) {
            return GetInternalFailureResponse(e.getMessage());
        }

        return ProgressEvent.<ResourceModel, CallbackContext>builder()
            .resourceModels(models)
            .status(OperationStatus.SUCCESS)
            .build();
    }
}
