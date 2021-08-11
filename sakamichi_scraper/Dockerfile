FROM python:3.9

ARG WORK_DIR=/app
ARG USER=scrapyrt

RUN useradd --create-home ${USER} && \
    mkdir ${WORK_DIR} && \
    chown ${USER} ${WORK_DIR} && \
    chmod 0755 ${WORK_DIR}

USER ${USER}

COPY ./sakamichi_scraper scrapy.cfg poetry.lock pyproject.toml ${WORK_DIR}/

WORKDIR /app

ENV PATH /home/${USER}/.local/bin:$PATH

RUN curl -sSL https://raw.githubusercontent.com/python-poetry/poetry/master/install-poetry.py | python - && \
    poetry config virtualenvs.create false && \
    poetry install && \
    mkdir logs

EXPOSE 8080

ENTRYPOINT ["poetry", "run", "scrapyrt", "--port", "8080", "--ip", "0.0.0.0"]