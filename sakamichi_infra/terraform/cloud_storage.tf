resource "google_storage_bucket" "hinata_schedule" {
  name          = "hinata-schedule"
  location      = "ASIA-NORTHEAST1"
  storage_class = "STANDARD"
}
