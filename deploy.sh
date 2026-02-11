#!/usr/bin/env bash
set -euo pipefail

REMOTE="solutionbox2"
REMOTE_DIR="/root/DofusEmu"
ROOT="$(cd "$(dirname "$0")" && pwd)"
SRC="$ROOT/CyonEmu 2.9/Source/src"
OUT="$ROOT/CyonEmu 2.9/Source/out"
LIBS="$ROOT/CyonEmu 2.9/Source/libs/*"
EMU="$ROOT/CyonEmu 2.9/Emulateur"

# Remote path with escaped spaces for rsync
REMOTE_EMU="$REMOTE_DIR/CyonEmu\ 2.9/Emulateur"

echo "=== 1. Compile ==="
javac -encoding ISO-8859-1 -source 8 -target 8 \
  -cp "$LIBS" \
  -d "$OUT" \
  "$SRC/common/"*.java \
  "$SRC/objects/"*.java \
  "$SRC/game/"*.java \
  "$SRC/realm/"*.java 2>&1 | grep -v "bootstrap class path" || true
echo "Compilation OK"

echo "=== 2. Bundle libs from backup JAR ==="
cd "$OUT"
unzip -qo "$EMU/CyonEmu.jar.bak" -x "common/*" "objects/*" "game/*" "realm/*" "META-INF/MANIFEST.MF"

echo "=== 3. Build JAR ==="
jar cfe "$EMU/CyonEmu.jar" common.CyonEmu .
LOCAL_HASH=$(md5 -q "$EMU/CyonEmu.jar")
LOCAL_SIZE=$(stat -f%z "$EMU/CyonEmu.jar")
echo "Local JAR: $LOCAL_SIZE bytes, md5=$LOCAL_HASH"

echo "=== 4. Sync files to $REMOTE ==="
cd "$ROOT"

# Transfer JAR via ssh pipe (avoids rsync/scp space-in-path issues)
cat "$EMU/CyonEmu.jar" | ssh "$REMOTE" "cat > '$REMOTE_DIR/CyonEmu 2.9/Emulateur/CyonEmu.jar'"

rsync -avz \
  config/CyonConfig.txt \
  "$REMOTE:$REMOTE_DIR/config/CyonConfig.txt"

rsync -avz \
  docker-compose.yml \
  "$REMOTE:$REMOTE_DIR/docker-compose.yml"

rsync -avz \
  z-*.sql \
  "$REMOTE:$REMOTE_DIR/"

rsync -avz \
  registration/ \
  "$REMOTE:$REMOTE_DIR/registration/"

rsync -avz \
  docker/ \
  "$REMOTE:$REMOTE_DIR/docker/"

echo "=== 5. Verify remote JAR ==="
REMOTE_HASH=$(ssh "$REMOTE" "md5sum '$REMOTE_DIR/CyonEmu 2.9/Emulateur/CyonEmu.jar'" | awk '{print $1}')
REMOTE_SIZE=$(ssh "$REMOTE" "stat -c%s '$REMOTE_DIR/CyonEmu 2.9/Emulateur/CyonEmu.jar'")
echo "Remote JAR: $REMOTE_SIZE bytes, md5=$REMOTE_HASH"

if [ "$LOCAL_SIZE" != "$REMOTE_SIZE" ]; then
  echo "FATAL: Size mismatch! Local=$LOCAL_SIZE Remote=$REMOTE_SIZE"
  exit 1
fi
echo "Verification OK - JAR sizes match"

echo "=== 6. Apply SQL overrides ==="
# Execute all z-*.sql files against the running DB (sorted by name)
for sql in $(ssh "$REMOTE" "ls $REMOTE_DIR/z-*.sql 2>/dev/null | sort"); do
  fname=$(basename "$sql")
  echo "  Executing $fname ..."
  ssh "$REMOTE" "cat '$sql' | docker exec -i dofusemu-db-1 mysql -u root -pcyonemu cyon_2.9" 2>&1 | tail -5
done

echo "=== 7. Restart server ==="
ssh "$REMOTE" "cd $REMOTE_DIR && docker compose up -d --build --remove-orphans"
# Wait for containers to be created/recreated
sleep 2
# Force restart server to pick up new JAR + fresh DB data
ssh "$REMOTE" "cd $REMOTE_DIR && docker compose restart server"

echo "=== 8. Wait for server boot ==="
for i in $(seq 1 30); do
  sleep 2
  LAST_LINE=$(ssh "$REMOTE" "docker logs dofusemu-server-1 --tail=1 2>&1" || echo "")
  echo "  [$i] $LAST_LINE"
  if echo "$LAST_LINE" | grep -q "Emulator OK\|Emulateur OK\|comptes ont ete charge\|personnages ont ete charge\|live actions ont ete appliquees"; then
    echo ""
    echo "=== Server is UP ==="
    ssh "$REMOTE" "docker logs dofusemu-server-1 --tail=5 2>&1"
    echo ""
    echo "=== Deploy complete ==="
    exit 0
  fi
  if echo "$LAST_LINE" | grep -q "Fermeture du serveur"; then
    echo ""
    echo "FATAL: Server crashed during startup!"
    echo "Last 30 lines:"
    ssh "$REMOTE" "docker logs dofusemu-server-1 --tail=30 2>&1"
    exit 1
  fi
done

echo ""
echo "WARNING: Server did not confirm boot within 60s. Last logs:"
ssh "$REMOTE" "docker logs dofusemu-server-1 --tail=20 2>&1"
exit 1
