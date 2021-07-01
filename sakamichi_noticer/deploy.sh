gcloud functions deploy update-schedule-hinata \
  --entry-point tokyo.sakamichi_noticer.UpdateSchedule \
  --runtime java11 \
  --memory 512MB \
  --trigger-resource hinata-schedule \
  --trigger-event google.storage.object.finalize \
  --project sakamichi-noticer