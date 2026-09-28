[K[2m  [2mmodel z-ai/glm-5.3-flash failed, trying next...[0m[0m
[K[2m  [2mmodel deepseek-ai/deepseek-v4.1-flash failed, trying next...[0m[0m
# OmniMart AI

> **Author:** [Shubh Kumar](https://github.com/shubhyagami)

![Java 21](https://img.shields.io/badge/Java-21-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)  
![Spring Boot 3.3](https://img.shields.io/badge/Spring%20Boot-3.3.x-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)  
![NVIDIA Nemotron 3 Ultra](https://img.shields.io/badge/NVIDIA%20Nemotron%203-Ultra-76B900?style=for-the-badge&logo=nvidia&logoColor=white)  
![Docker](https://img.shields.io/badge/Docker-Ready-46E3B7?style=for-the-badge&logo=docker&logoColor=black)  
![Brevo](https://img.shields.io/badge/Brevo-Transactional%20SMTP-0B99FF?style=for-the-badge&logo=brevo&logoColor=white)  
![License](https://img.shields.io/badge/License-MIT-yellow?style=for-the-badge)

---

## 🚀 Quick start

```bash
# Clone the repository
git clone https://github.com/shubhyagami/omnimart.ai
cd omnimart.ai

# Run locally (requires Java 21 and Maven)
./mvnw spring-boot:run
```

Alternatively, build and run with Docker (recommended):

```bash
docker build -t omnimart-ai .
docker run -d -p 8080:8080 \
  -e AI_PROVIDER=nvidia \
  -e NVIDIA_API_KEYS=YOUR_KEY \
  -e BREVO_API_KEY=YOUR_BREVO_KEY \
  omnimart-ai
```

Open <http://localhost:8080> in your browser. The REST API is exposed under `/api/...`.

---

## 📚 What is OmniMart AI?

OmniMart AI is a lightweight Spring Boot microservice that powers a conversational shopping assistant. It combines natural‑language dialogue with real‑time product recommendations, sentiment analysis, and transactional email support.

**Core capabilities**

- Multi‑turn conversations with long‑term context
- Optional LLM‑powered tool calls
- Hybrid recommendation engine (behaviour, content, ratings, popularity)
- Sentiment & topic extraction from customer reviews
- AI‑generated product specification cards
- Procedural UI elements (neon doodles, magnetic cursor, glass‑morphic cards)
- IP‑based geolocation with Leaflet map integration
- Brevo transactional email (OTPs, receipts)
- Guardrails that route all external calls through secure services to avoid hallucinations

The service works with three AI backends: NVIDIA Nemotron 3 Ultra, a local checkpoint, or a mock implementation. Every interaction is persisted in a relational database for audit and replay.

---

## 🔧 Configuration

| Variable                      | Default (application.yml)                                        | Notes |
|-------------------------------|------------------------------------------------------------------|-------|
| `PORT`                        | `8080`                                                           | HTTP port (useful on Render, Fly.io, etc.) |
| `AI_PROVIDER`                | `nvidia`                                                         | Options: `nvidia`, `local`, `mock` |
| `NVIDIA_API_KEYS`             | **required**                                                    | Comma‑separated keys |
| `NVIDIA_MODEL`                | `nvidia/nemotron-3-ultra-550b-a55b`                              | Model ID |
| `BREVO_API_KEY`               | **required**                                                    | Brevo transactional email key |
| `BREVO_SENDER_EMAIL`          | `support@omnimart-ai.com`                                        | Sender email |
| `BREVO_SENDER_NAME`           | `OmniMart AI`                                                    | Sender display name |
| `SPRING_DATASOURCE_URL`       | `jdbc:h2:mem:omnimart;DB_CLOSE_ON_EXIT=FALSE`                    | JDBC URL |
| `SPRING_DATASOURCE_USERNAME`   | `sa`                                                             | DB username |
| `SPRING_DATASOURCE_PASSWORD`   | `""`                                                             | DB password |

> **Tip:** Store secrets outside the repository, e.g. in a `.env` file, Docker secrets, or your cloud provider’s secret manager.

---

## 🏗️ Architecture

```
┌───────────────┐
│   React / UI   │
└──────┬────────┘
       ▼
┌───────────────┐
│ REST Controllers│ (Spring MVC)
└──────┬────────┘
       ▼
┌───────────────┐
│ AI Orchestrator│ (session, tool dispatch)
└──────┬────────┘
       ▼
┌───────────────┐
│ Tool Services │ (product lookup, comparison, profiling)
└──────┬────────┘
       ▼
┌───────────────┐
│  Database     │ (H2 in‑memory / MySQL)
└───────────────┘
```

---

## 🌐 Deployment

The project includes a `Dockerfile`. After building the image, expose port 8080 and set the required environment variables.

### Cloud providers that work out‑of‑the‑box

| Provider | Steps |
|----------|------|
| **Render** | Create a Web Service from this repo, let Render pick up the `Dockerfile`. Set `AI_PROVIDER`, `NVIDIA_API_KEYS`, `BREVO_API_KEY`. |
| **Fly.io** | Run `fly launch`, provide the same env vars, and expose port 8080. |
| **Railway** | Upload the repo, set the env vars, and run the container. |

---

## 👤 Demo Accounts

| Role      | Email                | Password       | Permissions                    |
|-----------|----------------------|----------------|--------------------------------|
| Customer  | `user@omnimart.com`  | `password123` | Storefront, cart, AI assistant |
| Admin      | `admin@omnimart.com` | `admin123`    | Analytics, sentiment charts   |

> These accounts are for demonstration only.

---

## ✅ Running tests

```bash
./mvnw test
```

The test suite covers the orchestrator logic and the AI tool abstraction.

---

## 🤝 Contributing

Pull requests are welcome. For major changes, open an issue first to discuss the approach. Please follow the guidelines in [CODE_OF_CONDUCT.md](CODE_OF_CONDUCT.md).

---

## 📜 Changelog

| Version | Date       | Highlights |
|---------|------------|-------------|
| **v1.0.0** | 2026‑08‑20 | Initial stable release – Spring Boot 3, NVIDIA Nemotron 3 Ultra, hybrid recommendation engine, zero‑hallucination guardrails, procedural UI, Docker support, Render deployment guide, demo accounts |

---

## 📜 License

MIT – see the [LICENSE](LICENSE) file.
