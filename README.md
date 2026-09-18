# OmniMart AI

> **Author:** [Shubh Kumar](https://github.com/shubhyagami)

[![Java 21](https://img.shields.io/badge/Java-21-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)](https://openjdk.org/)  
[![Spring Boot 3.3.x](https://img.shields.io/badge/Spring%20Boot-3.3.x-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)](https://spring.io/projects/spring-boot)  
[![NVIDIA Nemotron 3 Ultra](https://img.shields.io/badge/NVIDIA%20Nemotron%203-Ultra-76B900?style=for-the-badge&logo=nvidia&logoColor=white)](https://www.nvidia.com/)  
[![Docker Ready](https://img.shields.io/badge/Docker-Ready-46E3B7?style=for-the-badge&logo=docker&logoColor=black)](https://www.docker.com/)  
[![Brevo Transactional‑SMTP](https://img.shields.io/badge/Brevo-Transactional%20SMTP-0B99FF?style=for-the-badge&logo=brevo&logoColor=white)](https://www.brevo.com/)  
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow?style=for-the-badge)](LICENSE)

---

## 📚 Overview

OmniMart AI is a lightweight **Spring Boot 3** microservice that powers a conversational shopping assistant. It bundles:

- Multi‑turn chat with long‑term context and optional tool calls  
- Hybrid recommendation engine (behaviour, content, ratings, popularity)  
- Sentiment and topic mining from product reviews  
- AI‑generated side‑by‑side specification comparisons  
- Procedural UI features (neon doodles, magnetic cursor, glass‑morphic cards)  
- IP‑based geolocation on a Leaflet map  
- Brevo transactional e‑mail (OTPs, receipts)  
- Zero‑hallucination guardrails – all data is fetched through secure service calls

The service supports three AI backends: NVIDIA Nemotron 3 Ultra, a local checkpoint, or a mock implementation. Every interaction is persisted in a relational database for audit and replay.

---

## 🚀 Quick Start

```bash
# macOS / Linux
./mvnw spring-boot:run

# Windows
./mvnw.cmd spring-boot:run
```

The application listens on **port 8080**. Open <http://localhost:8080> or try the REST endpoints.

> 📌 *Database.*  
> If you want to use a database other than the default in‑memory H2, set:
> ```
> SPRING_DATASOURCE_URL=
> SPRING_DATASOURCE_USERNAME=
> SPRING_DATASOURCE_PASSWORD=
> ```
> before running.

---

## ⚙️ Configuration

The application reads its settings from `src/main/resources/application.yml`. Environment variables override the YAML values:

| Variable                | Default | Description |
|------------------------ |---------|-------------|
| `PORT`                  | `8080`  | HTTP listening port (useful on Render, Fly.io, etc.) |
| `AI_PROVIDER`           | `nvidia`| `nvidia`, `local`, or `mock` |
| `NVIDIA_API_KEYS`       | *required* | Comma‑separated NVIDIA API keys |
| `NVIDIA_MODEL`          | `nvidia/nemotron-3-ultra-550b-a55b` | Model identifier |
| `BREVO_API_KEY`          | *required* | Brevo transactional‑email key |
| `BREVO_SENDER_EMAIL`     | `support@omnimart-ai.com` | Sender e‑mail |
| `BREVO_SENDER_NAME`      | `OmniMart AI` | Sender display name |
| `SPRING_DATASOURCE_URL`  | `jdbc:h2:mem:omnimart;DB_CLOSE_ON_EXIT=FALSE` | JDBC URL |
| `SPRING_DATASOURCE_USERNAME` | `sa` | DB username |
| `SPRING_DATASOURCE_PASSWORD` | `""` | DB password |

Copy the YAML file to override any defaults locally.

---

## 🏗️ Architecture

```
┌──────────────────────┐
│  Web Layer           │  (React / Thymeleaf)
└──────────┬───────────┘
           ▼
┌──────────────────────┐
│  Controller Layer   │  (Spring MVC)
└──────────┬───────────┘
           ▼
┌──────────────────────┐
│  AI Orchestrator    │  (memory + tool selector)
└──────────┬───────────┘
           ▼
┌──────────────────────┐
│  Tool Services       │  (lookup, profiling, comparison)
└──────────┬───────────┘
           ▼
┌──────────────────────┐
│  Data Layer          │  (H2 / MySQL)
└──────────────────────┘
```

---

## 📦 Docker

```bash
# Build
docker build -t omnimart-ai:latest .

# Run
docker run -p 8080:8080 \
  -e AI_PROVIDER=nvidia \
  -e NVIDIA_API_KEYS=YOUR_KEY \
  -e BREVO_API_KEY=YOUR_BREVO_KEY \
  omnimart-ai:latest
```

The multi‑stage Dockerfile produces an image of ~80 MB.

---

## 🌐 Deploy

### Render (or any container platform)

1. Push the repo to GitHub.  
2. In Render, create a **Web Service** → **From GitHub** → select this repo.  
3. Render will detect the `Dockerfile`.  
4. Add the required environment variables (`AI_PROVIDER`, `NVIDIA_API_KEYS`, `BREVO_API_KEY`).  
5. Render exposes the app on `${PORT}` automatically.

---

## 👤 Demo Accounts

| Role      | Email                | Password | Permissions |
|-----------|----------------------|-----------|--------------|
| Customer  | `user@omnimart.com`  | `password123` | Storefront, cart, AI assistant |
| Admin     | `admin@omnimart.com` | `admin123`   | Analytics, sentiment charts, admin Q&A |

*These accounts are for demonstration only.*

---

## ✅ Tests

```bash
./mvnw test
```

Unit tests cover orchestrator logic and the AI tool abstraction.

---

## 🤝 Contributing

Pull requests are welcome. For major changes, please open an issue first to discuss the approach. See the [CODE_OF_CONDUCT.md](CODE_OF_CONDUCT.md) for guidelines.

---

## 📜 Changelog

| Version | Date       | Highlights |
|---------|------------|------------|
| **v1.0.0** | 2026‑08‑20 | Initial stable release – Spring Boot 3, NVIDIA Nemotron 3 Ultra, hybrid recommendation engine, zero‑hallucination guardrails, procedural UI, Docker build, Render deployment guide, demo accounts |

---

## 📄 License

MIT – see the [LICENSE](LICENSE) file.
