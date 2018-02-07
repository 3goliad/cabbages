#!/bin/bash

aws s3 sync \
	--exclude ".git/*" \
	--exclude "deploy.sh" \
	. s3://cabbag.es
