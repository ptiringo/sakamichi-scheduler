FROM python:3.9

WORKDIR /app
COPY ./sakamichi_scraper scrapy.cfg poetry.lock pyproject.toml ./

ENV POETRY_HOME=/opt/poetry

RUN curl -sSL https://raw.githubusercontent.com/python-poetry/poetry/master/install-poetry.py | python - && \
    ln -s /opt/poetry/bin/poetry /usr/local/bin/poetry && \
    poetry config virtualenvs.create false && \
    poetry install

EXPOSE 8080

ENTRYPOINT ["poetry", "run", "scrapyrt", "--port", "8080", "--ip", "0.0.0.0"]