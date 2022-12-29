# syntax=docker/dockerfile:1
FROM python:3.11

ARG WORK_DIR=/app
ARG LOG_DIR=/var/log/scrapyrt
ARG USER=scrapyrt

SHELL ["/bin/bash", "-o", "pipefail", "-c"]

ENV POETRY_VERSION=1.3.0
ENV POETRY_HOME=/usr/local/poetry
ENV PATH ${PATH}:${POETRY_HOME}/bin

WORKDIR ${WORK_DIR}

COPY install.python-poetry.py ./

RUN <<EOF
  useradd --create-home ${USER}
  mkdir ${LOG_DIR}
  chown ${USER} ${WORK_DIR} ${LOG_DIR}
  chmod 0755 ${WORK_DIR} ${LOG_DIR}
  python install.python-poetry.py
EOF

USER ${USER}

COPY poetry.lock pyproject.toml ./

RUN <<EOF
  mkdir logs
  poetry install
EOF

COPY scrapy.cfg ./
COPY sakamichi_scraper ./sakamichi_scraper

ENV PATH /home/${USER}/.local/bin:$PATH

EXPOSE 8080

ENTRYPOINT ["poetry", "run", "scrapyrt", "--port", "8080", "--ip", "0.0.0.0", "-s", "LOG_DIR=/var/log/scrapyrt"]