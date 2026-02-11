FROM python:3.11-slim
RUN pip install mysql-connector-python
WORKDIR /app
COPY server.py .
CMD ["python3", "-u", "server.py"]
