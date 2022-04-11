package com.atlassian.opsgenie.team.client;

import com.atlassian.opsgenie.team.model.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;

import java.io.IOException;

public class OpsgenieClient {
    private final static String USER_AGENT = "opsgenie-cloudformation-team-client";
    private String apiKey;
    private String endpoint;

    private OkHttpClient httpClient;

    public OpsgenieClient(String endpoint, String apiKey) {
        this.endpoint = endpoint;
        this.apiKey = apiKey;
        this.httpClient = new OkHttpClient();
    }

    private String execute(Request request) throws IOException, OpsgenieClientException {
        Response response = this.httpClient.newCall(request).execute();
        if (!(response.code() >= 200 && response.code() <= 299)) {
            throw new OpsgenieClientException(response.body().string(), response.code());
        }
        return response.body().string();

    }

    public CreateTeamResponse CreateTeam(OGRequest req) throws IOException, OpsgenieClientException {
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(req);
        RequestBody body = RequestBody.create(json, MediaType.get("application/json"));
        Request request = new Request.Builder()
                .url(this.endpoint + "/v2/teams")
                .post(body)
                .addHeader("Authorization", "GenieKey " + this.apiKey)
                .addHeader("User-Agent", USER_AGENT)
                .build();

        String bodyA = execute(request);

        return objectMapper.readValue(bodyA, CreateTeamResponse.class);
    }

    public DeleteTeamResponse DeleteTeam(String teamId) throws IOException, OpsgenieClientException {
        ObjectMapper objectMapper = new ObjectMapper();
        Request request = new Request.Builder()
                .url(this.endpoint + "/v2/teams/" + teamId + "?identifierType=id")
                .delete()
                .addHeader("Authorization", "GenieKey " + this.apiKey)
                .addHeader("User-Agent", USER_AGENT)
                .build();

        String bodyA = execute(request);

        return objectMapper.readValue(bodyA, DeleteTeamResponse.class);
    }

    public ListTeamResponse ListTeam() throws IOException, OpsgenieClientException {
        ObjectMapper objectMapper = new ObjectMapper();
        Request request = new Request.Builder()
                .url(this.endpoint + "/v2/teams")
                .get()
                .addHeader("Authorization", "GenieKey " + this.apiKey)
                .addHeader("User-Agent", USER_AGENT)
                .build();

        String bodyA = execute(request);

        return objectMapper.readValue(bodyA, ListTeamResponse.class);
    }

    public ReadTeamResponse ReadTeam(String teamId) throws IOException, OpsgenieClientException {
        ObjectMapper objectMapper = new ObjectMapper();
        Request request = new Request.Builder()
                .url(this.endpoint + "/v2/teams/" + teamId + "?identifierType=id")
                .get()
                .addHeader("Authorization", "GenieKey " + this.apiKey)
                .addHeader("User-Agent", USER_AGENT)
                .build();

        String bodyA = execute(request);

        return objectMapper.readValue(bodyA, ReadTeamResponse.class);
    }

    public UpdateTeamResponse UpdateTeam(String teamId, OGRequest req) throws IOException, OpsgenieClientException {
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(req);
        RequestBody body = RequestBody.create(json, MediaType.get("application/json"));
        Request request = new Request.Builder()
                .url(this.endpoint + "/v2/teams/" + teamId + "?identifierType=id")
                .patch(body)
                .addHeader("Authorization", "GenieKey " + this.apiKey)
                .addHeader("User-Agent", USER_AGENT)
                .build();

        String bodyA = execute(request);

        return objectMapper.readValue(bodyA, UpdateTeamResponse.class);
    }
}
