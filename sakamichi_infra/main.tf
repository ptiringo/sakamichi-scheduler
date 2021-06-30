terraform {
  required_version = "=1.0.1"

  required_providers {
    google = {
      source  = "hashicorp/google"
      version = "3.74.0"
    }
  }

  backend "remote" {
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
