from collections.abc import Iterable
from datetime import date, datetime, time, timedelta
from urllib.parse import urlparse

import scrapy
from dateutil.relativedelta import relativedelta
from sakamichi_scraper.items import HinataSchedule


class HinataScheduleSpider(scrapy.Spider):
    name = "hinata_schedule"

    allowed_domains = "hinatazaka46.com"

    custom_settings = {
        "FEEDS": {
            f"gs://hinata-schedule/hinata_schedule_%(batch_time)s.jl": {
                "format": "jsonlines",
                "encoding": "utf-8",
            }
        },
        "GCS_PROJECT_ID": "sakamichi-noticer",
    }

    def start_requests(self):
        today = date.today()

        # 3ヶ月分
        for d in [
            today,
            today + relativedelta(months=1),
            today + relativedelta(months=2),
        ]:
            yield scrapy.Request(
                f"https://www.hinatazaka46.com/s/official/media/list?ima=0000&dy={d.strftime('%Y%m')}",
            )

    def parse(self, response: scrapy.http.TextResponse):
        div_page_date = response.css("div.p-schedule__page_date")

        year = int(
            div_page_date.css("div.c-schedule__page_year::text")
            .get()
            .strip()
            .removesuffix("年")
        )
        month = int(
            div_page_date.css("div.c-schedule__page_month::text")
            .get()
            .strip()
            .removesuffix("月")
        )

        for div_list_group in response.css("div.p-schedule__list-group"):

            day_of_month = int(
                div_list_group.css("div.c-schedule__date--list span::text")
                .get()
                .strip()
            )

            for li_item in div_list_group.css(
                "ul.p-schedule__list li.p-schedule__item"
            ):
                a = li_item.css("a")

                print(
                    urlparse(a.attrib["href"]).path.split("/")[-1],
                    a.css("p.c-schedule__text::text").get().strip(),
                )

                schedule_date = date(year, month, day_of_month)

                time_str: str = a.css("div.c-schedule__time--list::text").get()

                start_time = None
                end_time = None

                if len(time_str) != 0:
                    splited_time = time_str.strip().split("～")
                    splited_start_time = splited_time[0].split(":")
                    schedule_date_time = start_time = datetime.combine(
                        schedule_date, time()
                    )

                    if len(splited_start_time) > 1:
                        start_time = schedule_date_time + timedelta(
                            hours=int(splited_start_time[0]),
                            minutes=int(splited_start_time[1]),
                        )

                    if splited_time[-1]:
                        splited_end_time = splited_time[-1].split(":")
                        end_time = schedule_date_time + +timedelta(
                            hours=int(splited_end_time[0]),
                            minutes=int(splited_end_time[1]),
                        )

                yield HinataSchedule(
                    schedule_id=urlparse(a.attrib["href"]).path.split("/")[-1],
                    title=a.css("p.c-schedule__text::text").get().strip(),
                    schedule_date=date(year, month, day_of_month),
                    schedule_type=a.css("div.c-schedule__category::text").get().strip(),
                    start_time=start_time if start_time else None,
                    end_time=end_time if end_time else None,
                )
