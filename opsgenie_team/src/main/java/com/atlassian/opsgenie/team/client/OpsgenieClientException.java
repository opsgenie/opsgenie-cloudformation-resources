package com.atlassian.opsgenie.team.client;

public class OpsgenieClientException extends Exception {
    static final long serialVersionUID = -3387516993124229948L;

    /**
     * Represents error code returning form OpsGenie service.
     */
    private int code;

    /**
     * Constructs an OpsGenieClientException with specified error message and error code.
     */
    public OpsgenieClientException(String message, int code) {
        super(message);
        this.code = code;
    }

    /**
     * Retrieves error code returning form OpsGenie service.
     */
    public int getCode() {
        return code;
    }
}
