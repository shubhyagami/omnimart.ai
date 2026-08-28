<div align="center">

# 🌌 OmniMart AI  

### Autonomous E‑Commerce Platform with AI‑Powered Shopping Assistant  

[![Java 21](https://img.shields.io/badge/Java-21-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)](https://openjdk.org/)  
[![Spring Boot 3.3.4](https://img.shields.io/badge/Spring%20Boot-3.3.4-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)](https://spring.io/projects/spring-boot)  
[![NVIDIA Nemotron](https://img.shields.io/badge/NVIDIA-Nemotron%203--Ultra-76B900?style=for-the-badge&logo=nvidia&logoColor=white)](https://build.nvidia.com/)  
[![Render Deploy](https://img.shields.io/badge/Render-Docker%20Ready-46E3B7?style=for-the-badge&logo=render&logoColor=black)](https://render.com/)  
[![Brevo](https://img.shields.io/badge/Brevo-Transactional%20OTP-0B99FF?style=for-the-badge&logo=brevo&logoColor=white)](https://www.brevo.com/)  
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg?style=for-the-badge)](https://opensource.org/licenses/MIT)  

**Architected by [Shubh Kumar](https://github.com/shubhyagami)**  

</div>

---

## Overview  

OmniMart AI is a Spring Boot 3 application that delivers a conversational shopping assistant, hyper‑personalized recommendations, and an immersive UI built with a procedural canvas, particle effects, and a live mini‑map. All AI interactions are grounded in the database to guarantee zero hallucinations, and the system orchestrates multiple AI providers with automatic fallback.

---

## Key Features  

- **AI Shopping Assistant** – Conversational product discovery powered by NVIDIA Nemotron‑3‑Ultra.  
- **Hybrid Recommendation Engine** – Weighs preferences, behavior, content relevance, ratings, and popularity for precise suggestions.  
- **Sentiment & Emotion Intelligence** – Analyzes reviews to extract topics and sentiment, enabling business‑focused insights.  
- **Procedural Live Wallpaper & Particle Engine** – 3‑layer parallax neon doodles, magnetic cursor ring, shopping‑bag morph, and click‑burst particles.  
- **Network Geolocation & Mini‑Map** – Determines city, state, country, and postal code from IP and visualizes it on a Leaflet mini‑map.  
- **Transactional Email & OTP** – Secure email delivery via Brevo for account creation, updates, and order receipts.  
- **AI Comparison Matrix** – Compare up to four products side‑by‑side with hardware breakdown and an AI‑generated verdict banner.  
- **Zero‑Hallucination Guardrails** – Responses are derived exclusively from the database; no raw SQL is executed.  

---

## AI Architecture & Guardrails  

The system is organized into clear layers:

1. **Web Layer** – Browser UI communicates with Spring Security‑protected REST endpoints.  
2. **Controller Layer** – Handles HTTP requests and routes to the AI orchestrator.  
3. **AI Orchestrator** – Manages multi‑turn memory, routes queries to the appropriate backend tool, and switches AI providers when needed.  
4. **Tool Services** – Safely executes operations such as product lookup, preference profiling, comparison, and feedback analysis without exposing raw SQL.  
5. **Data Layer** – Persists all data in an H2/MySQL database.  

All AI responses are database‑grounded; the orchestrator guarantees that no untrusted strings reach the database engine.

---

## Dynamic UI & Particle Engine  

- **Cosmos Wallpaper** – Procedurally generated 2‑D neon doodles with 3‑layer parallax and glow.  
- **Interactive Cursor** – Magnetic trailing ring, morphing shopping‑bag icon, and particle burst on click.  
- **Live Mini‑Map** – Leaflet radar marker, dark tiles, pulse rings, and IP‑based location scanner.  
- **Comparison Matrix** – Glassmorphic table with WCAG‑compliant contrast, automatic AI verdict banner.  
- **Glassmorphic Cards** – 16 px backdrop‑blur, gradient border glows, smooth hover lift.  

All UI components reuse shared CSS tokens and Bootstrap 5.3 utilities for consistency.

---

## Getting Started  

### Prerequisites  

- **Java 21** (or newer)  
- **Maven 3.9+** – the project ships with the `mvnw` wrapper, so no separate installation is required.  
- **Docker** (optional, for containerized runs).  

### Run Locally  

```bash
# Windows
.\mvnw.cmd spring-boot:run

# macOS / Linux
./mvnw spring-boot:run
```

The application starts on **port 8080** (or the `$PORT` variable when deployed on Render).  

- Storefront: <http://localhost:8080/>  
- H2 Console: <http://localhost:8080/h2-console>  

### Build & Run with Docker  

```bash
docker build -t omnimart-ai:latest .
docker run -p 8080:8080 -e AI_PROVIDER=nvidia omnimart-ai:latest
```

---

## Deployment  

### Render Blueprint  

1. Fork or push the repository to GitHub.  
2. Open the Render Dashboard and click **New + → Blueprint**.  
3. Select the repository and click **Apply**.  

Render automatically detects the Dockerfile and configures the service with the required environment variables.

### Alternative Docker Deploy  

Push the repository to a container registry, then create a Docker Web Service on Render, configure the environment variables, and expose the `$PORT` variable (`server.port: ${PORT:8080}`).

---

## Demo Accounts  

- **Customer** – `user@omnimart.com` / `password123` – full storefront access, AI assistant, cart, orders, wishlist.  
- **Admin** – `admin@omnimart.com` / `admin123` – executive analytics, sentiment charts, admin AI Q&A.  

Credentials can be auto‑filled on the `/login` page.

---

## Environment Variables  

| Variable | Default | Description |
|----------|---------|-------------|
| `PORT` | `8080` | HTTP port (auto‑bound on Render). |
| `AI_PROVIDER` | `nvidia` | Active AI provider (`nvidia`, `local`, or `mock`). |
| `NVIDIA_API_KEYS` | *comma‑separated list* | Fallback pool of NVIDIA API keys. |
| `NVIDIA_MODEL` | `nvidia/nemotron-3-ultra-550b-a55b` | Model identifier. |
| `BREVO_API_KEY` | *configured key* | Brevo Transactional Email API key. |
| `BREVO_SENDER_EMAIL` | `support@omnimart-ai.com` | Verified sender address. |
| `BREVO_SENDER_NAME` | `OmniMart AI` | Email sender name. |

---

## Changelog  

- **v1.0.0 – 2026‑08‑20**  
  - Initial release with Spring Boot 3, Java 21, and NVIDIA Nemotron‑3‑Ultra integration.  
  - Implemented hybrid recommendation engine, zero‑hallucination guardrails, and the procedural UI toolkit.  
  - Added Docker multi‑stage build, Render deployment blueprint, and demo account workflow.  

---

<div align="center">

**Designed & Crafted by [Shubh Kumar](https://github.com/shubhyagami)**  

</div>
