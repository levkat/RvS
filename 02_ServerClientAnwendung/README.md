# ServerClientAnwendung

## Aufgabestellung Checkliste

## TO FIX:
- [ ] FIX: server beenden (client alive)
- [ ] FIX: server beenden (client down)

### 1. Dienste

- [x] Zeit und Datum
  - [x] Get Time (TIME HH:mm:ss)
  - [x] Get Date (DATE dd.MM.yyyy)
- [x] Rechner
  - [x] ADD int1 int2 (SUM Ergebnis)
  - [x] SUB int1 int2 (DIFFERENCE Ergebnis)
  - [x] MUL int1 int2 (PRODUCT Ergebnis)
  - [x] DIV int1 int2 (QUOTIENT Ergebnis)
- [x] Echo/Didcard
  - [x] ECHO String (ECHO String)
  - [x] DISCARD String ()
- [x] PING (PONG)

### 2. Historie

- [x] HISTORY (alle Anfragen)
- [x] HISTORY (int)n (letzte n Anfragen)
- [x] ERROR Keine Historie vorhanden! (falls keine)
- [x] DISCARD (soll nicht in History auftauchen)

### 3. Fehlerhafte Anfrage vom Client

- [x] done

### 4. Server

- [x] den Port vom Nutzer entgegennehmen
- [x] Port korrekt (korrekter Datentyp und Port 2020)
- [x] ssocket.close()

### 5. Client

- [x] IP entgegennehmen
- [x] IP-check (soll: 127.0.0.1|localhost )
- [x] Port entgegennehmen
- [x] Port-check (soll: 2020)
- [x] promt '$'
- [x] socket.close()
- [x] FIX: handle server down before client
