# Opsgenie Team

## Atlassian::Opsgenie::Team

Allows us to create user entities in Opsgenie via Amazon Cloudformation.<br>
To the team, you can associate members upon creation. <br>
The Team resource supports all the fields available in Opsgenie's Team entity.

## Fields

| Property            	| Description                                                        	| Required 	| Limit                                 	|
|---------------------	|--------------------------------------------------------------------	|----------	|---------------------------------------	|
| OpsgenieApiKey      	| Your Opsgenie provided API Key                                     	| Required 	|                                       	|
| OpsgenieApiEndpoint 	| Endpoint of API according to your preferred environment            	| Required 	| api.eu.opsgenie.com, api.opsgenie.com 	|
| Name                	| Name of the team                                                   	| Required 	| 100 chars                             	|
| Description         	| The description of team                                            	| Optional 	| 10000 chars                           	|
| Members             	| The users which will be added to team, and optionally their roles. 	| Optional 	|                                       	|


- Member property

| Property            	| Description                                                        	| Required 	| Limit                                 	|
|---------------------	|--------------------------------------------------------------------	|----------	|---------------------------------------	|
| UserId              	| User identifier                                                    	| Required 	| 100 chars                             	|
| Role                	| Role of user                                                       	| Required 	| Owner, Admin, User, Custom roles                           	|
