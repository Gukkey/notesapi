# Notes API

REST based API for note keeping and management.
Front end on https://github.com/shiyaamsunder/notesfrontend

---

## Local Setup

Clone the repo

```shell
git clone https://github.com/Gukkey/notesapi
```

Setup DB (PostgreSQL must be installed and running). This will create a separate DB and a user.

```powershell
cd notesapi
.\scripts\db_init.ps1
```

Create `application.yml` file under `src/main/resources`. Copy the structure from `application.yml.example`

Then start the application using the mvn command.
```shell
mvn spring-boot:run
```

---
