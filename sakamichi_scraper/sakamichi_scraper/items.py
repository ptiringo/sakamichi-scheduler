# Define here the models for your scraped items
#
# See documentation in:
# https://docs.scrapy.org/en/latest/topics/items.html

from dataclasses import dataclass
from datetime import date, time
from typing import Optional


@dataclass
class HinataSchedule:
    schedule_id: str
    title: str
    schedule_type: str
    schedule_date: date
    start_time: Optional[time]
    end_time: Optional[time]


@dataclass
class NogiSchedule:
    schedule_id: str
    title: str
    schedule_type: str
    schedule_date: date
    start_time: Optional[time]
    end_time: Optional[time]
