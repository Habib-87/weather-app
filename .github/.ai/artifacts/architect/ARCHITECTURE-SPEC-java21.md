# Architecture Spec: Java 8 → 21 Migration — weather-app

- **Status:** IN_REVIEW
- **Date:** 2026-06-03
- **Author:** Architect Agent
- **Version:** v0.1

---

## 1. Current Baseline

| Attribute | Value |
|-----------|-------|
| Runtime | Java 8 (OpenJDK / Eclipse Temurin 8) |
| Build system | Maven (spring-boot-maven-plugin 2.7.18) |
| Framework | Spring Boot 2.7.18 |
| Web layer | Spring MVC (`@RestController`, `RestTemplate`) |
| Serialization | Jackson (via spring-boot-starter-web) |
| Boilerplate | Lombok (`@Data`, `@Builder`, `@Slf4j`) |
| Test framework | JUnit 5 via spring-boot-starter-test |
| Deployment model | Standalone fat JAR (embedded Tomcat) |
| External API | Open-Meteo (REST over HTTPS, no auth) |

### Source structure
```
src/main/java/com/example/weatherapp/
  WeatherAppApplication.java        ← @SpringBootApplication entry point
  controller/WeatherController.java ← GET /api/weather?city=
  service/WeatherService.java       ← orchestration + WMO code mapping
  client/GeocodingClient.java       ← Open-Meteo geocoding HTTP call
  client/WeatherClient.java         ← Open-Meteo forecast HTTP call
  model/{GeocodingResponse, OpenMeteoResponse, WeatherResponse, ErrorResponse}
  config/AppConfig.java             ← RestTemplate @Bean
  exception/{CityNotFoundException, WeatherServiceException, GlobalExceptionHandler}
```

---

## 2. Target Architecture (Java 21)

| Attribute | Target Value |
|-----------|-------------|
| Runtime | Java 21 LTS (Eclipse Temurin 21) |
| Build system | Maven (spring-boot-maven-plugin 3.x) |
| Framework | Spring Boot 3.3.x |
| Web layer | Spring MVC (unchanged API surface) |
| Serialization | Jackson 2.17.x (auto-managed by Boot 3.x) |
| Boilerplate | Lombok 1.18.32+ (Java 21 compatible) |
| Test framework | JUnit 5 (unchanged) |
| Deployment model | Standalone fat JAR (embedded Tomcat 10.x) |
| Namespace | `jakarta.*` (replaces `javax.*`) |

**Optional enhancements in Wave 3 (not required for migration):**
- Replace `RestTemplate` with `RestClient` (Spring 6.1 fluent API)
- Evaluate Project Loom virtual threads (`spring.threads.virtual.enabled=true`)

---

## 3. Compatibility Matrix

| Component | Current | Wave 1 (Java 11) | Wave 2 (Java 17 + Boot 3) | Wave 3 (Java 21) | Risk | Required Action |
|-----------|---------|------------------|---------------------------|------------------|------|-----------------|
| JDK | 8 | 11 | 17 | 21 | Low | Update `pom.xml` `java.version`, CI toolchain |
| Spring Boot | 2.7.18 | 2.7.18 | 3.3.x | 3.3.x | High (Wave 2) | Bump parent POM; resolve `jakarta` namespace |
| Lombok | (managed) | 1.18.30 | 1.18.32 | 1.18.32 | Medium | Explicit version pin per wave |
| Jackson | (managed) | (managed) | (managed) | (managed) | Low | No changes needed |
| `javax.servlet` | yes | yes | **jakarta.servlet** | jakarta.servlet | High (Wave 2) | Rename imports in `GlobalExceptionHandler` |
| `RestTemplate` | yes | yes | yes (deprecated) | yes (deprecated) | Low | Suppress warnings; optional migration in Wave 3 |
| JUnit 5 | yes | yes | yes | yes | None | No action needed |
| Embedded Tomcat | 9.x | 9.x | **10.x** | 10.x | Low | Managed by Boot parent |

---

## 4. Non-Functional Requirements

| NFR | Requirement | Verification |
|-----|-------------|--------------|
| **Performance** | No regression in API response latency after migration | Smoke test `GET /api/weather?city=London` before and after each wave |
| **Reliability** | Zero breaking changes to public API contract (`/api/weather`) | API contract test in CI |
| **Security** | No new CVEs introduced at each wave | `mvn dependency:analyze` + OWASP dependency-check |
| **Operability** | Actuator endpoints (`/actuator/health`) remain functional | Integration test after each wave |
| **Build repeatability** | Clean build passes with `mvn clean install` at every wave | CI gate |

---

## 5. Migration Waves

### Wave 1 — Java 11 (toolchain only)
- Bump `<java.version>` from `8` to `11` in `pom.xml`
- Pin Lombok to `1.18.30`
- Validate: `mvn clean install`, run application, smoke-test API
- Branch: `wave1/java11`
- **No Spring Boot version change. No source code changes.**

### Wave 2 — Java 17 + Spring Boot 3.x (breaking namespace change)
- Bump `<java.version>` to `17`
- Bump Spring Boot parent to `3.3.x`
- Replace all `javax.*` imports with `jakarta.*`
  - Affected file: `GlobalExceptionHandler.java` (`javax.servlet.http.HttpServletRequest`)
- Pin Lombok to `1.18.32`
- Validate: `mvn clean install`, actuator health, API smoke test
- Branch: `wave2/java17-boot3`

### Wave 3 — Java 21 (LTS target)
- Bump `<java.version>` to `21`
- Optionally enable virtual threads: `spring.threads.virtual.enabled=true`
- Validate: `mvn clean install`, full test suite, performance check
- Branch: `wave3/java21`

---

## 6. Risks & Mitigations

| # | Risk | Likelihood | Impact | Mitigation |
|---|------|------------|--------|------------|
| R1 | Lombok incompatibility at new JDK | Medium | Build failure | Pin version per wave; test `mvn compile` first |
| R2 | `javax` → `jakarta` compile errors | High | Build failure | Wave 2 is dedicated to this; complete list of affected files identified upfront |
| R3 | Hidden reflection usage breaking on Java 17 strong encapsulation | Low | Runtime failure | Run full test suite; add `--add-opens` only if needed and document |
| R4 | CI/CD pipeline uses hardcoded JDK 8 | Medium | Build failure in CI | Update pipeline JDK action/tool at start of each wave |
| R5 | Open-Meteo API call behavior changes | Very Low | Functional regression | No API changes in migration scope; existing tests are sufficient |

---

## 7. Rollout & Rollback

**Rollout strategy:**
- Each wave is a Git feature branch.
- Wave branch merged to `main` only after human approval of artifacts and passing CI.
- No concurrent waves — one wave at a time.

**Rollback triggers:**
- `mvn clean install` failure after applying wave changes.
- API smoke test returns non-200 for `GET /api/weather?city=London`.
- Any CVE introduced with severity HIGH or CRITICAL.

**Rollback steps:**
1. Do not merge the wave branch.
2. Revert to `main` (previous wave's approved state).
3. File a revision note in the artifact and resubmit for review.

---

## Approval

- **Status:** IN_REVIEW
- **Approved By:** _(pending)_
- **Approved On:** _(pending)_
- **Notes:** _(pending)_
