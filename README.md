# OmniMart AI

[![Java 21](https://img.shields.io/badge/Java-21-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)](https://openjdk.org/)  
[![Spring Boot 3.3.x](https://img.shields.io/badge/Spring%20Boot-3.3.x-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)](https://spring.io/projects/spring-boot)  
[![NVIDIA Nemotron 3 Ultra](https://img.shields.io/badge/NVIDIA-Nemotron%203-Ultra-76B900?style=for-the-badge&logo=nvidia&logoColor=white)](https://www.nvidia.com/)  
[![Docker Ready](https://img.shields.io/badge/Docker-Ready-46E3B7?style=for-the-badge&logo=docker&logoColor=black)](https://www.docker.com/)  
[![Brevo Transactional Email](https://img.shields.io/badge/Brevo-Transactional%20SMTP-0B99FF?style=for-the-badge&logo=brevo&logoColor=white)](https://www.brevo.com/)  
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow?style=for-the-badge)](LICENSE)

> **Author:** [Shubh Kumar](https://github.com/shubhyagami)

---

## Overview

OmniMart AI is a Spring Boot 3 application that powers a conversational shopping assistant and an AI‑driven recommendation engine.  
AI responses are persisted in a relational database so the service can audit or replay interactions, avoiding hallucinations that can arise from raw‑SQL queries.  
The application can use NVIDIA’s Nemotron 3 Ultra, a local model, or a mock provider and will automatically fall back if a provider is unavailable.

---

## Features

- **Conversational Assistant** – Multi‑turn chat powered by NVIDIA Nemotron 3 Ultra, with memory and tool‑based reasoning.  
- **Recommendation Engine** – Combines user preferences, browsing history, content relevance, ratings and popularity to surface relevant products.  
- **Sentiment & Topic Mining** – Extracts actionable insights from user reviews.  
- **AI‑Generated Comparisons** – Produces side‑by‑side specs tables with an AI verdict banner.  
- **Procedural UI** – Neon doodles, magnetic cursor, click‑burst particles, glass‑morphic cards.  
- **Geolocation** – Shows IP‑based city/state/country on a responsive Leaflet map.  
- **Transactional Email** – Brevo integration for OTPs, receipts and account actions.  
- **Zero‑Hallucination Guardrails** – All data is fetched through secure service calls; no raw SQL is exposed.

---

## Architecture

```text
┌─────────────────────┐   HTTP(S)   ┌───────────────────────┐
│  Web Layer (UI)     │───────────►│  Controller Layer      │
│  (React/Thymeleaf) │            │  (Spring MVC)          │
└─────────────────────┘            └───────┬────────────────┘
                                     │
                               ┌───────▼────────────────┐
                               │  AI Orchestrator        │
                               │  (Memory, tool selector)│
                               └───────┬────────────────┘
                                     │
                                    ▼
                               ┌────────────────────────────┐
                               │  Tool Services              │
                               │  (lookup, profiling, comparison)│
                               └───────┬─────────────────────┘
                                     │
                                     ▼
                               ┌─────────────────────┐
                               │  Data Layer          │
                               │  (H2 / MySQL)        │
                               └─────────────────────┘
```

---

## UI Highlights

| Feature | Description |
|---------|-------------|
| Cosmos Wallpaper | Three‑layer neon doodle parallax with glow |
| Interactive Cursor | Magnetic ring, bag‑icon morphing, click‑burst particles |
| Live Mini‑Map | Leaflet dark tiles, pulse rings, IP‑based location |
| Glass‑morphic Cards & Table | 16 px backdrop‑blur, gradient borders, WCAG‑compliant contrast |

---

## Getting Started

### Prerequisites

| Tool   | Minimum Version |
|--------|------------------|
| Java   | 21+ |
| Maven  | 3.9+ (wrapper `mvnw` is included) |
| Docker | Optional, recommended for CI/CD |

### Quick start

```bash
# On Windows
./mvnw.cmd spring-boot:run

# On macOS / Linux
./mvnw spring-boot:run
```

The application listens on `8080` by default.  
- Storefront: <http://localhost:8080>  
- H2 Console: <http://localhost:8080/h2-console>

### Docker

```bash
# Build
docker build -t omnimart-ai:latest .

# Run
docker run -p 8080:8080 -e AI_PROVIDER=nvidia omnimart-ai:latest
```

---

## Deploy to Render

1. Push the repo to GitHub.  
2. In Render, create a new service → **From GitHub** and select this repository.  
3. Render will detect the Dockerfile, build the image, and ask for the environment variables below.  
4. The service is automatically exposed on `${PORT}`; the app uses `server.port: ${PORT:8080}`.

---

## Environment Variables

| Variable           | Default                     | Description |
|--------------------|-----------------------------|-------------|
| `PORT`             | `8080`                      | HTTP port (overridden by Render). |
| `AI_PROVIDER`      | `nvidia`                    | `nvidia`, `local`, or `mock`. |
| `NVIDIA_API_KEYS`  | **required**                | Comma‑separated NVIDIA API keys. |
| `NVIDIA_MODEL`     | `nvidia/nemotron-3-ultra-550b-a55b` | Identifier for the NVIDIA model. |
| `BREVO_API_KEY`    | **required**                | Brevo transactional email key. |
| `BREVO_SENDER_EMAIL` | `support@omnimart-ai.com` | Verified sender address. |
| `BREVO_SENDER_NAME` | `OmniMart AI`               | Sender name used in outgoing emails. |

---

## Demo Accounts

> The login form pre‑populates demo credentials.

| Role     | Email                | Password      | Access |
|----------|---------------------|---------------|--------|
| Customer | `user@omnimart.com` | `password123` | Storefront, cart, AI assistant |
| Admin    | `admin@omnimart.com`| `admin123`  | Analytics, sentiment charts, admin AI Q&A |

---

## Changelog

- **v1.0.0 – 2026‑08‑20** – Initial release: Spring Boot 3, NVIDIA Nemotron 3 Ultra, hybrid recommendation engine, zero‑hallucination guardrails, procedural UI toolkit, Docker multi‑stage build, Render blueprint, demo accounts.

---

## Contributing

Pull requests are welcome.  
Please open an issue before submitting large changes.  
See the [CODE_OF_CONDUCT.md](CODE_OF_CONDUCT.md) for guidelines.

---

## License

MIT – see the [LICENSE](LICENSE) file.
