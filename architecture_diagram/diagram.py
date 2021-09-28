from pathlib import Path

from diagrams import Diagram
from diagrams.custom import Custom
from diagrams.gcp.compute import Functions, Run
from diagrams.gcp.database import Firestore
from diagrams.gcp.devtools import Scheduler
from diagrams.gcp.storage import Storage

with Diagram(
    "Sakamichi Schedule Notification",
    filename="output/architecture_diagram",
    show=True,
):
    (
        Scheduler("scheduler")
        >> Run("sakamichi-scraper")
        >> Storage("hinata-schedule")
        >> Functions("sakamichi-noticer")
        >> Firestore("hinata-schedule")
        >> Custom("LINE", str(Path("./resources/LINE_Brand_icon.png").resolve()))
    )
