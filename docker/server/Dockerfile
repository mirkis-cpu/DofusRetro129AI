FROM eclipse-temurin:8-jre

WORKDIR /app

EXPOSE 4444 5555

CMD ["bash", "-c", "\
echo 'Waiting for MySQL at ${DB_HOST:-db}:${DB_PORT:-3306}...' && \
retries=0 && \
while ! (echo > /dev/tcp/${DB_HOST:-db}/${DB_PORT:-3306}) 2>/dev/null; do \
  retries=$((retries + 1)); \
  if [ $retries -ge 60 ]; then \
    echo 'ERROR: MySQL not available after 120s'; \
    exit 1; \
  fi; \
  echo \"MySQL not ready... retry ${retries}/60\"; \
  sleep 2; \
done && \
echo 'MySQL is ready! Starting CyonEmu server...' && \
exec java -jar -Xms512m -Xmx1500m CyonEmu.jar"]
