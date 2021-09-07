# Define here the models for your scraped items
#
# See documentation in:
# https://docs.scrapy.org/en/latest/topics/items.html

from dataclasses import dataclass
from datetime import date, time

import scrapy


@dataclass
class HinataSchedule:
    schedule_id: str
    title: str
    schedule_type: str
    schedule_date: date
    start_time: time
    end_time: time
