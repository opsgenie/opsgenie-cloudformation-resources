# Opsgenie Cloudformation Integration


The typeConfiguration needs to be set via AWS CLI or AWS Console. You will not able to set credentials in resource models.

```
aws cloudformation set-type-configuration \
--region $REGION \
--type RESOURCE \
--type-name $RESOURCE_NAME \
--configuration-alias default \
--configuration { "OpsgenieCredentials": { "OpsgenieApiEndpoint": "$API_ENDPOINT", "OpsgenieApiKey": "$API_KEY" }}
```

Note: To use your type configuration in contract tests, you will need to save your type configuration json file
in `~/.cfn-cli/typeConfiguration.json`

```
{
    "OpsgenieCredentials": {
        "OpsgenieApiEndpoint": "$API_ENDPOINT",
        "OpsgenieApiKey": "$API_KEY"
    }
}
```

https://github.com/aws-cloudformation/cloudformation-cli

## Supported Resources

*Link to documentation*

- Opsgenie User


- Opsgenie Team


- Opsgenie Integration



## Installation

### Install from S3


- Opsgenie User resource
```shell
wget https://opsgeniedownloads.s3.us-west-2.amazonaws.com/cloudformation/atlassian-opsgenie-user-v2.1.0.zip

aws s3 cp atlassian-opsgenie-user-v2.1.0.zip s3://$BUCKET_NAME

aws cloudformation register-type \
--region us-west-2 \
--type-name "Atlassian::Opsgenie::User" \
--schema-handler-package "s3://$BUCKET_NAME/atlassian-opsgenie-user-v2.1.0.zip" \
--type RESOURCE > registration_token.json

aws cloudformation describe-type-registration --registration-token $REGISTRATION_TOKEN

aws cloudformation set-type-default-version --arn $TYPE_VERSION_ARN
```

- Opsgenie Team resource
```shell
wget https://opsgeniedownloads.s3.us-west-2.amazonaws.com/cloudformation/atlassian-opsgenie-team-v2.1.0.zip

aws s3 cp atlassian-opsgenie-team-v2.1.0.zip s3://$BUCKET_NAME

aws cloudformation register-type \
--region us-west-2 \
--type-name "Atlassian::Opsgenie::Team" \
--schema-handler-package "s3://$BUCKET_NAME/atlassian-opsgenie-team-v2.1.0.zip" \
--type RESOURCE > registration_token.json

aws cloudformation describe-type-registration --registration-token $REGISTRATION_TOKEN

aws cloudformation set-type-default-version --arn $TYPE_VERSION_ARN
```


- Opsgenie Integration resource
```shell
wget https://opsgeniedownloads.s3.us-west-2.amazonaws.com/cloudformation/atlassian-opsgenie-integration-v2.1.0.zip

aws s3 cp atlassian-opsgenie-integration-v2.1.0.zip s3://$BUCKET_NAME

aws cloudformation register-type \
--region us-west-2 \
--type-name "Atlassian::Opsgenie::Integration" \
--schema-handler-package "s3://$BUCKET_NAME/atlassian-opsgenie-integration-v2.1.0.zip" \
--type RESOURCE > registration_token.json

aws cloudformation describe-type-registration --registration-token $REGISTRATION_TOKEN

aws cloudformation set-type-default-version --arn $TYPE_VERSION_ARN
```


### Install using cfn-cli

- clone this repo

- navigate to desired resource (such as /opsgenie_user)

- run `cfn-cli submit` command 

### Install using AWS Console

- navigate to Cloudformation on AWS console
- navigate to Public Extensions section
- select Third Party Publisher
- search for `Atlassian::Opsgenie::User` or the others
- activate
