import re
from datetime import date, datetime, timedelta
from typing import Any, Generator, Iterator
from urllib.parse import ParseResult, urlparse

import scrapy
from dateutil.relativedelta import relativedelta
from scrapy import Request
from scrapy.selector import SelectorList

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
            yield scrapy.FormRequest(
                "https://www.nogizaka46.com/schedule/",
                formdata={"monthly": f"{d.strftime('%Y%m')}"},
            )

    def parse(
        self, response: scrapy.http.TextResponse, **kwargs
    ) -> Generator[NogiSchedule, Any, None]:

        yyyymm = datetime.strptime(
            response.css("#scheduleH2::text").get().strip(), "%Y年%m月"
        )

        schedule_tables = response.css("#scheduleTable div.scheduleTableList")

        for schedule_table in schedule_tables:
            day_of_month = int(
                schedule_table.css("div.first-child span::text").get().strip()
            )

            schedule_date = yyyymm.replace(day=day_of_month)

            schedules = schedule_table.css("div.last-child ul li")

            for schedule in schedules:
                link: SelectorList = schedule.css("a")
                url: ParseResult = urlparse(link.attrib["href"])
                schedule_id = url.path.split("/")[-1].removesuffix(".php")

                link_text = link.css("::text").get()

                match = re.match(
                    r"(?P<start_hour>\d{1,2}):(?P<start_minute>\d{2})"
                    r"[〜～]"
                    r"(?P<end_hour>\d{1,2}):(?P<end_minute>\d{2})"
                    r" (?P<title>.*)",
                    link_text,
                )

                if match:
                    start_hour, start_minute, end_hour, end_minute, title = (
                        int(match.group("start_hour")),
                        int(match.group("start_minute")),
                        int(match.group("end_hour")),
                        int(match.group("end_minute")),
                        match.group("title"),
                    )

                    s_days, s_hour = divmod(start_hour, 24)
                    start_time = schedule_date + timedelta(
                        days=s_days, hours=s_hour, minutes=start_minute
                    )

                    e_days, e_hour = divmod(end_hour, 24)
                    end_time = schedule_date + timedelta(
                        days=e_days, hours=e_hour, minutes=end_minute
                    )

                else:
                    title = link_text
                    start_time = None
                    end_time = None

                yield NogiSchedule(
                    schedule_id=schedule_id,
                    title=title,
                    schedule_date=schedule_date,
                    schedule_type=link.attrib["class"],
                    start_time=start_time,
                    end_time=end_time,
                )
