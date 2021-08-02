FROM python:3.9

COPY . /app
WORKDIR /app

ENV POETRY_HOME=/opt/poetry
RUN curl -sSL https://raw.githubusercontent.com/python-poetry/poetry/master/install-poetry.py | python -
RUN cd /usr/local/bin && ln -s /opt/poetry/bin/poetry

RUN poetry config virtualenvs.create false
RUN poetry install --no-root

EXPOSE 8080
ENTRYPOINT ["poetry", "run", "scrapyrt", "--port", "8080", "--ip", "0.0.0.0"]