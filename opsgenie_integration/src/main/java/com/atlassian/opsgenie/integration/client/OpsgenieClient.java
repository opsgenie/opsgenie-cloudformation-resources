package com.atlassian.opsgenie.integration.client;

import com.atlassian.opsgenie.integration.model.*;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;

import java.io.IOException;

public class OpsgenieClient {

    private String apiKey;
    private String endpoint;
    private OkHttpClient httpClient;
    private String userAgent = "opsgenie-cloudformation-integration-client";

    public OpsgenieClient(String endpoint, String apiKey) {
        this.apiKey = apiKey;
        this.endpoint = endpoint;
        this.httpClient = new OkHttpClient();
    }

    private String execute(Request request) throws IOException, OpsgenieClientException {
        Response response = this.httpClient.newCall(request)
                                           .execute();
        if (!(response.code() >= 200 && response.code() <= 299)) {
            throw new OpsgenieClientException(response.body()
                                                      .string(), response.code());
        }
        return response.body()
                       .string();

    }

    public CreateIntegrationResponse CreateIntegration(CreateIntegrationRequest req) throws IOException, OpsgenieClientException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String json = objectMapper.writeValueAsString(req);
        RequestBody body = RequestBody.create(json, MediaType.get("application/json"));
        Request request = new Request.Builder()
                .url(this.endpoint + "/v2/integrations")
                .post(body)
                .addHeader("Authorization", "GenieKey " + this.apiKey)
                .addHeader("User-Agent", userAgent)
                .build();

        return objectMapper.readValue(execute(request), CreateIntegrationResponse.class);
    }

    public void DeleteIntegration(String Id) throws IOException, OpsgenieClientException {
        Request request = new Request.Builder()
                .url(this.endpoint + "/v2/integrations/" + Id)
                .addHeader("Authorization", "GenieKey " + this.apiKey)
                .addHeader("User-Agent", userAgent)
                .delete()
                .build();
        execute(request);
    }

    public void UpdateIntegration(UpdateIntegrationRequest req) throws IOException, OpsgenieClientException {
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(req);
        RequestBody body = RequestBody.create(json, MediaType.get("application/json"));
        Request request = new Request.Builder()
                .url(this.endpoint + "/v2/integrations/" + req.getId())
                .put(body)
                .addHeader("Authorization", "GenieKey " + this.apiKey)
                .addHeader("User-Agent", userAgent)
                .build();
        execute(request);
    }

    public GetIntegrationResponse GetIntegration(String Id) throws IOException, OpsgenieClientException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        Request request = new Request.Builder()
                .url(this.endpoint + "/v2/integrations/" + Id)
                .get()
                .addHeader("Authorization", "GenieKey " + this.apiKey)
                .addHeader("User-Agent", userAgent)
                .build();
        return objectMapper.readValue(execute(request), GetIntegrationResponse.class);

    }

    public ListIntegrationResponse ListIntegrations() throws IOException, OpsgenieClientException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        Request request = new Request.Builder()
                .url(this.endpoint + "/v2/integrations")
                .get()
                .addHeader("Authorization", "GenieKey " + this.apiKey)
                .addHeader("User-Agent", userAgent)
                .build();
        return objectMapper.readValue(execute(request), ListIntegrationResponse.class);

    }


}
