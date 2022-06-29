#
#!/bin/bash

read -r -d '' PYSCRIPT << ENDPY
import json
import os
from os.path import expanduser
def generateTypeConfigurationJson():
  data = {
      'OpsgenieCredentials' : {
          'OpsgenieApiEndpoint' : os.getenv('OPSGENIE_API_ENDPOINT','https://api.opsgenie.com'),
          'OpsgenieApiKey' :  os.getenv('OPSGENIE_API_KEY',"")
      },
      'ContractTestSettings': {
          'DelayEnabled' : os.getenv('DELAY_ENABLED','true').lower() in ('true', '1', 'yes'),
          'DelaySeconds' : int(os.getenv('DELAY_SECONDS','10')),
      }
  }
  with open('typeConfiguration.json', 'w+') as f:
      json.dump(data, f, indent=4)

generateTypeConfigurationJson()
ENDPY

# execute python and fail on error

echo "$PYSCRIPT" |python3 - || exit 1
