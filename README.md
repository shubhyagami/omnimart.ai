# OmniMart AI

> **Author:** [Shubh Kumar](https://github.com/shubhyagami)

[![Java 21](https://img.shields.io/badge/Java-21-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)](https://openjdk.org/)  
[![Spring Boot 3.3.x](https://img.shields.io/badge/Spring%20Boot-3.3.x-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)](https://spring.io/projects/spring-boot)  
[![NVIDIA Nemotron 3 Ultra](https://img.shields.io/badge/NVIDIA%20Nemotron%203-Ultra-76B900?style=for-the-badge&logo=nvidia&logoColor=white)](https://www.nvidia.com/)  
[![Docker Ready](https://img.shields.io/badge/Docker-Ready-46E3B7?style=for-the-badge&logo=docker&logoColor=black)](https://www.docker.com/)  
[![Brevo Transactional‑SMTP](https://img.shields.io/badge/Brevo-Transactional%20SMTP-0B99FF?style=for-the-badge&logo=brevo&logoColor=white)](https://www.brevo.com/)  
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow?style=for-the-badge)](LICENSE)

---  

## Quick Start

```bash
./mvnw spring-boot:run      # macOS / Linux
./mvnw.cmd spring-boot:run   # Windows
```

The service starts on **port 8080**.  
Open <http://localhost:8080> in a browser or call the REST endpoints.

> 💡 Want to use a database other than the default in‑memory H2?  
> Set the following environment variables before running:
> - `SPRING_DATASOURCE_URL`
> - `SPRING_DATASOURCE_USERNAME`
> - `SPRING_DATASOURCE_PASSWORD`

---

## Overview

OmniMart AI is a Spring Boot 3 microservice that:

- Provides a conversational shopping assistant with multi‑turn memory and tool‑based reasoning.  
- Hosts a hybrid recommendation engine that blends user behaviour, content signals, ratings and popularity.  
- Stores every AI interaction in a relational database, giving audit trails, replayability and safe rollback.  
- Supports three AI backends:
  - **NVIDIA Nemotron 3 Ultra** (requires an API key),
  - a local checkpoint,
  - a mock provider for testing.  
  The provider is chosen automatically and the system falls back if one is unavailable.

---

## Key Features

| Feature | Description |
|---------|-------------|
| **Conversational Assistant** | Multi‑turn chat with long‑term context and tool calls. |
| **Hybrid Recommendation** | Combines user preferences, browsing history, content relevance, ratings and popularity. |
| **Sentiment & Topic Mining** | Extracts actionable insights from product reviews. |
| **AI‑Generated Comparisons** | Generates side‑by‑side spec tables with an AI verdict. |
| **Procedural UI** | Neon doodles, magnetic cursor, click‑burst particles, glass‑morphic cards. |
| **Geolocation** | IP‑based city/state/country displayed on a responsive Leaflet map. |
| **Transactional Email** | Brevo integration for OTPs, receipts and account actions. |
| **Zero‑Hallucination Guardrails** | All data is fetched via secure service calls – raw SQL is never exposed. |

---

## High‑Level Architecture

```
┌──────────────────────┐
│  Web Layer            │  (React / Thymeleaf)
└──────────┬─────────────┘
           ▼
┌──────────────────────┐
│  Controller Layer    │  (Spring MVC)
└──────────┬─────────────┘
           ▼
┌──────────────────────┐
│  AI Orchestrator     │  (memory & tool selector)
└──────────┬─────────────┘
           ▼
┌──────────────────────┐
│  Tool Services       │  (lookup, profiling, comparison)
└──────────┬─────────────┘
           ▼
┌──────────────────────┐
│  Data Layer          │  (H2 / MySQL)
└──────────────────────┘
```

---

## Environment Variables

| Variable | Default | Purpose |
|----------|--------|---------|
| `PORT` | `8080` | HTTP listening port (overridden by Render). |
| `AI_PROVIDER` | `nvidia` | Choose `nvidia`, `local`, or `mock`. |
| `NVIDIA_API_KEYS` | `*required*` | Comma‑separated NVIDIA API keys. |
| `NVIDIA_MODEL` | `nvidia/nemotron-3-ultra-550b-a55b` | Identifier of the NVIDIA model. |
| `BREVO_API_KEY` | `*required*` | Brevo transactional‑email key. |
| `BREVO_SENDER_EMAIL` | `support@omnimart-ai.com` | Verified sender address. |
| `BREVO_SENDER_NAME` | `OmniMart AI` | Name shown in outgoing emails. |
| `SPRING_DATASOURCE_URL` | `jdbc:h2:mem:omnimart;DB_CLOSE_ON_EXIT=FALSE` | JDBC URL. |
| `SPRING_DATASOURCE_USERNAME` | `sa` | DB username. |
| `SPRING_DATASOURCE_PASSWORD` | `""` | DB password. |

> ⚙️ The application reads `src/main/resources/application.yml`. Create a copy in another project to override these defaults.

---

## Docker

```bash
# Build the image
docker build -t omnimart-ai:latest .

# Run the container
docker run -p 8080:8080 \
  -e AI_PROVIDER=nvidia \
  -e NVIDIA_API_KEYS=YOUR_KEY \
  -e BREVO_API_KEY=YOUR_BREVO_KEY \
  omnimart-ai:latest
```

The multi‑stage Dockerfile produces an image of about 80 MB.

---

## Deploy to Render (or any container‑friendly platform)

1. Push the repository to GitHub.  
2. In Render, create a new “Web Service” → *From GitHub* → choose this repo.  
3. Render picks up the `Dockerfile` automatically.  
4. Add the required environment variables (`AI_PROVIDER`, `NVIDIA_API_KEYS`, `BREVO_API_KEY`).  
5. Render exposes the service on `${PORT}`; the app will listen on that port automatically.

---

## Demo Accounts

| Role     | Email                | Password    | Access |
|----------|-----------------------|------------|--------|
| Customer | `user@omnimart.com`   | `password123` | Storefront, cart, AI assistant |
| Admin    | `admin@omnimart.com`  | `admin123` | Analytics, sentiment charts, admin Q&A |

> ⚠️ These accounts are for demonstration only. Do not use them in production.

---

## Running Tests

```bash
./mvnw test
```

Unit tests cover the core orchestrator logic and the AI tool abstraction.

---

## Contributing

Pull requests are welcome.  
For significant changes, open an issue first to discuss the approach.  
See the [CODE_OF_CONDUCT.md](CODE_OF_CONDUCT.md) for community guidelines.

---

## Changelog

* **v1.0.0 – 2026‑08‑20** – First stable release. Includes Spring Boot 3, NVIDIA Nemotron 3 Ultra integration, hybrid recommendation engine, zero‑hallucination guardrails, procedural UI, Docker multi‑stage build, Render blueprint, and demo accounts.

---

## License

MIT – see the [LICENSE](LICENSE) file.
