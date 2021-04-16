package com.atlassian.opsgenie.user;

import software.amazon.cloudformation.proxy.*;
import com.atlassian.opsgenie.user.client.OpsgenieClient;
import com.atlassian.opsgenie.user.client.OpsgenieClientException;
import com.atlassian.opsgenie.user.model.DataModel;
import com.atlassian.opsgenie.user.model.ListUserResponse;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ListHandler extends BaseHandler<CallbackContext> {

    @Override
    public ProgressEvent<ResourceModel, CallbackContext> handleRequest(
            final AmazonWebServicesClientProxy proxy,
            final ResourceHandlerRequest<ResourceModel> request,
            final CallbackContext callbackContext,
            final Logger logger) {

        final List<ResourceModel> models = new ArrayList<>();
        OpsgenieClient OGClient = new OpsgenieClient(request.getDesiredResourceState().getOpsgenieApiEndpoint(), request.getDesiredResourceState().getOpsgenieApiKey());
        try {

            ListUserResponse listUserResponse = OGClient.ListUsers();
            for (DataModel dataModel : listUserResponse.getDataModel()) {
                if (!dataModel.getId().equals(request.getDesiredResourceState().getUserId())) {
                    continue;
                }
                ResourceModel tmp = new ResourceModel();
                tmp.setRole(dataModel.getRole().getName());
                tmp.setOpsgenieApiEndpoint(request.getDesiredResourceState().getOpsgenieApiEndpoint());
                tmp.setOpsgenieApiKey(request.getDesiredResourceState().getOpsgenieApiKey());
                tmp.setUsername(dataModel.getUsername());
                tmp.setFullName(dataModel.getFullName());
                tmp.setUserId(dataModel.getId());
                models.add(tmp);
            }
        } catch (OpsgenieClientException e) {
            HandlerErrorCode errorCode = HandlerErrorCode.GeneralServiceException;
            if (e.getCode() == 404) {
                errorCode = HandlerErrorCode.NotFound;
            }
            if (e.getCode() == 429) {
                errorCode = HandlerErrorCode.Throttling;
            }
            logger.log(e.getMessage());
            return ProgressEvent.<ResourceModel, CallbackContext>builder()
                    .errorCode(errorCode)
                    .status(OperationStatus.FAILED)
                    .build();
        } catch (IOException e) {
            e.printStackTrace();
            return ProgressEvent.<ResourceModel, CallbackContext>builder()
                    .status(OperationStatus.FAILED)
                    .build();
        }

        for(ResourceModel model: models) {
            logger.log("[CREATE] " + model.getUserId());
        }

        return ProgressEvent.<ResourceModel, CallbackContext>builder()
                .resourceModels(models)
                .status(OperationStatus.SUCCESS)
                .build();
    }
}
