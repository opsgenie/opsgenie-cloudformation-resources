#
#!/bin/bash

read -r -d '' PYSCRIPT << ENDPY
import json
import os
def updateJsonFile():

    with open('overrides.json', 'r') as f:
        data = json.load(f)
        data["CREATE"]["/OpsgenieApiKey"]= os.environ['OPSGENIE_API_KEY']
        data["CREATE"]["/OpsgenieApiEndpoint"]= os.environ['OPSGENIE_API_ENDPOINT']
        if hasattr( data ,"UPDATE"):
          data["UPDATE"]["/OpsgenieApiKey"]= os.environ['OPSGENIE_API_KEY']
          data["UPDATE"]["/OpsgenieApiEndpoint"]= os.environ['OPSGENIE_API_ENDPOINT']
        print(data);
        with open('overrides.json', 'w') as f1:
            json.dump(data, f1, indent=4)

updateJsonFile()
ENDPY

# execute python and fail on error

echo "$PYSCRIPT" |python - || exit 1
