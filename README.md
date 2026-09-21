# OmniMart AI

> **Author:** [Shubh Kumar](https://github.com/shubhyagami)

![Java 21](https://img.shields.io/badge/Java-21-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)  
![Spring Boot 3.3](https://img.shields.io/badge/Spring%20Boot-3.3.x-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)  
![NVIDIA Nemotron 3 Ultra](https://img.shields.io/badge/NVIDIA%20Nemotron%203-Ultra-76B900?style=for-the-badge&logo=nvidia&logoColor=white)  
![Docker](https://img.shields.io/badge/Docker-Ready-46E3B7?style=for-the-badge&logo=docker&logoColor=black)  
![Brevo](https://img.shields.io/badge/Brevo-Transactional%20SMTP-0B99FF?style=for-the-badge&logo=brevo&logoColor=white)  
![License: MIT](https://img.shields.io/badge/License-MIT-yellow?style=for-the-badge)

---

## 📚 Overview

OmniMart AI is a lightweight Spring Boot 3 microservice that powers a conversational shopping assistant. It bundles:

- Multi‑turn chat with long‑term context and optional tool calls  
- Hybrid recommendation engine (behaviour, content, ratings, popularity)  
- Sentiment and topic mining from product reviews  
- AI‑generated side‑by‑side product specifications  
- Procedural UI elements: neon doodles, magnetic cursor, glass‑morphic cards  
- Leaflet map with IP‑based geolocation  
- Brevo transactional e‑mail (OTPs, receipts)  
- Zero‑hallucination guardrails – all data is fetched through secure service calls  

The service supports three AI backends: NVIDIA Nemotron 3 Ultra, a local checkpoint, or a mock implementation. Every interaction is persisted in a relational database for audit and replay.

---

## 🚀 Getting Started

> **Prerequisites**: Java 21, Maven, Docker (optional)

```bash
# Clone the repository
git clone https://github.com/shubhyagami/omnimart.ai
cd omnimart.ai

# Run with Maven wrapper
./mvnw spring-boot:run
# or on Windows
./mvnw.cmd spring-boot:run
```

The application listens on **port 8080** by default. Open <http://localhost:8080> to see the UI or try the REST endpoints.

> **Using Docker**  
> ```bash
> docker build -t omnimart-ai:latest .
> docker run -p 8080:8080 \
>   -e AI_PROVIDER=nvidia \
>   -e NVIDIA_API_KEYS=YOUR_KEY \
>   -e BREVO_API_KEY=YOUR_BREVO_KEY \
>   omnimart-ai:latest
> ```

---

## ⚙️ Configuration

The app reads from `src/main/resources/application.yml`. Environment variables override YAML values:

| Variable                       | Default (YAML)                                                  | Description |
|--------------------------------|-----------------------------------------------------------------|-------------|
| `PORT`                         | `8080`                                                          | HTTP listening port (useful on Render, Fly.io, etc.) |
| `AI_PROVIDER`                  | `nvidia`                                                       | `nvidia`, `local`, or `mock` |
| `NVIDIA_API_KEYS`                | *required*                                                     | Comma‑separated NVIDIA API keys |
| `NVIDIA_MODEL`                  | `nvidia/nemotron-3-ultra-550b-a55b`                            | Model identifier |
| `BREVO_API_KEY`                 | *required*                                                     | Brevo transactional‑email key |
| `BREVO_SENDER_EMAIL`            | `support@omnimart-ai.com`                                      | Sender e‑mail |
| `BREVO_SENDER_NAME`             | `OmniMart AI`                                                   | Sender display name |
| `SPRING_DATASOURCE_URL`         | `jdbc:h2:mem:omnimart;DB_CLOSE_ON_EXIT=FALSE`                  | JDBC URL |
| `SPRING_DATASOURCE_USERNAME`   | `sa`                                                            | DB username |
| `SPRING_DATASOURCE_PASSWORD`  | `""`                                                           | DB password |

> *Tip*: Use a `.env` file or your cloud platform’s secret manager to keep keys out of source control.

---

## 🏗️ Architecture

```
┌──────────────────────┐
│  Web Layer            │  (React / Thymeleaf)
└──────────┬───────────┘
           ▼
┌──────────────────────┐
│  Controller Layer     │  (Spring MVC)
└──────────┬───────────┘
           ▼
┌──────────────────────┐
│  AI Orchestrator     │  (memory + tool selector)
└──────────┬───────────┘
           ▼
┌──────────────────────┐
│  Tool Services       │  (lookup, profiling, comparison)
└──────────┬───────────┘
           ▼
┌──────────────────────┐
│  Data Layer           │  (H2 / MySQL)
└──────────────────────┘
```

---

## 🌐 Deploy

### Render (or any cloud with Docker support)

1. Push this repo to GitHub.  
2. In Render, create a **Web Service** → **From GitHub** → select this repo.  
3. Render will detect the `Dockerfile`.  
4. Set the following environment variables: `AI_PROVIDER`, `NVIDIA_API_KEYS`, `BREVO_API_KEY`.  
5. Render automatically exposes the app on the `${PORT}` port.

Other providers (Fly.io, Render, Railway, etc.) work the same way if a Dockerfile is present.

---

## 👤 Demo Accounts

| Role      | Email                | Password     | Permissions |
|-----------|----------------------|--------------|-------------|
| Customer  | `user@omnimart.com`  | `password123` | Storefront, cart, AI assistant |
| Admin     | `admin@omnimart.com` | `admin123`    | Analytics, sentiment charts, admin Q&A |

> *These accounts are for demonstration only.*

---

## ✅ Tests

```bash
./mvnw test
```

Unit tests cover orchestrator logic and the AI tool abstraction.

---

## 🤝 Contributing

Pull requests are welcome. For major changes, open an issue first to discuss the approach. See the [CODE_OF_CONDUCT.md](CODE_OF_CONDUCT.md) for guidelines.

---

## 📜 Changelog

| Version | Date       | Highlights |
|---------|------------|------------|
| **v1.0.0** | 2026‑08‑20 | Initial stable release – Spring Boot 3, NVIDIA Nemotron 3 Ultra, hybrid recommendation engine, zero‑hallucination guardrails, procedural UI, Docker build, Render deployment guide, demo accounts |

---

## 📄 License

MIT – see the [LICENSE](LICENSE) file.
