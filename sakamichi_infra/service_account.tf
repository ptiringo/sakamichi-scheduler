data "google_compute_default_service_account" "compute_default" {
  depends_on = [google_project_service.compute]
}

resource "google_service_account" "ci_executor" {
  account_id   = "ci-executor"
  display_name = "CI Executor"
}

resource "google_service_account_key" "ci_executor" {
  service_account_id = google_service_account.ci_executor.name
  depends_on         = [google_project_service.compute]
}
