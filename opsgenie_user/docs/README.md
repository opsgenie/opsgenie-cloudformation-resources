# Atlassian::Opsgenie::User

Opsgenie User

## Syntax

To declare this entity in your AWS CloudFormation template, use the following syntax:

### JSON

<pre>
{
    "Type" : "Atlassian::Opsgenie::User",
    "Properties" : {
        "<a href="#userid" title="UserId">UserId</a>" : <i>String</i>,
        "<a href="#opsgenieapiendpoint" title="OpsgenieApiEndpoint">OpsgenieApiEndpoint</a>" : <i>String</i>,
        "<a href="#opsgenieapikey" title="OpsgenieApiKey">OpsgenieApiKey</a>" : <i>String</i>,
        "<a href="#username" title="Username">Username</a>" : <i>String</i>,
        "<a href="#fullname" title="FullName">FullName</a>" : <i>String</i>,
        "<a href="#role" title="Role">Role</a>" : <i>String</i>
    }
}
</pre>

### YAML

<pre>
Type: Atlassian::Opsgenie::User
Properties:
    <a href="#userid" title="UserId">UserId</a>: <i>String</i>
    <a href="#opsgenieapiendpoint" title="OpsgenieApiEndpoint">OpsgenieApiEndpoint</a>: <i>String</i>
    <a href="#opsgenieapikey" title="OpsgenieApiKey">OpsgenieApiKey</a>: <i>String</i>
    <a href="#username" title="Username">Username</a>: <i>String</i>
    <a href="#fullname" title="FullName">FullName</a>: <i>String</i>
    <a href="#role" title="Role">Role</a>: <i>String</i>
</pre>

## Properties

#### UserId

_Required_: No

_Type_: String

_Update requires_: [Replacement](https://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/using-cfn-updating-stacks-update-behaviors.html#update-replacement)

#### OpsgenieApiEndpoint

_Required_: Yes

_Type_: String

_Minimum_: <code>1</code>

_Pattern_: <code>^https://api(\.eu|\.sandbox|)\.opsgenie\.com$</code>

_Update requires_: [No interruption](https://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/using-cfn-updating-stacks-update-behaviors.html#update-no-interrupt)

#### OpsgenieApiKey

_Required_: Yes

_Type_: String

_Pattern_: <code>[a-f0-9]{8}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{12}</code>

_Update requires_: [No interruption](https://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/using-cfn-updating-stacks-update-behaviors.html#update-no-interrupt)

#### Username

Opsgenie Username the mail address of the user

_Required_: Yes

_Type_: String

_Minimum_: <code>1</code>

_Pattern_: <code>^[a-z0-9._%+-]+@[a-z0-9]+\.[a-z]{2,6}$</code>

_Update requires_: [Replacement](https://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/using-cfn-updating-stacks-update-behaviors.html#update-replacement)

#### FullName

User full name

_Required_: Yes

_Type_: String

_Minimum_: <code>1</code>

_Pattern_: <code>^[a-zA-Z0-9- _.]+$</code>

_Update requires_: [Replacement](https://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/using-cfn-updating-stacks-update-behaviors.html#update-replacement)

#### Role

User role, default is User

_Required_: Yes

_Type_: String

_Minimum_: <code>1</code>

_Update requires_: [No interruption](https://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/using-cfn-updating-stacks-update-behaviors.html#update-no-interrupt)

## Return Values

### Ref

When you pass the logical ID of this resource to the intrinsic `Ref` function, Ref returns the Username.
