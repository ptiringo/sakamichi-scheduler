resource "google_secret_manager_secret" "line_bot_channel_token" {
  secret_id = "line-bot-channel-token"

  replication {
    automatic = true
  }
}

resource "google_secret_manager_secret" "line_bot_channel_secret" {
  secret_id = "line-bot-channel-secret"

  replication {
    automatic = true
  }
}

resource "google_secret_manager_secret" "notification_target_line_user_id" {
  secret_id = "notification-target-line-user-id"

  replication {
    automatic = true
  }
}
