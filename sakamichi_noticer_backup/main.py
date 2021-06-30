from sakamichi_scheduler.model.schedule import Schedule, ScheduleType
from datetime import date, datetime


def main():
    schedule = Schedule(
        id="",
        title="",
        schedule_type=ScheduleType.EVENT,
        schedule_date=datetime(2020, 12, 1),
        schedule_start_date=datetime(2020, 12, 1),
    )
    print(schedule)


if __name__ == "__main__":
    main()
