# Multi-Agent Orchestrator (v1)

## Objective
Human-gated workflow for Java 8 -> 21 migration with:
1. Architect Agent
2. Developer Agent

## Global Rules
- No stage proceeds without previous stage approval.
- Every artifact must include metadata:
  - Title
  - Author (Agent)
  - Date
  - Version
  - Status: `DRAFT | IN_REVIEW | APPROVED`
- Human reviewer is the only approver.
- Handoffs must reference approved artifact paths.

## Stage Order
- Stage 1: Architect
- Stage 2: Developer

## Gate Rules
Developer can start only if:
- `.ai/approvals/architect/approval.yaml` => `status: APPROVED`
- Architect required artifacts exist

## Required Architect Outputs
- `.ai/artifacts/architect/ADR-001-java21-migration-strategy.md`
- `.ai/artifacts/architect/ARCHITECTURE-SPEC-java21.md`
- `.ai/artifacts/architect/IMPLEMENTATION-PLAN-wave1.md`
- `.ai/agents/architect/handoff-to-developer.md`

## Required Developer Outputs
- `.ai/artifacts/developer/DEV-TASK-BREAKDOWN-wave1.md`
- `.ai/artifacts/developer/CHANGE-PLAN-wave1.md`
- `.ai/agents/developer/handoff-to-human-review.md`

## Approval File Format
```yaml
status: IN_REVIEW | APPROVED | REJECTED
approved_by: "<name>"
approved_on: "YYYY-MM-DD"
notes: "<review notes>"
```

## Rejection Handling
- If `REJECTED`, agent must create a revision section in the same artifact and resubmit with status `IN_REVIEW`.