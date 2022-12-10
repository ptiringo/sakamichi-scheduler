terraform {
  required_version = "=1.3.6"

  required_providers {
    google = {
      source  = "hashicorp/google"
      version = "4.45.0"
    }
  }

  cloud {
    organization = "ptiringo"
    workspaces {
      name = "sakamichi-infra"
    }
  }
}

provider "google" {
  project = "sakamichi-noticer"
  region  = "asia-northeast1"
  zone    = "asia-northeast1-a"
}
