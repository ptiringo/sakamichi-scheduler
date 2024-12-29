resource "google_monitoring_notification_channel" "mail" {
  display_name = "Mail Notification"
  type         = "email"
  labels = {
    email_address = "ptiringo@gmail.com"
  }
}

resource "google_monitoring_alert_policy" "error_log" {
  enabled = false

  display_name          = "Error Log"
  combiner              = "OR"
  notification_channels = [google_monitoring_notification_channel.mail.name]

  alert_strategy {
    notification_rate_limit {
      period = "300s"
    }
  }

  conditions {
    display_name = "Cloud Function Error"

    condition_matched_log {
      filter = <<-EOT
      resource.type=("cloud_function" OR "cloud_run_revision" OR "cloud_scheduler_job") severity>=ERROR
      EOT
    }
  }
}
