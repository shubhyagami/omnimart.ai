# OmniMart AI  

![Java 21](https://img.shields.io/badge/Java-21-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)  
![Spring Boot 3.3.4](https://img.shields.io/badge/Spring%20Boot-3.3.4-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)  
![NVIDIA Nemotron 3‑Ultra](https://img.shields.io/badge/NVIDIA-Nemotron%203-Ultra-76B900?style=for-the-badge&logo=nvidia&logoColor=white)  
![Render Docker‑Ready](https://img.shields.io/badge/Render-Docker%20Ready-46E3B7?style=for-the-badge&logo=render&logoColor=black)  
![Brevo](https://img.shields.io/badge/Brevo-Transactional%20OTP-0B99FF?style=for-the-badge&logo=brevo&logoColor=white)  
![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg?style=for-the-badge)  

**Architected by [Shubh Kumar](https://github.com/shubhyagami)**  

---  

## 📦 What is OmniMart AI?  

A Spring Boot 3 application that powers a conversational shopping assistant, AI‑driven product recommendations, and a highly‑interactive UI.  
All AI responses are strictly sourced from the database, so the system has no hallucinations.  
It can switch between multiple AI providers (NVIDIA, local model, or a mock) with automatic fallback.

---

## ✨ Key Features  

- **Conversational Shopping Assistant** – Powered by NVIDIA Nemotron‑3‑Ultra.  
- **Hybrid Recommendation Engine** – Combines user preferences, behaviour, content relevance, ratings and popularity.  
- **Sentiment & Topic Mining** – Extracts insights from reviews.  
- **Procedural Canvas & Particle UI** – 3‑layer neon doodles, magnetic cursor ring, shopping‑bag morph, click‑burst particles.  
- **Geo‑location & Mini‑map** – IP‑based city, state, country and postal code, shown on a Leaflet map.  
- **Transactional Email & OTP** – Brevo integration for account actions and order receipts.  
- **AI‑driven Product Comparison** – Side‑by‑side table with hardware stats and an AI verdict banner.  
- **Zero‑Hallucination Guardrails** – All data is fetched via secure service calls, never raw SQL.

---

## 🏗️ Architecture  

1. **Web Layer** – UI communicates with Spring Security‑protected REST endpoints.  
2. **Controller Layer** – Routes requests to the AI orchestrator.  
3. **AI Orchestrator** – Handles multi‑turn memory, tool selection, and provider fallback.  
4. **Tool Services** – Execute safe operations (lookup, profiling, comparison) without exposing raw queries.  
5. **Data Layer** – Persists data in an H2/MySQL instance.

---

## 🎨 UI Highlights  

- **Cosmos Wallpaper** – Procedurally generated neon doodles with 3‑layer parallax and glow.  
- **Interactive Cursor** – Magnetic trailing ring, bag icon morph, click‑burst particles.  
- **Live Mini‑Map** – Leaflet radar marker, dark tiles, pulse rings, IP‑based location scanner.  
- **Glassmorphic Cards & Table** – 16 px backdrop‑blur, gradient borders, WCAG‑compliant contrast.

---

## 🚀 Getting Started  

### Prerequisites  

- Java 21 (or newer)  
- Maven 3.9+ (the wrapper `mvnw` is included)  
- Docker (optional but recommended)

### Run Locally  

```bash
# Windows
.mvnw.cmd spring-boot:run

# macOS / Linux
./mvnw spring-boot:run
```

The app starts on **port 8080** (or `$PORT` when running on Render).

- Storefront: <http://localhost:8080/>  
- H2 Console: <http://localhost:8080/h2-console>

### Docker  

```bash
docker build -t omnimart-ai:latest .
docker run -p 8080:8080 -e AI_PROVIDER=nvidia omnimart-ai:latest
```

---

## 🛠️ Deployment  

### Render Blueprint  

1. Push the repo to GitHub.  
2. In Render, click **New + → Blueprint** and select this repository.  
3. Render will pick up the Dockerfile and set up the service; just provide the required environment variables.

### Manual Docker Deployment  

1. Push the image to a registry.  
2. Create a Render Web Service, set the env vars, expose `$PORT`.  
3. The service will use the `server.port: ${PORT:8080}` override.

---

## 👥 Demo Accounts  

| Role   | Email               | Password      | Access |
|--------|---------------------|---------------|--------|
| Customer | `user@omnimart.com` | `password123` | Storefront, cart, orders, AI assistant |
| Admin     | `admin@omnimart.com` | `admin123`   | Analytics, sentiment charts, admin AI Q&A |

The credentials are autofilled on `/login`.

---

## ⚙️ Environment Variables  

| Variable           | Default                     | Description |
|--------------------|-----------------------------|-------------|
| `PORT`             | `8080`                      | HTTP port (automatic on Render). |
| `AI_PROVIDER`      | `nvidia`                    | `nvidia`, `local`, or `mock`. |
| `NVIDIA_API_KEYS`  | *comma‑separated list*      | Pool of NVIDIA API keys. |
| `NVIDIA_MODEL`     | `nvidia/nemotron-3-ultra-550b-a55b` | Model identifier. |
| `BREVO_API_KEY`     | *configured key*            | Brevo transactional email key. |
| `BREVO_SENDER_EMAIL` | `support@omnimart-ai.com`   | Verified sender address. |
| `BREVO_SENDER_NAME`  | `OmniMart AI`               | Email sender name. |

---

## 📆 Changelog  

- **v1.0.0 – 2026‑08‑20**  
  Initial release with Spring Boot 3, Java 21, NVIDIA Nemotron‑3‑Ultra integration, hybrid recommendation engine, zero‑hallucination guardrails, and a procedural UI toolkit. Added Docker multi‑stage build, Render blueprint, and demo accounts.

---

<div align="center">
**Designed & Crafted by [Shubh Kumar](https://github.com/shubhyagami)**
</div>
