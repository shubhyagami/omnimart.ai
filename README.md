# OmniMart AI

[![Java 21](https://img.shields.io/badge/Java-21-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)](https://openjdk.org/)  
[![Spring Boot 3.3.x](https://img.shields.io/badge/Spring%20Boot-3.3.x-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)](https://spring.io/projects/spring-boot)  
[![NVIDIA Nemotron 3 Ultra](https://img.shields.io/badge/NVIDIA%20Nemotron%203-Ultra-76B900?style=for-the-badge&logo=nvidia&logoColor=white)](https://www.nvidia.com/)  
[![Docker Ready](https://img.shields.io/badge/Docker-Ready-46E3B7?style=for-the-badge&logo=docker&logoColor=black)](https://www.docker.com/)  
[![Brevo Transactional‑SMTP](https://img.shields.io/badge/Brevo-Transactional%20SMTP-0B99FF?style=for-the-badge&logo=brevo&logoColor=white)](https://www.brevo.com/)  
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow?style=for-the-badge)](LICENSE)

> **Author:** [Shubh Kumar](https://github.com/shubhyagami)

---

## Quick Start

Prerequisites:

* JDK 21 (or later)
* Maven 3.9+
* Docker (optional, for containerized run)
* An NVIDIA API key if you want to use the Nemotron 3 Ultra provider

```bash
# Run with Maven Wrapper
./mvnw spring-boot:run            # macOS / Linux
./mvnw.cmd spring-boot:run        # Windows
```

The application starts on **port 8080** and is reachable at <http://localhost:8080>.

>💡 *If you need a database other than the in‑memory H2 instance, set the `SPRING_DATASOURCE_URL`, `SPRING_DATASOURCE_USERNAME`, and `SPRING_DATASOURCE_PASSWORD` environment variables before starting the app.*

---

## In a Nutshell

OmniMart AI is a Spring Boot 3 service that powers a conversational shopping assistant and a hybrid recommendation engine.  
All AI interactions are stored in a relational database, which provides:

* Auditing & replay
* Reduced hallucination risk
* Easy rollback of erroneous responses

The engine can talk to:

* **NVIDIA Nemotron 3 Ultra** – the flagship LLM (requires a key)
* **A local checkpoint** – for offline use
* **A mock provider** – for quick demos

The system will automatically fall back to an alternate provider if the current one is unavailable.

---

## Core Features

| Feature | What it does |
|---------|--------------|
| **Conversational Assistant** | Multi‑turn chat with long‑term memory, tool‑based reasoning, and NVIDIA Nemotron 3 Ultra |
| **Hybrid Recommendation Engine** | Combines user preferences, browsing history, content relevance, ratings, and popularity |
| **Sentiment & Topic Mining** | Extracts actionable insights from product reviews |
| **AI‑Generated Comparisons** | Produces side‑by‑side spec tables with an AI verdict |
| **Procedural UI** | Neon doodles, magnetic cursor, click‑burst particles, glass‑morphic cards |
| **Geolocation** | Shows IP‑based city/state/country on a responsive Leaflet map |
| **Transactional Email** | Brevo integration for OTPs, receipts, and account actions |
| **Zero‑Hallucination Guardrails** | All data is fetched through secure service calls – raw SQL is never exposed |

---

## Architecture

```
┌─────────────────────┐   HTTP(S)   ┌─────────────────────────────┐
│  Web Layer (React/ │────────────►│  Controller Layer (Spring MVC) │
│     Thymeleaf)     │             └───────┬──────────────────────┘
└─────────────────────┘                 │
                                  ┌─────▼─────────────────────┐
                                  │  AI Orchestrator          │
                                  │  (memory, tool selector) │
                                  └─────┬─────────────────────┘
                                  │
                                  ▼
                         ┌─────────────────────────┐
                         │  Tool Services           │
                         │  (lookup, profiling,    │
                         │   comparison)           │
                         └─────┬─────────────────┘
                               │
                               ▼
                         ┌──────────────────────┐
                         │  Data Layer             │
                         │  (H2 / MySQL)           │
                         └──────────────────────┘
```

*The diagram is intentionally high‑level to keep the focus on the main responsibilities.*

---

## Environment Variables

| Variable | Default | Purpose |
|----------|---------|---------|
| `PORT` | `8080` | HTTP listening port (overridden by Render) |
| `AI_PROVIDER` | `nvidia` | One of `nvidia`, `local`, or `mock` |
| `NVIDIA_API_KEYS` | **required** | Comma‑separated NVIDIA API keys |
| `NVIDIA_MODEL` | `nvidia/nemotron-3-ultra-550b-a55b` | Identifier of the NVIDIA model |
| `BREVO_API_KEY` | **required** | Brevo transactional‑email key |
| `BREVO_SENDER_EMAIL` | `support@omnimart-ai.com` | Verified sender address |
| `BREVO_SENDER_NAME` | `OmniMart AI` | Name shown in outgoing emails |
| `SPRING_DATASOURCE_URL` | `jdbc:h2:mem:omnimart;DB_CLOSE_ON_EXIT=FALSE` | JDBC URL |
| `SPRING_DATASOURCE_USERNAME` | `sa` | DB username |
| `SPRING_DATASOURCE_PASSWORD` | `""` | DB password |

>🛠️ *The application uses Spring Boot’s `application.yml`. For custom configurations, create `src/main/resources/application.yml` in a separate project and override these defaults.*

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

The image is built with a multi‑stage Dockerfile, keeping the runtime footprint to ~80 MB.

---

## Deploying to Render (or any container‑friendly platform)

1. Push the repo to GitHub.  
2. In Render, create a new “Web Service” → *From GitHub* → select this repo.  
3. Render will automatically detect the Dockerfile.  
4. Set the environment variables shown previously (especially `AI_PROVIDER`, `NVIDIA_API_KEYS`, `BREVO_API_KEY`).  
5. Render exposes the service on `${PORT}`; the app will listen on that port automatically.

---

## Demo Accounts

The login page pre‑populates the following demo credentials:

| Role     | Email                 | Password     | Access |
|----------|------------------------|-------------|--------|
| Customer | `user@omnimart.com`   | `password123` | Storefront, cart, AI assistant |
| Admin    | `admin@omnimart.com` | `admin123`   | Analytics, sentiment charts, admin Q&A |

>🔐 *These accounts are for demonstration only. Do not use them in a production environment.*

---

## Changelog

* **v1.0.0 – 2026‑08‑20** – First stable release. Includes Spring Boot 3, NVIDIA Nemotron 3 Ultra integration, hybrid recommendation engine, zero‑hallucination guardrails, procedural UI, Docker multi‑stage build, Render blueprint, and demo accounts.

---

## Running Tests

```bash
./mvnw test
```

Unit tests cover the core orchestrator logic and the AI tool abstraction.

---

## Contributing

Pull requests are welcome.  
Please open an issue to discuss major changes before starting work.  
See the [CODE_OF_CONDUCT.md](CODE_OF_CONDUCT.md) for community guidelines.

---

## License

MIT – see the [LICENSE](LICENSE) file.
