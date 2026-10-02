[K[2m  [2mmodel z-ai/glm-5.3-flash failed, trying next...[0m[0m
[K[2m  [2mmodel deepseek-ai/deepseek-v4.1-flash failed, trying next...[0m[0m
# OmniMart AI

**Author:** [Shubh Kumar](https://github.com/shubhyagami)

![Java 21](https://img.shields.io/badge/Java-21-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot 3.3](https://img.shields.io/badge/Spring%20Boot-3.3.x-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-Ready-46E3B7?style=for-the-badge&logo=docker&logoColor=black)
![Brevo](https://img.shields.io/badge/Brevo-Transactional%20SMTP-0B99FF?style=for-the-badge&logo=brevo&logoColor=white)
![License](https://img.shields.io/badge/License-MIT-yellow?style=for-the-badge)

---

## Overview

OmniMart AI is a Spring Boot micro‑service that powers a conversational shopping assistant.  
Key capabilities include:

- Multi‑turn dialogue with long‑term context  
- LLM‑powered tool calls (product lookup, comparison, profiling)  
- Hybrid recommendation engine (behavior, content, ratings, popularity)  
- Sentiment & topic extraction from reviews  
- AI‑generated product specification cards  
- Procedural UI components (neon doodles, magnetic cursor, glass‑morphic cards)  
- IP‑based geolocation with Leaflet map integration  
- Brevo transactional email (OTPs, receipts)  
- Guardrails that route external calls through secure services to reduce hallucinations  
- Persisted interactions in a relational database for audit and replay  

The service supports three AI backends:

| Backend | Description |
|---------|-------------|
| `nvidia` | NVIDIA Nemotron 3 Ultra |
| `local` | A locally‑hosted checkpoint |
| `mock` | Deterministic mock responses (for testing) |

---

## Getting Started

```bash
# Clone the repository
git clone https://github.com/shubhyagami/omnimart.ai
cd omnimart.ai

# Build and run locally (Java 21, Maven)
./mvnw spring-boot:run
```

The application starts on **http://localhost:8080**.  
REST API endpoints are prefixed with `/api/...`, while the UI is served from the root path.

---

## Docker (Production‑Ready)

```bash
docker build -t omnimart-ai .
docker run -d -p 8080:8080 \
  -e AI_PROVIDER=nvidia \
  -e NVIDIA_API_KEYS=YOUR_KEY \
  -e BREVO_API_KEY=YOUR_BREVO_KEY \
  omnimart-ai
```

> **Tip:** Replace the placeholders with your actual credentials. Store secrets in a `.env` file, Docker secrets, or a cloud secret manager – never commit them.

---

## Configuration

| Variable | Default (application.yml) | Notes |
|-----------|---------------------------|-------|
| `PORT` | `8080` | HTTP port (useful on Render, Fly.io, etc.) |
| `AI_PROVIDER` | `nvidia` | `nvidia`, `local`, or `mock` |
| `NVIDIA_API_KEYS` | **required** | Comma‑separated keys (`key1,key2`) |
| `NVIDIA_MODEL` | `nvidia/nemotron-3-ultra-550b-a55b` | Model ID |
| `BREVO_API_KEY` | **required** | Brevo transactional email key |
| `BREVO_SENDER_EMAIL` | `support@omnimart-ai.com` | Sender email |
| `BREVO_SENDER_NAME` | `OmniMart AI` | Sender display name |
| `SPRING_DATASOURCE_URL` | `jdbc:h2:mem:omnimart;DB_CLOSE_ON_EXIT=FALSE` | JDBC URL |
| `SPRING_DATASOURCE_USERNAME` | `sa` | DB username |
| `SPRING_DATASOURCE_PASSWORD` | `""` | DB password |

> **Security:** Do not commit secrets to the repository. Use a `.env` file, Docker secrets, or a cloud secret manager.

---

## Architecture Overview

```
┌───────────────┐
│   React / UI  │
└───────▲───────┘
        │
┌───────▼───────┐
│ REST Controllers │
└───────▲───────┘
        │
┌───────▼───────┐
│ AI Orchestrator │ (sessions, tool dispatch)
└───────▲───────┘
        │
┌───────▼───────┐
│ Tool Services   │ (product lookup, comparison, profiling)
└───────▲───────┘
        │
┌───────▼───────┐
│ Database       │ (H2 in‑memory / MySQL)
└────────────────┘
```

---

## Demo Accounts

| Role     | Email               | Password      | Permissions |
|----------|---------------------|---------------|-------------|
| Customer | `user@omnimart.com` | `password123` | Storefront, cart, AI assistant |
| Admin    | `admin@omnimart.com` | `admin123`   | Analytics, sentiment charts |

*These accounts are for demonstration only.*

---

## Running Tests

```bash
./mvnw test
```

The test suite covers orchestrator logic, AI tool abstraction, and integration points.

---

## Contributing

Pull requests are welcome. For substantial changes, open an issue first to discuss the idea.  
Please follow the style guidelines in the [CODE_OF_CONDUCT.md](CODE_OF_CONDUCT.md).

---

## Changelog

| Version   | Date       | Highlights |
|-----------|------------|------------|
| **v1.0.0** | 2026‑08‑20 | Initial stable release – Spring Boot 3, NVIDIA Nemotron 3 Ultra, hybrid recommendation engine, zero‑hallucination guardrails, procedural UI, Docker support, Render deployment guide, demo accounts |

---

## License

MIT – see the [LICENSE](LICENSE) file.
