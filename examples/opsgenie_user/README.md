# Opsgenie User

## Atlassian::Opsgenie::User

Allows us to create user entities in Opsgenie via Amazon Cloudformation. <br>
Uluru resource supports three configurable properties for simplicity along with the required OpsgenieApiEndpoint and OpsgenieApiKey properties.

## Fields

| Field               	| Description                                             	| Required 	| Limit                                                                      	|
|---------------------	|---------------------------------------------------------	|----------	|----------------------------------------------------------------------------	|
| OpsgenieApiKey      	| Your Opsgenie provided API Key                          	| Required 	|                                                                            	|
| OpsgenieApiEndpoint 	| Endpoint of API according to your preferred environment 	| Required 	| api.eu.opsgenie.com, api.opsgenie.com 	|
| Username            	| E-mail address of the user                              	| Required 	| 100 chars                                                                  	|
| FullName            	| Name of the user                                        	| Required 	| 512 chars                                                                  	|
| Role                	| Role of user                                            	| Required 	| Owner, Admin, User, Custom roles   	|
