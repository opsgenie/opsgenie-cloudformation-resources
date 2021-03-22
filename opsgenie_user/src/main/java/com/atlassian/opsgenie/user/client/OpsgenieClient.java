package com.atlassian.opsgenie.user.client;

import com.atlassian.opsgenie.user.model.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;

import java.io.IOException;

public class OpsgenieClient {
    private String apiKey;
    private String endPoint;
    private OkHttpClient httpClient;
    private String userAgent = "opsgenie-cloudformation-user-client";

    public OpsgenieClient(String endPoint, String apiKey) {
        this.endPoint = endPoint;
        this.apiKey = apiKey;
        this.httpClient = new OkHttpClient().newBuilder().retryOnConnectionFailure(true).build();
    }

    private String execute(Request request) throws IOException, OpsgenieClientException {
        Response response = this.httpClient.newCall(request).execute();
        if (!(response.code() >= 200 && response.code() <= 299)) {
            throw new OpsgenieClientException(response.body().string(), response.code());
        }
        return response.body().string();

    }

    public AddUserResponse AddUser(AddUserRequest req) throws IOException, OpsgenieClientException {
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(req);
        RequestBody body = RequestBody.create(json, MediaType.get("application/json"));
        Request request = new Request.Builder()
                .url(endPoint + "/v2/users")
                .post(body)
                .addHeader("Authorization", "GenieKey " + this.apiKey)
                .addHeader("User-Agent",userAgent)
                .build();

        return objectMapper.readValue(execute(request), AddUserResponse.class);
    }

    public void DeleteUser(String Id) throws IOException, OpsgenieClientException {
        Request request = new Request.Builder()
                    .url(endPoint + "/v2/users/" + Id)
                .delete()
                .addHeader("Authorization", "GenieKey " + this.apiKey)
                .addHeader("User-Agent",userAgent)
                .build();
        execute(request);
    }

    public UpdateUserResponse UpdateUser(String Id, UpdateUserRequest req) throws IOException, OpsgenieClientException {
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(req);
        RequestBody body = RequestBody.create(json, MediaType.get("application/json"));
        Request request = new Request.Builder()
                .url(endPoint + "/v2/users/" + Id)
                .patch(body)
                .addHeader("Authorization", "GenieKey " + this.apiKey)
                .addHeader("User-Agent",userAgent)
                .build();
        //throw new OpsgenieClientException(Id, 404);
        return objectMapper.readValue(execute(request), UpdateUserResponse.class);

    }

    public GetUserResponse GetUser(String Id) throws IOException, OpsgenieClientException {
        ObjectMapper objectMapper = new ObjectMapper();
        Request request = new Request.Builder()
                .url(endPoint + "/v2/users/" + Id)
                .get()
                .addHeader("Authorization", "GenieKey " + this.apiKey)
                .addHeader("User-Agent",userAgent)
                .build();
        return objectMapper.readValue(execute(request), GetUserResponse.class);

    }

    public ListUserResponse ListUsers() throws IOException, OpsgenieClientException {
        ObjectMapper objectMapper = new ObjectMapper();
        Request request = new Request.Builder()
                .url(endPoint + "/v2/users/")
                .get()
                .addHeader("Authorization", "GenieKey " + this.apiKey)
                .addHeader("User-Agent",userAgent)
                .build();
        return objectMapper.readValue(execute(request), ListUserResponse.class);

    }
}
