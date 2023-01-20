#
#!/bin/bash

function readJson() {
  UNAMESTR=$(uname)
  if [[ "$UNAMESTR" == 'Linux' ]]; then
    SED_EXTENDED='-r'
  elif [[ "$UNAMESTR" == 'Darwin' ]]; then
    SED_EXTENDED='-E'
  fi

  VALUE=$(grep -m 1 "\"${2}\"" ${1} | sed ${SED_EXTENDED} 's/^ *//;s/.*: *"//;s/",?//')

  if [ ! "$VALUE" ]; then
    echo "Error: Cannot find \"${2}\" in ${1}" >&2
    exit 1
  else
    echo $VALUE
  fi
}

mvn -version
mvn package -B
if [[ "$?" -ne 0 ]] ; then
  echo 'build failed'; exit 1
fi

cfn submit --dry-run
aws s3 cp $RESOURCE_PACKAGE.zip  s3://$BUCKET_NAME
aws cloudformation register-type --type RESOURCE --type-name $RESOURCE_NAME --schema-handler-package s3://$BUCKET_NAME/$RESOURCE_PACKAGE.zip > registration_token.json

REGISTRATION_TOKEN=$(readJson registration_token.json RegistrationToken) || exit 1

aws cloudformation describe-type-registration --registration-token $REGISTRATION_TOKEN >registration_status.json

REGISTRATION_STATUS=$(readJson registration_status.json ProgressStatus) || exit 1

while [ $REGISTRATION_STATUS = "IN_PROGRESS" ]; do
  aws cloudformation describe-type-registration --registration-token $REGISTRATION_TOKEN >registration_status.json
  REGISTRATION_STATUS=$(readJson registration_status.json ProgressStatus) || exit 1
done

echo $REGISTRATION_STATUS

if [ $REGISTRATION_STATUS = "COMPLETE" ]; then
  TYPE_VERSION_ARN=$(readJson registration_status.json TypeVersionArn) || exit 1
  echo $TYPE_VERSION_ARN
  aws cloudformation set-type-default-version --arn $TYPE_VERSION_ARN
else
  exit 1
fi
