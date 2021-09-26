#!/usr/bin/env bash

set -eu -o pipefail

gcloud functions deploy hinata-schedule-notifier \
  --runtime java11 \
  --region asia-northeast1 \
  --entry-point org.springframework.cloud.function.adapter.gcp.GcfJarLauncher \
  --trigger-resource gs://hinata-schedule \
  --trigger-event google.storage.object.finalize \
  --source target/deploy \
  --memory 512MB \
  --set-env-vars SPRING_PROFILES_ACTIVE=prod \
  --service-account hinata-notifier-executor@${PROJECT_ID}.iam.gserviceaccount.com