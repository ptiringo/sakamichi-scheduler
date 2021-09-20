#----------------------------------------------------------------------
# google_project_iam_member
#----------------------------------------------------------------------
resource "google_project_iam_member" "ci_executor_artifactregistry_writer" {
  role   = "roles/artifactregistry.writer"
  member = "serviceAccount:${google_service_account.ci_executor.email}"
}

resource "google_project_iam_member" "ci_executor_run_developer" {
  role   = "roles/run.developer"
  member = "serviceAccount:${google_service_account.ci_executor.email}"
}

resource "google_project_iam_member" "ci_executor_service_account_user" {
  role   = "roles/iam.serviceAccountUser"
  member = "serviceAccount:${google_service_account.ci_executor.email}"
}

resource "google_project_iam_member" "ci_executor_invoker_cloudscheduler_admin" {
  role   = "roles/cloudscheduler.admin"
  member = "serviceAccount:${google_service_account.ci_executor.email}"
}

resource "google_project_iam_member" "ci_executor_invoker_cloudfunctions_admin" {
  role   = "roles/cloudfunctions.admin"
  member = "serviceAccount:${google_service_account.ci_executor.email}"
}

resource "google_project_iam_member" "sakamichi_scraper_invoker_run_invoker" {
  role   = "roles/run.invoker"
  member = "serviceAccount:${google_service_account.sakamichi_scraper_invoker.email}"
}

resource "google_project_iam_member" "hinata_notifier_executor_cloudfunctions_service_agent" {
  role   = "roles/cloudfunctions.serviceAgent"
  member = "serviceAccount:${google_service_account.hinata_notifier_executor.email}"
}

#----------------------------------------------------------------------
# google_service_account_iam_member
#----------------------------------------------------------------------
resource "google_service_account_iam_member" "sakamichi_scraper_executor_service_account_user_ci_executor" {
  service_account_id = google_service_account.sakamichi_scraper_executor.name
  role               = "roles/iam.serviceAccountUser"
  member             = "serviceAccount:${google_service_account.ci_executor.email}"
}

#----------------------------------------------------------------------
# google_storage_bucket_iam_member
#----------------------------------------------------------------------
resource "google_storage_bucket_iam_member" "hinata_schedule_storage_legacy_bucket_reader_sakamichi_scraper_executor" {
  bucket = google_storage_bucket.hinata_schedule.name
  role   = "roles/storage.legacyBucketReader"
  member = "serviceAccount:${google_service_account.sakamichi_scraper_executor.email}"
}

resource "google_storage_bucket_iam_member" "hinata_schedule_storage_object_creator_sakamichi_scraper_executor" {
  bucket = google_storage_bucket.hinata_schedule.name
  role   = "roles/storage.objectCreator"
  member = "serviceAccount:${google_service_account.sakamichi_scraper_executor.email}"
}

resource "google_storage_bucket_iam_member" "hinata_schedule_storage_object_viewer_hinata_notifier_executor" {
  bucket = google_storage_bucket.hinata_schedule.name
  role   = "roles/storage.objectViewer"
  member = "serviceAccount:${google_service_account.hinata_notifier_executor.email}"
}

#----------------------------------------------------------------------
# google_secret_manager_secret_iam_policy
#----------------------------------------------------------------------
resource "google_secret_manager_secret_iam_member" "line_bot_channel_token_secretmanager_secret_accessor_hinata_notifier_executor" {
  secret_id = google_secret_manager_secret.line_bot_channel_token.secret_id
  role      = "roles/secretmanager.secretAccessor"
  member    = "serviceAccount:${google_service_account.hinata_notifier_executor.email}"
}

resource "google_secret_manager_secret_iam_member" "line_bot_channel_secret_secretmanager_secret_accessor_hinata_notifier_executor" {
  secret_id = google_secret_manager_secret.line_bot_channel_secret.secret_id
  role      = "roles/secretmanager.secretAccessor"
  member    = "serviceAccount:${google_service_account.hinata_notifier_executor.email}"
}
