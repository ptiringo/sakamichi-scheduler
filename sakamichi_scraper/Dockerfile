FROM python:3.9

# RUN useradd scrapyrt
# USER scrapyrt

COPY ./sakamichi_scraper scrapy.cfg poetry.lock pyproject.toml /app/

ENV POETRY_HOME=/opt/poetry

WORKDIR /app

RUN curl -sSL https://raw.githubusercontent.com/python-poetry/poetry/master/install-poetry.py | python - && \
    ln -s /opt/poetry/bin/poetry /usr/local/bin/poetry && \
    poetry config virtualenvs.create false && \
    poetry install

EXPOSE 8080


RUN poetry install

ENTRYPOINT ["poetry", "run", "scrapyrt", "--port", "8080", "--ip", "0.0.0.0"]