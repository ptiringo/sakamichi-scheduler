from datetime import date, datetime, time, timedelta
from pprint import pprint
from typing import Any, Generator, Iterator
from urllib.parse import parse_qs, urlparse

import scrapy
from dateutil.relativedelta import relativedelta
from scrapy import Request
from scrapy_splash import SplashRequest

from sakamichi_scraper.items import NogiSchedule


class NogiScheduleSpider(scrapy.Spider):
    name = "nogi_schedule"

    allowed_domains = "www.nogizaka46.com"

    custom_settings = {
        "FEEDS": {
            f"gs://nogi-schedule/nogi_schedule_%(batch_time)s.jl": {
                "format": "jsonlines",
                "encoding": "utf-8",
            }
        },
        "GCS_PROJECT_ID": "sakamichi-noticer",
    }

    def start_requests(self) -> Iterator[Request]:
        today = date.today()

        # 3ヶ月分
        for d in [
            today,
            today + relativedelta(months=1),
            today + relativedelta(months=2),
        ]:
            yield SplashRequest(
                url=f"https://www.nogizaka46.com/s/n46/media/list?dy={d.strftime('%Y%m')}"
            )

    def parse(
        self, response: scrapy.http.TextResponse, **kwargs
    ) -> Generator[NogiSchedule, Any, None]:
        """

        @url https://www.nogizaka46.com/s/n46/media/list?dy=202301
        @returns items 210
        """
        qs = parse_qs(urlparse(response.url).query)

        yyyymm = datetime.strptime(qs["dy"][0], "%Y%m")

        schedule_days = response.css("div.sc--lists .sc--day")

        for schedule_day in schedule_days:
            dd = int(schedule_day.css("p.sc--day__d::text")[0].get())
            schedule_date = yyyymm.replace(day=dd)

            schedules = schedule_day.css("div.m--sclist div.m--scone")

            for schedule in schedules:
                url = schedule.css("a.m--scone__a")[0].attrib["href"]
                schedule_id = urlparse(url).path.split("/")[-1]

                title = schedule.css("p.m--scone__ttl::text")[0].get()
                schedule_type = schedule.css("p.m--scone__cat__name::text")[0].get()
                time_str = schedule.css("p.m--scone__st::text").get()

                start_time = None
                end_time = None

                if time_str is not None:
                    splitted_time = time_str.strip().split("〜")
                    splitted_start_time = splitted_time[0].split(":")
                    schedule_date_time = datetime.combine(schedule_date, time())

                    if len(splitted_start_time) > 1:
                        start_time = schedule_date_time + timedelta(
                            hours=int(splitted_start_time[0]),
                            minutes=int(splitted_start_time[1]),
                        )

                    if splitted_time[-1]:
                        splitted_end_time = splitted_time[-1].split(":")
                        end_time = schedule_date_time + timedelta(
                            hours=int(splitted_end_time[0]),
                            minutes=int(splitted_end_time[1]),
                        )

                yield NogiSchedule(
                    schedule_id=schedule_id,
                    title=title,
                    schedule_date=schedule_date,
                    schedule_type=schedule_type,
                    start_time=start_time,
                    end_time=end_time,
                )
