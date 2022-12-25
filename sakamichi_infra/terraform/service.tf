locals {
  services = [
    "appengine",
    "cloudbuild",
    "cloudfunctions",
    "cloudscheduler",
    "compute",
    "datastore",
    "iam",
    "monitoring",
    "secretmanager"
  ]
}

resource "google_project_service" "service" {
  for_each = toset(local.services)
  service  = "${each.value}.googleapis.com"
}
