# Opsgenie User

## Atlassian::Opsgenie::User

Allows us to create user entities in Opsgenie via Amazon Cloudformation. <br>
The resource supports three configurable properties for simplicity.

## Fields

| Field               	| Description                                             	| Required 	| Limit                                                                      	|
|---------------------	|---------------------------------------------------------	|----------	|----------------------------------------------------------------------------	|
| Username            	| E-mail address of the user                              	| Required 	| 100 chars                                                                  	|
| FullName            	| Name of the user                                        	| Required 	| 512 chars                                                                  	|
| Role                	| Role of user                                            	| Required 	| Owner, Admin, User, Custom roles   	|
