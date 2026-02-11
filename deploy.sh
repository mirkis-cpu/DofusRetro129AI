#!/usr/bin/env bash
set -euo pipefail

ROOT="$(cd "$(dirname "$0")" && pwd)"
SRC="$ROOT/CyonEmu 2.9/Source/src"
OUT="$ROOT/CyonEmu 2.9/Source/out"
LIBS="$ROOT/CyonEmu 2.9/Source/libs/*"
EMU="$ROOT/CyonEmu 2.9/Emulateur"

echo "=== 1. Compile ==="
javac -encoding ISO-8859-1 -source 8 -target 8 \
  -cp "$LIBS" \
  -d "$OUT" \
  "$SRC/common/"*.java \
  "$SRC/objects/"*.java \
  "$SRC/game/"*.java \
  "$SRC/realm/"*.java 2>&1 | grep -v "bootstrap class path" || true

echo "=== 2. Bundle libs ==="
cd "$OUT"
unzip -qo "$EMU/CyonEmu.jar.bak" -x "common/*" "objects/*" "game/*" "realm/*" "META-INF/MANIFEST.MF"

echo "=== 3. Build JAR ==="
jar cfe "$EMU/CyonEmu-fixed.jar" common.CyonEmu .
cp "$EMU/CyonEmu-fixed.jar" "$EMU/CyonEmu.jar"

echo "=== 4. Deploy ==="
cd "$ROOT"
docker compose restart server

echo "=== Done ==="
