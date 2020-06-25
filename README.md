# Opsgenie Cloudformation Integration


## Important Update

*As for the June 24, we updated the SDK and our models.*

That means if you try to update resources please be careful if rollback is enabled. 
New version(v1.1) users will not affected of this issue

Current version : *v1.1*

Old version: *v1.0*

## Supported Resources


*Link to documentation*
 
- Opsgenie User

  
- Opsgenie Team


- Opsgenie Integration



## Installation

### Install from S3


- Opsgenie User resource
```
aws cloudformation register-type \
--region us-west-2 \
--type-name "Atlassian::Opsgenie::User" \
--schema-handler-package "s3://opsgeniedownloads/cloudformation/atlassian-opsgenie-user-v1.1.zip" \
--type RESOURCE
```

- Opsgenie Team resource
```
aws cloudformation register-type \
--region us-west-2 \
--type-name "Atlassian::Opsgenie::Team" \
--schema-handler-package "s3://opsgeniedownloads/cloudformation/atlassian-opsgenie-team-v1.1.zip" \
--type RESOURCE
```


- Opsgenie Integration resource
```
aws cloudformation register-type \
--region us-west-2 \
--type-name "Atlassian::Opsgenie::Integration" \
--schema-handler-package "s3://opsgeniedownloads/cloudformation/atlassian-opsgenie-integration-v1.1.zip" \
--type RESOURCE
```


### Install using cfn-cli

- clone this repo

- navigate to desired resource (such as /opsgenie_user)

- run `cfn-cli submit` command 
