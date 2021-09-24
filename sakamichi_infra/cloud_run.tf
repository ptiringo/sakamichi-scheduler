data "google_cloud_run_service" "sakamichi_scraper" {
  name     = "sakamichi-scraper"
  location = "asia-northeast1"
}
