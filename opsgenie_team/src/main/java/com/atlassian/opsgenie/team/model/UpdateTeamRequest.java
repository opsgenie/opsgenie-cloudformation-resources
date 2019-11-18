package com.atlassian.opsgenie.team.model;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;


@Builder
@Data
@EqualsAndHashCode(callSuper = false)
public class UpdateTeamRequest extends OGRequest {
    private String name;
    private String description;
    private List<MemberModel> members;
}
