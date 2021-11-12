# Opsgenie Schedule

## Atlassian::Opsgenie::Schedule

Allows us to create schedule entities in Opsgenie via Amazon Cloudformation.<br>
To the team, you can associate schedule upon creation. <br>

## Fields

| Property            	| Description                                                                                     | Required 	| Limit                                 	|
|---------------------	|-------------------------------------------------------------------------------------------------|----------	|---------------------------------------	|
| OpsgenieApiKey      	| Your Opsgenie provided API Key                                     	                          | Required 	|                                       	|
| OpsgenieApiEndpoint 	| Endpoint of API according to your preferred environment            	                          | Required 	| api.eu.opsgenie.com, api.opsgenie.com 	|
| Name                	| Name of the schedule                                                                            | Required 	| 100 chars                             	|
| Description         	| The description of team                                                                      	  | Optional 	| 10000 chars                           	|
| Timezone         	    | The timezone of schedule                                            	                          | Optional 	| 10000 chars                           	|
| Enabled               | This parameter is for specifying whether the schedule will be enabled or not. Defaults to true  | Optional 	| Boolean value defaults to true.           |
| OwnerTeamId           | The identifier of the team associated with the integration.                                     | Required 	|                                          	|
| OwnerTeamName         | The name of the team associated with the integration.                                           | Optional 	|                                          	|                                                                                                                  	|
