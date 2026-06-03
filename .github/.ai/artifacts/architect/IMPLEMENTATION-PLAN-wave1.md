# Implementation Plan — Wave 1: Java 8 → Java 11

- **Status:** IN_REVIEW
- **Date:** 2026-06-03
- **Author:** Architect Agent
- **Version:** v0.1

---

## Scope

Wave 1 upgrades the JDK compilation target from Java 8 to Java 11. This is a **toolchain-only** wave — no source code changes, no Spring Boot version change, no API changes.

---

## Out of Scope

- Spring Boot version upgrade (Wave 2)
- `javax` → `jakarta` namespace migration (Wave 2)
- Java 17 or Java 21 features (Wave 3)
- Any functional changes to the application logic
- Docker / containerisation changes

---

## Work Breakdown

### Task 1 — Discovery & Readiness

| # | Action | Owner | Notes |
|---|--------|-------|-------|
| 1.1 | Verify Java 11 JDK is installed locally (`java -version`) | Developer | Use Eclipse Temurin 11 |
| 1.2 | Confirm Maven wrapper or local Maven version ≥ 3.8 | Developer | `mvn -version` |
| 1.3 | Create Git branch `wave1/java11` from `main` | Developer | All Wave 1 changes on this branch |

### Task 2 — Build & Toolchain Changes

| # | File | Change | Notes |
|---|------|--------|-------|
| 2.1 | `pom.xml` | Change `<java.version>8</java.version>` → `<java.version>11</java.version>` | Single property change |
| 2.2 | `pom.xml` | Add explicit Lombok version `<lombok.version>1.18.30</lombok.version>` in `<properties>` | Ensures compatibility with Java 11 annotation processor |
| 2.3 | `pom.xml` | Update Lombok dependency to use `${lombok.version}` | Consistent version management |

### Task 3 — Dependency Validation

| # | Action | Command | Pass Criteria |
|---|--------|---------|---------------|
| 3.1 | Resolve all dependencies cleanly | `mvn dependency:resolve` | No resolution errors |
| 3.2 | Check for outdated/incompatible dependencies | `mvn versions:display-dependency-updates` | Review output; no action required in Wave 1 |
| 3.3 | Check for OWASP CVEs (baseline) | `mvn org.owasp:dependency-check-maven:check` | Document any existing CVEs as baseline; no blocker |

### Task 4 — Compatibility Verification

| # | Action | Command | Pass Criteria |
|---|--------|---------|---------------|
| 4.1 | Compile with Java 11 | `mvn clean compile` | Zero compilation errors or warnings |
| 4.2 | Run unit/integration tests | `mvn test` | All tests pass (`BUILD SUCCESS`) |
| 4.3 | Produce fat JAR | `mvn clean package -DskipTests` | JAR created in `target/` |
| 4.4 | Start application and verify | `java -jar target/weather-app-*.jar` | Application starts, `Started WeatherAppApplication` in logs |
| 4.5 | Smoke test API | `curl "http://localhost:8080/api/weather?city=London"` | HTTP 200 with JSON weather payload |
| 4.6 | Verify actuator | `curl http://localhost:8080/actuator/health` | `{"status":"UP"}` |

### Task 5 — Documentation & Handoff

| # | Action |
|---|--------|
| 5.1 | Update this plan's status to `IN_REVIEW` (done by Architect Agent) |
| 5.2 | Developer confirms all validation gates pass |
| 5.3 | Developer raises PR for `wave1/java11` → `main` |
| 5.4 | Human reviewer approves PR and updates `approval.yml` |

---

## Dependencies

- Java 11 JDK installed on developer machine and CI runner
- Git branch `wave1/java11` created before any changes
- All Wave 1 changes are isolated to `pom.xml` only

---

## Validation Gates

| Gate | Command | Pass Criteria |
|------|---------|---------------|
| **Build gate** | `mvn clean install` | `BUILD SUCCESS`, zero test failures |
| **Smoke test gate** | `curl http://localhost:8080/api/weather?city=Berlin` | HTTP 200 |
| **Actuator gate** | `curl http://localhost:8080/actuator/health` | `{"status":"UP"}` |
| **Security gate** | OWASP dependency-check | No new HIGH/CRITICAL CVEs vs baseline |

---

## Exit Criteria

- [ ] `pom.xml` has `<java.version>11</java.version>`
- [ ] `pom.xml` has explicit Lombok 1.18.30 version pin
- [ ] `mvn clean install` passes on Java 11
- [ ] Application starts successfully on Java 11
- [ ] API smoke test passes (`/api/weather?city=London` returns 200)
- [ ] No new CVEs vs Wave 0 baseline
- [ ] PR raised for `wave1/java11` → `main`
- [ ] Human reviewer has approved

---

## Approval

- **Status:** IN_REVIEW
- **Approved By:** _(pending)_
- **Approved On:** _(pending)_
- **Notes:** _(pending)_
