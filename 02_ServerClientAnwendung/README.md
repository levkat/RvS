# ServerClientAnwendung

## Aufgabestellung Checkliste

### 1. Dienste

- [ ] Zeit und Datum
    - [ ] Get Time (TIME HH:mm:ss)
    - [ ] Get Date (DATE dd.MM.yyyy)
- [ ] Rechner
    - [ ] ADD int1 int2 (SUM Ergebnis)
    - [ ] SUB int1 int2 (DIFFERENCE Ergebnis)
    - [ ] MUL int1 int2 (PRODUCT Ergebnis)
    - [ ] DIV int1 int2 (QUOTIENT Ergebnis)
- [ ] Echo/Didcard
    - [ ] ECHO String (ECHO String)
    - [ ] DISCARD String ()
- [ ] PING (PONG)

### 2. Historie

- [ ] HISTORY (alle Anfragen)
- [ ] HISTORY (int)n (letzte n Anfragen)
- [ ] ERROR Keine Historie vorhanden! (falls keine)
- [ ] DISCARD (soll nicht in History auftauchen)

### 3. Fehlerhafte Anfrage vom Client

- [x] done

### 4. Server

- [ ] den Port vom Nutzer entgegennehmen
- [ ] Port korrekt (korrekter Datentyp und Port 2020)
- [ ] ssocket.clos()

### 5. Client

- [ ] IP entgegennehmen
- [ ] IP-check (soll: 127.0.0.1|localhost )
- [ ] Port entgegennehmen
- [ ] Port-check (soll: 2020)
- [ ] promt '$'
- [ ] socket.close()