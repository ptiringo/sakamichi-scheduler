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

#----------------------------------------------------------------------
# google_service_account_iam_member
#----------------------------------------------------------------------
resource "google_service_account_iam_member" "sakamichi_scraper_executor_service_account_user_ci_executor" {
  service_account_id = google_service_account.sakamichi_scraper_executor.name
  role               = "roles/iam.serviceAccountUser"
  member             = "serviceAccount:${google_service_account.ci_executor.email}"
}
