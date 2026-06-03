# ADR-001: Java 8 to Java 21 Migration Strategy

- **Status:** IN_REVIEW
- **Date:** 2026-06-03
- **Author:** Architect Agent
- **Version:** v0.1

---

## Context

The `weather-app` is a Spring Boot 2.7.18 application currently compiled and run on Java 8 (`<java.version>8</java.version>`). The project uses:

- **Build tool:** Maven
- **Framework:** Spring Boot 2.7.18 (end-of-OSS-support: Nov 2023)
- **Dependencies:** spring-boot-starter-web, spring-boot-starter-actuator, lombok, spring-boot-starter-test
- **HTTP client:** `RestTemplate` (synchronous, blocking)
- **Language level:** Java 8 idioms only (no `var`, no streams beyond basics, `switch` statements)

Java 8 is past its free public update lifecycle (Oracle). Java 21 is the current LTS (Long-Term Support) release with a support horizon through 2031. Spring Boot 3.x requires a minimum of Java 17.

The migration objective is to move this application to **Java 21** in a safe, step-wise fashion with human approval gates at each wave.

---

## Decision

Adopt a **three-wave incremental migration** strategy:

| Wave | Target | Key Change |
|------|--------|------------|
| Wave 1 | Java 11 | JDK upgrade, toolchain update, dependency baseline |
| Wave 2 | Java 17 | Spring Boot 3.x migration, `javax` → `jakarta` namespace |
| Wave 3 | Java 21 | Modern language features, virtual threads (optional) |

Each wave produces a working, deployable application. No wave is started until the previous wave is approved by a human reviewer.

---

## Alternatives Considered

1. **Big-bang direct upgrade (Java 8 → Java 21):** Rejected. High risk of undetected incompatibilities, difficult to bisect failures, and no intermediate stable checkpoints.
2. **Java 8 → Java 17 (two waves):** Viable but skips an important intermediate LTS. Java 11 provides a lower-risk first step given the volume of JVM/GC changes between 8 and 17.
3. **Stay on Java 8 with Spring Boot 2.7.x:** Not viable long-term. Spring Boot 2.7.x is end-of-life; security patches will not be backported.

---

## Consequences

**Positive:**
- Each wave is independently deployable and testable.
- Intermediate LTS stops provide a stable rollback target per wave.
- Spring Boot 3.x brings performance improvements, native image support, and continued security patching.
- Java 21 virtual threads (`Project Loom`) can replace `RestTemplate` with `WebClient` or structured concurrency in a future iteration.

**Negative:**
- Three separate upgrade cycles require coordinated developer effort.
- `javax.*` → `jakarta.*` package rename (Wave 2) requires code changes beyond build config.
- Lombok requires a compatible version per JDK level.

---

## Risks

| Risk | Likelihood | Impact | Mitigation |
|------|------------|--------|------------|
| Lombok annotation processor incompatible at intermediate JDK | Medium | High | Pin Lombok version per wave; validate with `mvn compile` at each step |
| Spring Boot 3.x `javax` → `jakarta` breakage | High | High | Wave 2 explicitly addresses namespace migration; compile gate before any runtime test |
| Reflection-based code (e.g. Jackson serialization) breaks on Java 17+ strong encapsulation | Low | Medium | Run full test suite after each wave; add `--add-opens` flags if required and document them |
| `RestTemplate` deprecation warning noise | Low | Low | Acceptable for Wave 1–2; migrate to `WebClient` or `RestClient` in Wave 3 optionally |
| CI/CD pipeline JDK mismatch | Medium | High | Update GitHub Actions / pipeline JDK version in sync with each wave |

---

## Rollback Considerations

**Trigger conditions for rollback:**
- `mvn clean install` fails after wave changes.
- Any production API contract test fails.
- Critical security vulnerability introduced by a new dependency.

**Rollback strategy per wave:**
- Each wave is a separate Git branch (`wave1/java11`, `wave2/java17`, `wave3/java21`).
- Rollback = revert to `main` (previous wave's approved branch).
- No wave is merged to `main` without human approval and a passing build.

---

## Approval

- **Status:** IN_REVIEW
- **Approved By:** _(pending)_
- **Approved On:** _(pending)_
- **Notes:** _(pending)_
