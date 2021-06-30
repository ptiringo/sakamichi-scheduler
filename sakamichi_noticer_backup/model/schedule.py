from datetime import date, datetime
from enum import Enum, auto
from typing import Optional


class ScheduleType(Enum):
    TV = "テレビ"
    RADIO = "ラジオ"
    MAGAZINE = "雑誌"
    EVENT = auto()
    BIRTHDAY = auto()
    HANDSHAKE_MEETING = auto()
    LIVE = "配信"
    OTHER = auto()

    @classmethod
    def value_of(cls, target_value):
        for t in ScheduleType:
            if t.value == target_value:
                return t
        return ScheduleType.OTHER


class Schedule:
    def __init__(
        self,
        id: str,
        title: str,
        schedule_type: ScheduleType,
        schedule_date: date,
        schedule_start_date: Optional[datetime] = None,
    ):
        self.id = id
        self.title = title
        self.schedule_type = schedule_type
        self.schedule_date = schedule_date
        self.schedule_start_date = schedule_start_date
