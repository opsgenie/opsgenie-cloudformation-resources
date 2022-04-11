package com.atlassian.opsgenie.team;

import com.atlassian.opsgenie.team.client.OpsgenieClient;
import com.atlassian.opsgenie.team.client.OpsgenieClientException;
import org.apache.commons.lang3.StringUtils;
import software.amazon.cloudformation.proxy.*;

import java.io.IOException;

import static com.atlassian.opsgenie.team.Helper.*;

public class DeleteHandler extends BaseHandler<CallbackContext, TypeConfigurationModel> {

    @Override
    public ProgressEvent<ResourceModel, CallbackContext> handleRequest(AmazonWebServicesClientProxy proxy, ResourceHandlerRequest<ResourceModel> request, CallbackContext callbackContext,
                                                                       Logger logger, TypeConfigurationModel typeConfiguration) {

        final ResourceModel model = request.getDesiredResourceState();
        try {
            OpsgenieClient OGClient = CreateOGClient(typeConfiguration);

            if (StringUtils.isEmpty(model.getTeamId())) {
                return GetServiceFailureResponse(404, "TeamId must be provided.");
            }

            OGClient.DeleteTeam(model.getTeamId());
        } catch (OpsgenieClientException e) {
            return GetServiceFailureResponse(e.getCode(), e.getMessage());
        } catch (IOException e) {
            return GetInternalFailureResponse(e.getMessage());
        }

        logger.log("[DELETE] " + model.getTeamId());
        return ProgressEvent.<ResourceModel, CallbackContext>builder()
                .status(OperationStatus.SUCCESS)
                .build();
    }
}
