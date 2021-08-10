resource "google_project_iam_member" "ci_executor_artifactregistry_writer" {
  role   = "roles/artifactregistry.writer"
  member = "serviceAccount:${google_service_account.ci_executor.email}"
}
