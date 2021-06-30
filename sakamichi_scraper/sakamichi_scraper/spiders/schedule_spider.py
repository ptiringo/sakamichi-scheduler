from collections.abc import Iterable
from datetime import date
from urllib.parse import urlparse

import scrapy
from sakamichi_scheduler.model.schedule import ScheduleType


class ScheduleSpider(scrapy.Spider):
    name = "schedule"

    start_urls = [
        f"https://www.hinatazaka46.com/s/official/media/list?ima=0000&dy={date.today().strftime('%Y%m')}"
    ]

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

            schedule_date = date(year, month, day_of_month)

            for li_item in div_list_group.css(
                "ul.p-schedule__list li.p-schedule__item"
            ):
                a = li_item.css("a")

                id = urlparse(a.attrib["href"]).path.split("/")[-1]
                title = a.css("p.c-schedule__text::text").get().strip()
                schedule_type = a.css("div.c-schedule__category::text").get().strip()

                yield {
                    "id": id,
                    "title": title,
                    "schedule_type": schedule_type,
                    "schedule_date": schedule_date,
                }

        print(year)
        print(month)
