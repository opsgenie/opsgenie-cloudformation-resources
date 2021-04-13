# Atlassian::Opsgenie::Team

Opsgenie Team resource schema

## Syntax

To declare this entity in your AWS CloudFormation template, use the following syntax:

### JSON

<pre>
{
    "Type" : "Atlassian::Opsgenie::Team",
    "Properties" : {
        "<a href="#teamid" title="TeamId">TeamId</a>" : <i>String</i>,
        "<a href="#name" title="Name">Name</a>" : <i>String</i>,
        "<a href="#description" title="Description">Description</a>" : <i>String</i>,
        "<a href="#members" title="Members">Members</a>" : <i>[ <a href="member.md">Member</a>, ... ]</i>,
        "<a href="#opsgenieapikey" title="OpsgenieApiKey">OpsgenieApiKey</a>" : <i>String</i>,
        "<a href="#opsgenieapiendpoint" title="OpsgenieApiEndpoint">OpsgenieApiEndpoint</a>" : <i>String</i>
    }
}
</pre>

### YAML

<pre>
Type: Atlassian::Opsgenie::Team
Properties:
    <a href="#teamid" title="TeamId">TeamId</a>: <i>String</i>
    <a href="#name" title="Name">Name</a>: <i>String</i>
    <a href="#description" title="Description">Description</a>: <i>String</i>
    <a href="#members" title="Members">Members</a>: <i>
      - <a href="member.md">Member</a></i>
    <a href="#opsgenieapikey" title="OpsgenieApiKey">OpsgenieApiKey</a>: <i>String</i>
    <a href="#opsgenieapiendpoint" title="OpsgenieApiEndpoint">OpsgenieApiEndpoint</a>: <i>String</i>
</pre>

## Properties

#### TeamId

Team id

_Required_: No

_Type_: String

_Pattern_: <code>[a-f0-9]{8}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{12}</code>

_Update requires_: [Replacement](https://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/using-cfn-updating-stacks-update-behaviors.html#update-replacement)

#### Name

Team name

_Required_: Yes

_Type_: String

_Pattern_: <code>^[a-zA-Z_.]+$</code>

_Update requires_: [No interruption](https://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/using-cfn-updating-stacks-update-behaviors.html#update-no-interrupt)

#### Description

Team description

_Required_: No

_Type_: String

_Minimum_: <code>1</code>

_Maximum_: <code>100</code>

_Pattern_: <code>^[a-zA-Z0-9-_.]+$</code>

_Update requires_: [No interruption](https://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/using-cfn-updating-stacks-update-behaviors.html#update-no-interrupt)

#### Members

Array of members

_Required_: No

_Type_: List of <a href="member.md">Member</a>

_Update requires_: [No interruption](https://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/using-cfn-updating-stacks-update-behaviors.html#update-no-interrupt)

#### OpsgenieApiKey

Api Key

_Required_: No

_Type_: String

_Pattern_: <code>[a-f0-9]{8}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{12}</code>

_Update requires_: [No interruption](https://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/using-cfn-updating-stacks-update-behaviors.html#update-no-interrupt)

#### OpsgenieApiEndpoint

Api endpoint

_Required_: No

_Type_: String

_Pattern_: <code>^https://api(\.eu|\.sandbox|)\.opsgenie\.com$</code>

_Update requires_: [No interruption](https://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/using-cfn-updating-stacks-update-behaviors.html#update-no-interrupt)

## Return Values

### Ref

When you pass the logical ID of this resource to the intrinsic `Ref` function, Ref returns the TeamId.
