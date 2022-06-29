package com.atlassian.opsgenie.integration;

import com.atlassian.opsgenie.integration.client.OpsgenieClient;
import com.atlassian.opsgenie.integration.client.OpsgenieClientException;
import com.atlassian.opsgenie.integration.model.GetIntegrationResponse;
import org.apache.commons.lang3.StringUtils;
import software.amazon.cloudformation.proxy.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.atlassian.opsgenie.integration.Helper.*;

public class ReadHandler extends BaseHandler<CallbackContext, TypeConfigurationModel> {

    @Override
    public ProgressEvent<ResourceModel, CallbackContext> handleRequest(AmazonWebServicesClientProxy proxy, ResourceHandlerRequest<ResourceModel> request, CallbackContext callbackContext,
                                                                       Logger logger, TypeConfigurationModel typeConfiguration) {

        final ResourceModel model = request.getDesiredResourceState();
        try {
            OpsgenieClient OGClient = CreateOGClient(typeConfiguration);
            if (StringUtils.isEmpty(model.getIntegrationId())) {
                return GetServiceFailureResponse(404, "IntegrationId must be provided.");
            }

            GetIntegrationResponse getIntegrationResponse = OGClient.GetIntegration(model.getIntegrationId());
            model.setAllowWriteAccess(getIntegrationResponse.getData()
                                                            .isAllowWriteAccess());
            model.setAllowReadAccess(getIntegrationResponse.getData()
                                                           .isAllowReadAccess());
            model.setAllowDeleteAccess(getIntegrationResponse.getData()
                                                             .isAllowDeleteAccess());
            model.setAllowConfigurationAccess(getIntegrationResponse.getData()
                                                                    .isAllowConfigurationAccess());
            model.setEnabled(getIntegrationResponse.getData()
                                                   .isEnabled());
            if (getIntegrationResponse.getData()
                                      .getOwnerTeam() != null) {
                model.setOwnerTeamId(getIntegrationResponse.getData()
                                                           .getOwnerTeam()
                                                           .getId());
                model.setOwnerTeamName(getIntegrationResponse.getData()
                                                             .getOwnerTeam()
                                                             .getName());
            }
            model.setResponders(getIntegrationResponse.getData()
                                                      .getRespondersPropertyList());

            model.setOpsgenieApiEndpoint(typeConfiguration.getOpsgenieCredentials().getOpsgenieApiEndpoint());

        } catch (OpsgenieClientException e) {
            return GetServiceFailureResponse(e.getCode(), e.getMessage());
        } catch (IOException e) {
            return GetInternalFailureResponse(e.getMessage());
        }

        logger.log("[READ] " + model.getIntegrationId());
        return ProgressEvent.<ResourceModel, CallbackContext>builder()
                .resourceModel(model)
                .status(OperationStatus.SUCCESS)
                .build();
    }
}
