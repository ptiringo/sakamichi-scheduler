resource "google_cloud_scheduler_job" "collect_hinata_schedule" {
  name      = "collect-hinata-schedule"
  schedule  = "every 6 hours"
  time_zone = "Asia/Tokyo"

  http_target {
    http_method = "GET"
    uri         = "${data.google_cloud_run_service.sakamichi_scraper.status[0].url}/crawl.json?spider_name=hinata_schedule&start_requests=true"

    oidc_token {
      service_account_email = google_service_account.sakamichi_scraper_invoker.email
    }
  }
}

resource "google_cloud_scheduler_job" "collect_nogi_schedule" {
  name      = "collect-nogi-schedule"
  schedule  = "every 6 hours"
  time_zone = "Asia/Tokyo"

  http_target {
    http_method = "GET"
    uri         = "${data.google_cloud_run_service.sakamichi_scraper.status[0].url}/crawl.json?spider_name=nogi_schedule&start_requests=true"

    oidc_token {
      service_account_email = google_service_account.sakamichi_scraper_invoker.email
    }
  }
}
