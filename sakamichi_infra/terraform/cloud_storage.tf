resource "google_storage_bucket" "hinata_schedule" {
  name          = "hinata-schedule"
  location      = "ASIA-NORTHEAST1"
  storage_class = "STANDARD"
}

resource "google_storage_bucket" "nogi_schedule" {
  name          = "nogi-schedule"
  location      = "ASIA-NORTHEAST1"
  storage_class = "STANDARD"
}
