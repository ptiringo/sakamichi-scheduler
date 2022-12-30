#----------------------------------------------------------------------
# google_project_iam_member
#----------------------------------------------------------------------
locals {
  ci_executor_roles = [
    "roles/artifactregistry.writer",
    "roles/cloudfunctions.admin",
    "roles/cloudscheduler.admin",
    "roles/iam.serviceAccountUser",
    "roles/run.developer"
  ]
  sakamichi_scraper_invoker_roles = [
    "roles/run.invoker"
  ]
  hinata_notifier_executor_roles = [
    "roles/cloudfunctions.serviceAgent",
    "roles/datastore.user"
  ]
}

resource "google_project_iam_member" "ci_executor" {
  for_each = toset(local.ci_executor_roles)
  project  = data.google_project.project.id
  role     = each.value
  member   = "serviceAccount:${google_service_account.ci_executor.email}"
}

resource "google_project_iam_member" "sakamichi_scraper_invoker" {
  for_each = toset(local.sakamichi_scraper_invoker_roles)
  project  = data.google_project.project.id
  role     = each.value
  member   = "serviceAccount:${google_service_account.sakamichi_scraper_invoker.email}"
}

resource "google_project_iam_member" "hinata_notifier_executor" {
  for_each = toset(local.hinata_notifier_executor_roles)
  project  = data.google_project.project.id
  role     = each.value
  member   = "serviceAccount:${google_service_account.hinata_notifier_executor.email}"
}

#----------------------------------------------------------------------
# google_service_account_iam_member
#----------------------------------------------------------------------
locals {
  ci_executor_service_account_roles = [
    {
      service_account_id = google_service_account.sakamichi_scraper_executor.name
      role               = "roles/iam.serviceAccountUser"
    },
  ]
}

resource "google_service_account_iam_member" "ci_executor" {
  for_each = { for r in local.ci_executor_service_account_roles : "${r.service_account_id}_${r.role}" => {
    service_account_id = r.service_account_id
    role               = r.role
  } }
  service_account_id = each.value.service_account_id
  role               = each.value.role
  member             = "serviceAccount:${google_service_account.ci_executor.email}"
}

resource "google_service_account_iam_member" "repository_workload_identity_user_ci_executor" {
  service_account_id = google_service_account.ci_executor.name
  role               = "roles/iam.workloadIdentityUser"
  member             = "principalSet://iam.googleapis.com/${google_iam_workload_identity_pool.github.name}/attribute.repository/ptiringo/sakamichi-scheduler"
}

#----------------------------------------------------------------------
# google_storage_bucket_iam_member
#----------------------------------------------------------------------
locals {
  sakamichi_scraper_executor_storage_bucket_roles = [
    {
      bucket = google_storage_bucket.hinata_schedule.name
      role   = "roles/storage.legacyBucketReader"
    },
    {
      bucket = google_storage_bucket.hinata_schedule.name
      role   = "roles/storage.objectCreator"
    },
    {
      bucket = google_storage_bucket.nogi_schedule.name
      role   = "roles/storage.legacyBucketReader"
    },
    {
      bucket = google_storage_bucket.nogi_schedule.name
      role   = "roles/storage.objectCreator"
    }
  ]
  hinata_notifier_executor_storage_bucket_roles = [
    {
      bucket = google_storage_bucket.hinata_schedule.name
      role   = "roles/storage.objectViewer"
    },
    {
      bucket = google_storage_bucket.nogi_schedule.name
      role   = "roles/storage.objectViewer"
    }
  ]
}

resource "google_storage_bucket_iam_member" "sakamichi_scraper_executor" {
  for_each = { for r in local.sakamichi_scraper_executor_storage_bucket_roles : "${r.bucket}_${r.role}" => {
    bucket = r.bucket
    role   = r.role
  } }
  bucket = each.value.bucket
  role   = each.value.role
  member = "serviceAccount:${google_service_account.sakamichi_scraper_executor.email}"
}

resource "google_storage_bucket_iam_member" "hinata_notifier_executor" {
  for_each = { for r in local.hinata_notifier_executor_storage_bucket_roles : "${r.bucket}_${r.role}" => {
    bucket = r.bucket
    role   = r.role
  } }
  bucket = each.value.bucket
  role   = each.value.role
  member = "serviceAccount:${google_service_account.hinata_notifier_executor.email}"
}

#----------------------------------------------------------------------
# google_secret_manager_secret_iam_policy
#----------------------------------------------------------------------
locals {
  hinata_notifier_executor_secret_manager_secret_roles = [
    {
      secret_id = google_secret_manager_secret.line_bot_channel_token.secret_id
      role      = "roles/secretmanager.secretAccessor"
    },
    {
      secret_id = google_secret_manager_secret.line_bot_channel_secret.secret_id
      role      = "roles/secretmanager.secretAccessor"
    }
  ]
}

resource "google_secret_manager_secret_iam_member" "hinata_notifier_executor" {
  for_each = { for r in local.hinata_notifier_executor_secret_manager_secret_roles : "${r.secret_id}_${r.role}" => {
    secret_id = r.secret_id
    role      = r.role
  } }
  secret_id = each.value.secret_id
  role      = each.value.role
  member    = "serviceAccount:${google_service_account.hinata_notifier_executor.email}"
}
