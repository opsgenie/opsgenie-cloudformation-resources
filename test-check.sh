#
#!/bin/bash

function readJson {
  UNAMESTR=`uname`
  if [[ "$UNAMESTR" == 'Linux' ]]; then
    SED_EXTENDED='-r'
  elif [[ "$UNAMESTR" == 'Darwin' ]]; then
    SED_EXTENDED='-E'
  fi;

  VALUE=`grep -m 1 "\"${2}\"" ${1} | sed ${SED_EXTENDED} 's/^ *//;s/.*: *"//;s/",?//'`

  if [ ! "$VALUE" ]; then
    echo "Error: Cannot find \"${2}\" in ${1}" >&2;
    exit 1;
  else
    echo $VALUE ;
  fi;
}

aws cloudformation test-type --type RESOURCE --type-name $RESOURCE_NAME --log-delivery-bucket $BUCKET_NAME> test_arn.json

TEST_ARN=`readJson test_arn.json TypeVersionArn` || exit 1;

echo $TEST_ARN

aws cloudformation describe-type --arn $TEST_ARN > test-result.json

TEST_STATUS=`readJson test-result.json TypeTestsStatus` || exit 1;

while [ $TEST_STATUS = "IN_PROGRESS" ]; do
  sleep 60
  aws cloudformation describe-type --arn $TEST_ARN > test-result.json
  TEST_STATUS=`readJson test-result.json TypeTestsStatus` || exit 1;
  echo $TEST_STATUS
done

if [ $TEST_STATUS = "PASSED" ]; then
  echo $TEST_STATUS
  echo $TEST_STATUS > test_result.txt
else
  echo $TEST_STATUS
  echo $TEST_STATUS > test_result.txt
  exit 1
fi

