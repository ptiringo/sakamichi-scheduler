from diagrams import Diagram
from diagrams.gcp.compute import Functions, Run
from diagrams.gcp.database import Firestore
from diagrams.gcp.devtools import Scheduler
from diagrams.gcp.storage import Storage

with Diagram(
    "Sakamichi Schedule Notification",
    filename="output/architecture_diagram",
    show=False,
):
    (
        Scheduler("scheduler")
        >> Run("sakamichi-scraper")
        >> Storage("hinata-schedule")
        >> Functions("sakamichi-noticer")
        >> Firestore("hinata-schedule")
    )
