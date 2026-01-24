# CatchApp Backend

Backend för CatchApp byggd med **Jakarta EE 10**, paketerad som **WAR** och körs i **WildFly**.  
Databas: **PostgreSQL** (via Docker Compose).

---

## Tech stack
- Java 21
- Maven
- Jakarta EE 10
- WildFly
- PostgreSQL
- Docker / Docker Compose

---

## Krav
Följande måste vara installerat lokalt:

- JDK 21
- Maven 3.9+
- Docker + Docker Compose
- WildFly (Jakarta EE 10-kompatibel)

## Startguide
1. Installera Java 21, Maven, Docker och WildFly
2. Klona projektet
3. docker compose up -d
4. Starta WildFly
5. Skapa datasource: java:/jdbc/CatchDS
6. mvn clean package
7. Deploya WAR till WildFly
