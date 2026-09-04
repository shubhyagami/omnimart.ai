# OmniMart AI

[![Java 21](https://img.shields.io/badge/Java-21-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)](https://openjdk.org/)  
[![Spring Boot 3.3.x](https://img.shields.io/badge/Spring%20Boot-3.3.x-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)](https://spring.io/projects/spring-boot)  
[![NVIDIA Nemotron 3 Ultra](https://img.shields.io/badge/NVIDIA-Nemotron%203-Ultra-76B900?style=for-the-badge&logo=nvidia&logoColor=white)](https://www.nvidia.com/)  
[![Docker Ready](https://img.shields.io/badge/Docker-Ready-46E3B7?style=for-the-badge&logo=docker&logoColor=black)](https://www.docker.com/)  
[![Brevo Transactional Email](https://img.shields.io/badge/Brevo-Transactional%20OTP-0B99FF?style=for-the-badge&logo=brevo&logoColor=white)](https://www.brevo.com/)  
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow?style=for-the-badge)](LICENSE)

> **Designed & crafted by** [Shubh Kumar](https://github.com/shubhyagami)

---

## 🌍 Overview

OmniMart AI is a Spring Boot 3 application that powers a conversational shopping assistant, AI‑driven product recommendations, and a glass‑morphic user interface.  
All AI responses are pulled from a relational database, eliminating raw‑SQL hallucinations.  
The service can switch between NVIDIA’s Nemotron 3 Ultra, a local model, or a mock provider, falling back gracefully when a provider is unavailable.

---

## 🚀 Key Features

| Feature | Description |
|---------|-------------|
| Conversational Assistant | Multi‑turn chat powered by NVIDIA Nemotron 3 Ultra, with memory and tool‑based reasoning. |
| Recommendation Engine | Combines user preferences, browsing history, content relevance, ratings, and popularity. |
| Sentiment & Topic Mining | Analyzes user reviews for actionable insights. |
| AI‑Driven Comparison | Generates side‑by‑side specs tables with an AI verdict banner. |
| Procedural UI | Neon doodles, magnetic cursor, click‑burst particles, glassmorphic cards. |
| Geo‑Location | IP‑based city/state/country displayed on a responsive Leaflet map. |
| Transactional Email | Brevo integration for OTPs, receipts, and account actions. |
| Zero‑Hallucination Guardrails | All data retrieved via secured service calls; no raw SQL references. |

---

## 🏗️ Architecture

```
┌─────────────────────┐   HTTP(S)   ┌───────────────────────┐
│  Web Layer (UI)      │───────────►│  Controller Layer        │
│  (React/Thymeleaf) │            │  (Spring MVC)            │
└─────────────────────┘            └───────┬────────────────┘
                                     │
                               ┌───────▼────────────────┐
                               │  AI Orchestrator        │
                               │  (Memory, tool selector)│
                               └───────┬────────────────┘
                                     │
                                    ▼
                               ┌────────────────────────────┐
                               │  Tool Services (lookup,      │
                               │   profiling, comparison)   │
                               └───────┬─────────────────────┘
                                     │
                                     ▼
                               ┌─────────────────────┐
                               │  Data Layer          │
                               │  (H2 / MySQL)        │
                               └─────────────────────┘
```

---

## 🎨 UI Highlights

* **Cosmos Wallpaper** – 3‑layer parallax neon doodles with glow.  
* **Interactive Cursor** – Magnetic ring, bag‑icon morphing, click‑burst particles.  
* **Live Mini‑Map** – Leaflet dark tiles, pulse rings, IP‑based location.  
* **Glassmorphic Cards / Table** – 16 px backdrop‑blur, gradient borders, WCAG‑compliant contrast.

---

## 🛠️ Getting Started

### Prerequisites

| Tool   | Minimum Version |
|--------|------------------|
| Java   | 21 (or newer)   |
| Maven  | 3.9+ (wrapper `mvnw` is included) |
| Docker | Optional, but recommended for CI/CD |

### Run Locally

```bash
# Windows
./mvnw.cmd spring-boot:run

# macOS / Linux
./mvnw spring-boot:run
```

The application defaults to **port 8080** (`$PORT` from Render).  
* Storefront: <http://localhost:8080>  
* H2 Console: <http://localhost:8080/h2-console>

### Build and Run with Docker

```bash
# Build
docker build -t omnimart-ai:latest .

# Run
docker run -p 8080:8080 \
           -e AI_PROVIDER=nvidia \
           omnimart-ai:latest
```

---

## 📦 Deployment on Render

1. Push the repo to GitHub.  
2. In Render, click **New → Service** → **Select this repository**.  
3. Render will detect the Dockerfile, build the image, and prompt for the environment variables listed below.  

> Render exposes the web service on `$PORT`. The application uses `server.port: ${PORT:8080}` to match automatically.

---

## ⚙️ Environment Variables

| Variable           | Default | Description |
|--------------------|---------|-------------|
| `PORT`             | `8080`  | HTTP port (overridden by Render). |
| `AI_PROVIDER`      | `nvidia` | `nvidia`, `local`, or `mock`. |
| `NVIDIA_API_KEYS`  | **required** | Comma‑separated list of NVIDIA API keys. |
| `NVIDIA_MODEL`     | `nvidia/nemotron-3-ultra-550b-a55b` | Identifier for the NVIDIA model. |
| `BREVO_API_KEY`    | **required** | Brevo transactional email key. |
| `BREVO_SENDER_EMAIL` | `support@omnimart-ai.com` | Verified sender address. |
| `BREVO_SENDER_NAME`  | `OmniMart AI` | Sender name used in outgoing emails. |

---

## 👤 Demo Accounts

> Credentials are pre‑filled when you visit `/login`.

| Role      | Email                | Password      | Access |
|-----------|----------------------|--------------|--------|
| Customer  | `user@omnimart.com`  | `password123` | Storefront, cart, orders, AI assistant |
| Admin     | `admin@omnimart.com` | `admin123`  | Analytics, sentiment charts, admin AI Q&A |

---

## 📜 Changelog

* **v1.0.0 – 2026‑08‑20** – Initial release: Spring Boot 3, NVIDIA Nemotron 3 Ultra, hybrid recommendation engine, zero‑hallucination guardrails, procedural UI toolkit, Docker multi‑stage build, Render blueprint, demo accounts.

---

## 🤝 Contributing

Pull requests are welcome!  
Open an issue first to discuss major changes or new features.  
See the `CODE_OF_CONDUCT.md` for style guidelines.

---

## 📄 License

MIT – see the [LICENSE](LICENSE) file.
