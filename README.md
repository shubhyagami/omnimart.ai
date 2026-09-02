# OmniMart AI

![Java 21](https://img.shields.io/badge/Java-21-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)  
![Spring Boot 3.3.4](https://img.shields.io/badge/Spring%20Boot-3.3.4-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)  
![NVIDIA Nemotron 3‑Ultra](https://img.shields.io/badge/NVIDIA-Nemotron%203-Ultra-76B900?style=for-the-badge&logo=nvidia&logoColor=white)  
![Render Docker‑Ready](https://img.shields.io/badge/Render-Docker%20Ready-46E3B7?style=for-the-badge&logo=render&logoColor=black)  
![Brevo](https://img.shields.io/badge/Brevo-Transactional%20OTP-0B99FF?style=for-the-badge&logo=brevo&logoColor=white)  
![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg?style=for-the-badge)

**Architected by [Shubh Kumar](https://github.com/shubhyagami)**

---

## What is OmniMart AI?

OmniMart AI is a Spring Boot 3 application that powers a conversational shopping assistant, AI‑driven product recommendations, and an interactive UI.  
All AI responses are sourced directly from the database, ensuring no hallucinations. The system can switch between multiple AI providers (NVIDIA, a local model, or a mock) with automatic fallback.

---

## Key Features

- **Conversational Shopping Assistant** – powered by NVIDIA Nemotron 3 Ultra.  
- **Hybrid Recommendation Engine** – blends user preferences, browsing behaviour, content relevance, ratings, and popularity.  
- **Sentiment & Topic Mining** – extracts insights from user reviews.  
- **Procedural Canvas & Particle UI** – neon doodles, magnetic cursor ring, shopping‑bag morph, click‑burst particles.  
- **Geo‑Location & Mini‑Map** – IP‑based city, state, country, postal code displayed on a Leaflet map.  
- **Transactional Email & OTP** – Brevo integration for account actions and order receipts.  
- **AI‑Driven Product Comparison** – side‑by‑side table with hardware stats and an AI verdict banner.  
- **Zero‑Hallucination Guardrails** – data is retrieved via secure service calls, never raw SQL.

---

## Architecture

1. **Web Layer** – UI calls Spring Security‑protected REST endpoints.  
2. **Controller Layer** – routes requests to the AI orchestrator.  
3. **AI Orchestrator** – manages multi‑turn memory, tool selection, and provider fallback.  
4. **Tool Services** – safe operations such as lookup, profiling, comparison.  
5. **Data Layer** – persists data in H2 or MySQL.

---

## UI Highlights

- **Cosmos Wallpaper** – procedural neon doodles with 3‑layer parallax and glow.  
- **Interactive Cursor** – magnetic trailing ring, bag icon morph, click‑burst particles.  
- **Live Mini‑Map** – Leaflet radar marker, dark tiles, pulse rings, IP‑based location scanner.  
- **Glassmorphic Cards & Table** – 16 px backdrop‑blur, gradient borders, WCAG‑compliant contrast.

---

## Getting Started

### Prerequisites

- Java 21 (or newer)  
- Maven 3.9+ (wrapper `mvnw` is included)  
- Docker (optional but recommended)

### Run Locally

```bash
# Windows
.mvnw.cmd spring-boot:run

# macOS / Linux
./mvnw spring-boot:run
```

The app starts on **port 8080** (or `$PORT` when running on Render).

- Storefront: <http://localhost:8080>  
- H2 Console: <http://localhost:8080/h2-console>

### Docker

```bash
docker build -t omnimart-ai:latest .
docker run -p 8080:8080 -e AI_PROVIDER=nvidia omnimart-ai:latest
```

---

## Deploying to Render

1. Push the repo to GitHub.  
2. In Render click **New + → Blueprint** and select this repository.  
3. Render picks up the Dockerfile, creates the service, and prompts for the required environment variables.

---

### Manual Docker Deployment

1. Push the image to a registry.  
2. Create a Render Web Service, set the env vars, and expose `$PORT`.  
3. The service uses `server.port: ${PORT:8080}` to align with Render’s port assignment.

---

## Demo Accounts

| Role      | Email               | Password      | Access |
|-----------|---------------------|---------------|--------|
| Customer  | `user@omnimart.com` | `password123` | Storefront, cart, orders, AI assistant |
| Admin     | `admin@omnimart.com` | `admin123`  | Analytics, sentiment charts, admin AI Q&A |

Credentials are pre‑filled on `/login`.

---

## Environment Variables

| Variable           | Default                          | Description |
|--------------------|----------------------------------|-------------|
| `PORT`             | `8080`                           | HTTP port (Render overrides). |
| `AI_PROVIDER`      | `nvidia`                         | `nvidia`, `local`, or `mock`. |
| `NVIDIA_API_KEYS`  | *comma‑separated list*           | Pool of NVIDIA API keys. |
| `NVIDIA_MODEL`     | `nvidia/nemotron-3-ultra-550b-a55b` | Model identifier. |
| `BREVO_API_KEY`    | *configured key*                 | Brevo transactional email key. |
| `BREVO_SENDER_EMAIL` | `support@omnimart-ai.com`       | Verified sender address. |
| `BREVO_SENDER_NAME`  | `OmniMart AI`                   | Email sender name. |

---

## Changelog

- **v1.0.0 – 2026‑08‑20**  
  Initial release with Spring Boot 3, Java 21, NVIDIA Nemotron 3 Ultra integration, hybrid recommendation engine, zero‑hallucination guardrails, procedural UI toolkit, Docker multi‑stage build, Render blueprint, and demo accounts.

---

<div align="center">
**Designed & Crafted by [Shubh Kumar](https://github.com/shubhyagami)**
</div>
