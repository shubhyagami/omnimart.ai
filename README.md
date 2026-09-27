[K[2m  [2mmodel z-ai/glm-5.3-flash failed, trying next...[0m[0m
[K[2m  [2mmodel deepseek-ai/deepseek-v4.1-flash failed, trying next...[0m[0m
# OmniMart AI

> **Author:** [Shubh Kumar](https://github.com/shubhyagami)

![Java 21](https://img.shields.io/badge/Java-21-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)  
![Spring Boot 3.3](https://img.shields.io/badge/Spring%20Boot-3.3.x-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)  
![NVIDIA Nemotron 3 Ultra](https://img.shields.io/badge/NVIDIA%20Nemotron%203-Ultra-76B900?style=for-the-badge&logo=nvidia&logoColor=white)  
![Docker](https://img.shields.io/badge/Docker-Ready-46E3B7?style=for-the-badge&logo=docker&logoColor=black)  
![Brevo](https://img.shields.io/badge/Brevo-Transactional%20SMTP-0B99FF?style=for-the-badge&logo=brevo&logoColor=white)  
![MIT License](https://img.shields.io/badge/License-MIT-yellow?style=for-the-badge)

---  

## 🚀 Quick start

```bash
# Clone the repo
git clone https://github.com/shubhyagami/omnimart.ai
cd omnimart.ai

# Run locally with Maven
./mvnw spring-boot:run

# Or in Docker (recommended)
docker build -t omnimart-ai .
docker run -d -p 8080:8080 \
  -e AI_PROVIDER=nvidia \
  -e NVIDIA_API_KEYS=YOUR_KEY \
  -e BREVO_API_KEY=YOUR_BREVO_KEY \
  omnimart-ai
```

Open <http://localhost:8080> in a browser. The REST API is also available at `/api/...`.

---

## 📚 What is OmniMart AI?

OmniMart AI is a lightweight Spring Boot microservice that powers a conversational shopping assistant. It provides:

- **Multi‑turn dialogue** with long‑term context and optional LLM tool calls  
- **Hybrid recommendation engine** (behaviour, content, ratings, and popularity signals)  
- **Sentiment & topic extraction** from customer reviews  
- **AI‑generated product specification cards** (side‑by‑side, glass‑morphic UI)  
- **Procedural UI**: neon doodles, magnetic cursor, glass‑morphic cards  
- **Map integration** using IP‑based geolocation (Leaflet)  
- **Transactional e‑mail** via Brevo (OTPs, receipts)  
- **Guardrails** that force all data to come through secure service calls, preventing hallucinations

The service can run with three AI backends: NVIDIA Nemotron 3 Ultra, a local checkpoint, or a mock implementation. Every user interaction is persisted in a relational database for audit and replay.

---

## 🔧 Configuration

| Variable                       | Default (YAML) | Note |
|--------------------------------|-----------------|------|
| `PORT`                         | `8080`          | HTTP port – useful on Render, Fly.io, etc. |
| `AI_PROVIDER`                 | `nvidia`        | `nvidia`, `local`, or `mock` |
| `NVIDIA_API_KEYS`              | **required**   | Comma‑separated keys |
| `NVIDIA_MODEL`                  | `nvidia/nemotron-3-ultra-550b-a55b` | Model ID |
| `BREVO_API_KEY`                | **required**   | Brevo transactional‑email key |
| `BREVO_SENDER_EMAIL`           | `support@omnimart-ai.com` | Sender email |
| `BREVO_SENDER_NAME`            | `OmniMart AI`  | Sender display name |
| `SPRING_DATASOURCE_URL`         | `jdbc:h2:mem:omnimart;DB_CLOSE_ON_EXIT=FALSE` | JDBC URL |
| `SPRING_DATASOURCE_USERNAME`   | `sa`            | DB username |
| `SPRING_DATASOURCE_PASSWORD`   | `""`            | DB password |

> **Tip**: Keep secrets out of source control by using a `.env` file, Docker secrets, or your cloud platform’s secret manager.

---

## 🏗️ Architecture

```
┌───────────────┐
│ React / UI    │
└──────┬────────┘
       ▼
┌───────────────┐
│ REST Controllers│  (Spring MVC)
└──────┬────────┘
       ▼
┌───────────────┐
│ AI Orchestrator│  (session memory, tool selector)
└──────┬────────┘
       ▼
┌───────────────┐
│ Tool Services │  (product lookup, comparison, profiling)
└──────┬────────┘
       ▼
┌───────────────┐
│ Database      │  (H2 in‑memory / MySQL)
└───────────────┘
```

---

## 🌐 Deployment

The application is Docker‑ready. After building the image, expose port 8080 and set the required environment variables. The following cloud providers work out‑of‑the‑box:

| Provider | Steps |
|-----------|-------|
| Render | Create a **Web Service** from this repo, let Render pick up the `Dockerfile`. Set `AI_PROVIDER`, `NVIDIA_API_KEYS`, `BREVO_API_KEY`. |
| Fly.io | Use `fly launch`, provide the same env vars, and expose port 8080. |
| Railway | Upload the repo, set the same env vars, and run the container. |

---

## 👤 Demo Accounts

| Role      | Email                 | Password      | Permissions                     |
|-----------|-----------------------|---------------|--------------------------------|
| Customer  | `user@omnimart.com`   | `password123` | Storefront, cart, AI assistant |
| Admin     | `admin@omnimart.com` | `admin123`    | Analytics, sentiment charts   |

> *These accounts are for demonstration purposes only.*

---

## ✅ Running Tests

```bash
./mvnw test
```

The test suite covers the orchestrator logic and the AI tool abstraction.

---

## 🤝 Contributing

Pull requests are welcome. For major changes, open an issue first to discuss the approach. Please follow the [CODE_OF_CONDUCT.md](CODE_OF_CONDUCT.md) guidelines.

---

## 📄 Changelog

| Version | Date       | Highlights |
|---------|------------|------------|
| **v1.0.0** | 2026‑08‑20 | Initial stable release – Spring Boot 3, NVIDIA Nemotron 3 Ultra, hybrid recommendation engine, zero‑hallucination guardrails, procedural UI, Docker support, Render deployment guide, demo accounts |

---

## 📜 License

MIT – see the [LICENSE](LICENSE) file.
