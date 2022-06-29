package com.atlassian.opsgenie.user;

import com.atlassian.opsgenie.user.client.OpsgenieClient;
import com.atlassian.opsgenie.user.client.OpsgenieClientException;
import com.atlassian.opsgenie.user.model.DataModel;
import com.atlassian.opsgenie.user.model.ListUserResponse;
import software.amazon.cloudformation.proxy.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.atlassian.opsgenie.user.Helper.*;

public class ListHandler extends BaseHandler<CallbackContext, TypeConfigurationModel> {

    @Override
    public ProgressEvent<ResourceModel, CallbackContext> handleRequest(AmazonWebServicesClientProxy proxy, ResourceHandlerRequest<ResourceModel> request, CallbackContext callbackContext,
                                                                       Logger logger, TypeConfigurationModel typeConfiguration) {

        final List<ResourceModel> models = new ArrayList<>();
        final ResourceModel desiredResourceState = request.getDesiredResourceState();
        try {
            OpsgenieClient OGClient = CreateOGClient(typeConfiguration);
            Delay(typeConfiguration);
            ListUserResponse listUserResponse = OGClient.ListUsers();
            for (DataModel dataModel : listUserResponse.getDataModel()) {
                if (dataModel.getId().equals(desiredResourceState.getUserId())){
                    ResourceModel resourceModel = new ResourceModel();
                    resourceModel.setRole(dataModel.getRole()
                            .getName());
                    resourceModel.setUsername(dataModel.getUsername());
                    resourceModel.setFullName(dataModel.getFullName());
                    resourceModel.setUserId(dataModel.getId());
                    models.add(resourceModel);
                }
            }
        } catch (OpsgenieClientException e) {
            return GetServiceFailureResponse(e.getCode(), e.getMessage());
        } catch (IOException e) {
            return GetInternalFailureResponse(e.getMessage());
        } catch (InterruptedException e) {
            return GetInternalFailureResponse(e.getMessage());
        }

        return ProgressEvent.<ResourceModel, CallbackContext>builder()
                .resourceModels(models)
                .status(OperationStatus.SUCCESS)
                .build();
    }
}
