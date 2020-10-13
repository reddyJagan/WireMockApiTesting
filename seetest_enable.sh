#!/system/bin/sh
//#!/bin/bash
TEST="curl --location --request POST '127.0.0.1:5000/test' \
--form 'file=@/WiremockApiTesting/postman.PNG'"

echo $TEST

RESPONSE=`$TEST`
echo $RESPONSE