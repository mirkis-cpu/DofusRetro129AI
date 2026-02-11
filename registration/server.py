#!/usr/bin/env python3
import http.server
import json
import urllib.parse
import mysql.connector
import os
import re

DB_HOST = os.environ.get("DB_HOST", "db")
DB_USER = os.environ.get("DB_USER", "root")
DB_PASS = os.environ.get("DB_PASS", "cyonemu")
DB_NAME = os.environ.get("DB_NAME", "cyon_2.9")

HTML = """<!DOCTYPE html>
<html lang="cs">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>DofusAI - Registration</title>
<style>
* { margin: 0; padding: 0; box-sizing: border-box; }
body {
    font-family: 'Segoe UI', sans-serif;
    background: linear-gradient(135deg, #1a1a2e 0%, #16213e 50%, #0f3460 100%);
    min-height: 100vh;
    display: flex;
    align-items: center;
    justify-content: center;
    color: #e0e0e0;
}
.card {
    background: rgba(255,255,255,0.07);
    backdrop-filter: blur(10px);
    border: 1px solid rgba(255,255,255,0.15);
    border-radius: 16px;
    padding: 40px;
    width: 380px;
    box-shadow: 0 8px 32px rgba(0,0,0,0.3);
}
h1 {
    text-align: center;
    font-size: 28px;
    margin-bottom: 8px;
    color: #e94560;
}
.subtitle {
    text-align: center;
    font-size: 13px;
    color: #888;
    margin-bottom: 30px;
}
label {
    display: block;
    font-size: 13px;
    margin-bottom: 6px;
    color: #aaa;
}
input {
    width: 100%;
    padding: 12px 14px;
    border: 1px solid rgba(255,255,255,0.15);
    border-radius: 8px;
    background: rgba(255,255,255,0.05);
    color: #fff;
    font-size: 15px;
    margin-bottom: 18px;
    outline: none;
    transition: border-color 0.2s;
}
input:focus { border-color: #e94560; }
button {
    width: 100%;
    padding: 13px;
    border: none;
    border-radius: 8px;
    background: #e94560;
    color: #fff;
    font-size: 16px;
    font-weight: 600;
    cursor: pointer;
    transition: background 0.2s;
}
button:hover { background: #c73750; }
button:disabled { background: #555; cursor: not-allowed; }
.msg {
    margin-top: 16px;
    padding: 12px;
    border-radius: 8px;
    text-align: center;
    font-size: 14px;
    display: none;
}
.msg.ok { display: block; background: rgba(46,204,113,0.15); color: #2ecc71; border: 1px solid rgba(46,204,113,0.3); }
.msg.err { display: block; background: rgba(231,76,60,0.15); color: #e74c3c; border: 1px solid rgba(231,76,60,0.3); }
</style>
</head>
<body>
<div class="card">
    <h1>DofusAI</h1>
    <p class="subtitle">Create your account</p>
    <form id="form" onsubmit="return register(event)">
        <label>Username</label>
        <input type="text" id="user" required minlength="3" maxlength="25" pattern="[a-zA-Z0-9_-]+" placeholder="min 3 characters, a-z 0-9">
        <label>Password</label>
        <input type="password" id="pass" required minlength="3" maxlength="40" placeholder="min 3 characters">
        <label>Confirm Password</label>
        <input type="password" id="pass2" required placeholder="repeat password">
        <button type="submit" id="btn">Create Account</button>
    </form>
    <div class="msg" id="msg"></div>
</div>
<script>
async function register(e) {
    e.preventDefault();
    const user = document.getElementById('user').value.trim();
    const pass = document.getElementById('pass').value;
    const pass2 = document.getElementById('pass2').value;
    const msg = document.getElementById('msg');
    const btn = document.getElementById('btn');
    msg.className = 'msg';
    if (pass !== pass2) { msg.className = 'msg err'; msg.textContent = 'Passwords do not match'; return; }
    btn.disabled = true;
    btn.textContent = 'Creating...';
    try {
        const res = await fetch('/register', {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify({username: user, password: pass})
        });
        const data = await res.json();
        if (data.ok) {
            msg.className = 'msg ok';
            msg.textContent = 'Account created! You can login now.';
            document.getElementById('form').reset();
        } else {
            msg.className = 'msg err';
            msg.textContent = data.error || 'Registration failed';
        }
    } catch(err) {
        msg.className = 'msg err';
        msg.textContent = 'Server error';
    }
    btn.disabled = false;
    btn.textContent = 'Create Account';
}
</script>
</body>
</html>"""

class Handler(http.server.BaseHTTPRequestHandler):
    def do_GET(self):
        self.send_response(200)
        self.send_header("Content-Type", "text/html; charset=utf-8")
        self.end_headers()
        self.wfile.write(HTML.encode("utf-8"))

    def do_POST(self):
        if self.path != "/register":
            self.send_error(404)
            return
        length = int(self.headers.get("Content-Length", 0))
        body = json.loads(self.rfile.read(length))
        username = body.get("username", "").strip().lower()
        password = body.get("password", "")

        if not re.match(r'^[a-zA-Z0-9_-]{3,25}$', username):
            self._json(400, {"ok": False, "error": "Invalid username (3-25 chars, a-z 0-9 _ -)"})
            return
        if len(password) < 3 or len(password) > 40:
            self._json(400, {"ok": False, "error": "Password must be 3-40 characters"})
            return

        try:
            conn = mysql.connector.connect(host=DB_HOST, user=DB_USER, password=DB_PASS, database=DB_NAME)
            cur = conn.cursor()
            cur.execute("SELECT guid FROM accounts WHERE account = %s", (username,))
            if cur.fetchone():
                cur.close()
                conn.close()
                self._json(409, {"ok": False, "error": "Username already taken"})
                return
            cur.execute(
                "INSERT INTO accounts (account, pass, pseudo, email, question, reponse, level, vip, banned, "
                "lastIP, lastConnectionDate, bank, friends, enemy, cadeau, points, bankKamas) "
                "VALUES (%s, %s, %s, '', 'DELETE?', 'DELETE', 0, 1, 0, '', '', '', '', '', 0, 0, 0)",
                (username, password, username))
            conn.commit()
            cur.close()
            conn.close()
            print(f"[REG] Account created: {username}")
            self._json(200, {"ok": True})
        except Exception as e:
            print(f"[REG] Error: {e}")
            self._json(500, {"ok": False, "error": "Database error"})

    def _json(self, code, data):
        self.send_response(code)
        self.send_header("Content-Type", "application/json")
        self.end_headers()
        self.wfile.write(json.dumps(data).encode())

    def log_message(self, fmt, *args):
        print(f"[WEB] {args[0]}")

if __name__ == "__main__":
    server = http.server.HTTPServer(("0.0.0.0", 80), Handler)
    print("[REG] Registration server running on port 80")
    server.serve_forever()
