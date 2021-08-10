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
