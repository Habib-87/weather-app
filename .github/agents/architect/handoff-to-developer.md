# Architect → Developer Handoff

- **Date:** 2026-06-03
- **From:** Architect Agent
- **To:** Developer Agent
- **Status:** BLOCKED — awaiting human approval of architect artifacts

---

## Gate Status

> ⛔ Developer Agent must NOT proceed until the following file is set to `status: APPROVED`:
> `.github/.ai/approvals/architect/approval.yml`

---

## Approved Artifacts (pending human sign-off)

| Artifact | Path | Status |
|----------|------|--------|
| ADR-001 | `.github/.ai/artifacts/architect/ADR-001-java21-migration-strategy.md` | IN_REVIEW |
| Architecture Spec | `.github/.ai/artifacts/architect/ARCHITECTURE-SPEC-java21.md` | IN_REVIEW |
| Wave 1 Implementation Plan | `.github/.ai/artifacts/architect/IMPLEMENTATION-PLAN-wave1.md` | IN_REVIEW |

---

## What Developer Agent Must Do (Wave 1 only)

1. Read `.github/.ai/orchestrator.md` and confirm gate is open.
2. Read the three architect artifacts listed above.
3. Read `.github/agents/developer/charter.md` and `.github/agents/developer/checklist.md`.
4. Produce:
   - `.github/.ai/artifacts/developer/DEV-TASK-BREAKDOWN-wave1.md`
   - `.github/.ai/artifacts/developer/CHANGE-PLAN-wave1.md`
5. Create `.github/agents/developer/handoff-to-human-review.md`.
6. Update `.github/.ai/state/workflow-state.yml` with developer status `IN_REVIEW`.
7. Request human approval.

---

## Key Constraints Passed From Architect

- Wave 1 changes are **`pom.xml` only** — no source code modifications.
- Branch: `wave1/java11`
- Only one file changes: `<java.version>` + Lombok version pin.
- All validation gates must pass before PR is raised.

---

## Contact

Human reviewer must set `approval.yml` → `status: APPROVED` to unblock the Developer Agent.

## Notes
Pending human approval. Do not proceed until `.ai/approvals/architect/approval.yaml` is `APPROVED`.