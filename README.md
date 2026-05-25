# NHS-prescription-tracker-system
Real-time NHS GP prescription tracking system built with Spring Boot microservices, GraphQL and RethinkDB. Based on direct GP surgery operational experience.

# Why I built this

I have worked as a higher administrator in a UK GP surgery for over four years. In that time I have managed prescription workflows daily — coordinating between receptionists, prescription clerks, an on-site pharmacist, and GPs.

# The problems I see repeatedly:

- Patients requesting prescriptions too early, with no automated check
- Prescriptions going missing between request and collection
- GPs signing without adequate review time because requests pile up invisibly
- Patients calling reception repeatedly asking "is my prescription ready?"
- No single view of where a prescription is in the workflow at any moment

These are not edge cases. They happen every week in every surgery I am aware of. Current systems like SystmOne handle clinical records well but do not give staff a real-time operational view of prescription status across the whole workflow.

I built this system to solve that.

---

## What it does

When a patient requests a repeat prescription, the system:

1. Checks whether they are eligible (72-hour rule — NHS standard)
2. Flags early requests for GP decision rather than rejecting automatically
3. Routes the request through the correct workflow stage
4. Notifies the right person at each stage in real time
5. Gives every role — patient, clerk, GP, pharmacist, admin — a live view
   of exactly where their prescription is

The moment a prescription status changes, RethinkDB's changefeed pushes the update instantly to every connected client. No polling. No refreshing. No phone calls asking "is it ready yet?"

---

## Prescription workflow
Patient requests
↓
Early request check (eligible after 21 days)
↓
REQUESTED → PENDING_GP_REVIEW → SIGNED_BY_GP
↓
SENT_TO_PHARMACY → READY_TO_COLLECT → COMPLETED
At any stage → REJECTED or QUERY_RAISED

---

## Technical decisions

**Why RethinkDB?**
Standard databases require the application to poll for changes. RethinkDB pushes changes to the application the moment they happen.
For a prescription workflow where multiple staff members need to see status changes instantly, this is architecturally the right choice.

**Why GraphQL?**
Different roles need different data. A patient needs their own prescriptions. A GP needs prescriptions pending their review. A pharmacist needs prescriptions sent to their pharmacy. GraphQL lets each client query exactly what it needs without multiple REST endpoints.

**Why microservices?**
Each concern is separated — authentication, prescription workflow, and notifications are independent services. This mirrors how the workflow actually operates: different people handle different stages.

---

## Architecture
Patient requests
↓
Early request check (eligible after 21 days)
↓
REQUESTED → PENDING_GP_REVIEW → SIGNED_BY_GP
↓
SENT_TO_PHARMACY → READY_TO_COLLECT → COMPLETED
At any stage → REJECTED or QUERY_RAISED

---

## Technical decisions

**Why RethinkDB?**
Standard databases require the application to poll for changes. RethinkDB pushes changes to the application the moment they happen.
For a prescription workflow where multiple staff members need to see status changes instantly, this is architecturally the right choice.

**Why GraphQL?**
Different roles need different data. A patient needs their own prescriptions. A GP needs prescriptions pending their review. A pharmacist needs prescriptions sent to their pharmacy. GraphQL lets each client query exactly what it needs without multiple REST endpoints.

**Why microservices?**
Each concern is separated — authentication, prescription workflow, and notifications are independent services. This mirrors how the workflow actually operates: different people handle different stages.

---

## Architecture
Client (Patient / GP / Clerk / Pharmacist / Admin)
↓
API Gateway (8080)
JWT validation + role routing
↓
┌───────────────────────────────────────────────┐
│                                               │
Auth Service      Prescription Service     Notification Service
(8081)            (8082)                   (8083)
Patient/GP/       Request tracking         Real-time alerts
Clerk/Pharmacist  72-hour rule check       to right person
signup & login    Status management        at each stage
JWT tokens        Real-time stream
Full audit trail

---

## Roles

| Role | Permissions |
|---|---|
| PATIENT | Request prescription, view own status |
| CLERK | Process requests, assign to GP |
| GP | Review early requests, sign prescriptions |
| PHARMACIST | Mark ready to collect, mark completed |
| ADMIN | Full access across all workflows |

---

## Tech stack

- Java 21
- Spring Boot 3.2.5
- Spring for GraphQL
- Spring WebFlux (reactive)
- RethinkDB 2.4.4
- JWT (JJWT 0.12.6)
- BCrypt password hashing
- Maven

---

## Running locally

**Prerequisites:**
- Java 21
- Maven
- RethinkDB running on localhost:28015

**Start each service:**
```bash
# Auth service
cd auth-service && mvn spring-boot:run

# Prescription service  
cd prescription-service && mvn spring-boot:run

# Notification service
cd notification-service && mvn spring-boot:run

# API Gateway
cd api-gateway && mvn spring-boot:run
```

GraphQL playground available at:
- Auth: http://localhost:8081/graphiql
- Prescriptions: http://localhost:8082/graphiql

---

## Project status

🔨 In active development

- [x] Project architecture and design
- [ ] Auth service — signup/login for all roles
- [ ] Prescription service — request and tracking
- [ ] Early request checker — 72-hour eligibility rule
- [ ] Real-time status streaming
- [ ] Notification service
- [ ] API Gateway
- [ ] Deployment

---

## Background

I am a higher administrator from India, based in the UK, with over four years of experience managing GP surgery operations. I am building this system to combine my clinical administration knowledge with software engineering, with the goal of contributing to NHS digital infrastructure modernisation.

---
