# OmniMart AI  

![Java 21](https://img.shields.io/badge/Java-21-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)  
![Spring Boot 3.3.4](https://img.shields.io/badge/Spring%20Boot-3.3.4-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)  
![NVIDIA Nemotron](https://img.shields.io/badge/NVIDIA-Nemotron%203-Ultra-76B900?style=for-the-badge&logo=nvidia&logoColor=white)  
![Render Deploy](https://img.shields.io/badge/Render-Docker%20Ready-46E3B7?style=for-the-badge&logo=render&logoColor=black)  
![Brevo](https://img.shields.io/badge/Brevo-Transactional%20OTP-0B99FF?style=for-the-badge&logo=brevo&logoColor=white)  
![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg?style=for-the-badge)  

**Architected by [Shubh Kumar](https://github.com/shubhyagami)**  

---  

## Overview  

OmniMart AI is a Spring Boot 3 application that delivers a conversational shopping assistant, hyper‑personalized product recommendations, and an interactive UI featuring procedural canvas effects, particle animations, and a live mini‑map. All AI interactions are grounded in the database, eliminating hallucinations, and the system can switch between multiple AI providers with automatic fallback.

## Key Features  

- **AI Shopping Assistant** – Conversational product discovery powered by NVIDIA Nemotron‑3‑Ultra.  
- **Hybrid Recommendation Engine** – Merges user preferences, behavior, content relevance, ratings, and popularity for precise suggestions.  
- **Sentiment & Emotion Analysis** – Extracts topics and sentiment from reviews to provide business insights.  
- **Procedural Canvas & Particle Engine** – 3‑layer parallax neon doodles, magnetic cursor ring, shopping‑bag morph, and click‑burst particles.  
- **Network Geolocation & Mini‑Map** – Determines city, state, country, and postal code from IP and visualizes it on a Leaflet mini‑map.  
- **Transactional Email & OTP** – Secure email delivery via Brevo for account creation, order receipts, and password resets.  
- **AI Comparison Matrix** – Side‑by‑side product comparison with hardware breakdown and an AI‑generated verdict banner.  
- **Zero‑Hallucination Guardrails** – Responses are derived exclusively from the database; no raw SQL is executed.  

## Architecture & Guardrails  

The application follows a layered architecture:

1. **Web Layer** – Browser UI communicates with Spring Security‑protected REST endpoints.  
2. **Controller Layer** – Routes HTTP requests to the AI orchestrator.  
3. **AI Orchestrator** – Manages multi‑turn memory, selects the appropriate tool, and falls back to alternative AI providers when needed.  
4. **Tool Services** – Safely performs operations such as product lookup, preference profiling, comparison, and feedback analysis without exposing raw SQL.  
5. **Data Layer** – Persists all data in an H2/MySQL database.  

All AI responses are grounded in the database, ensuring that no untrusted strings reach the database engine.

## Dynamic UI & Particle Engine  

- **Cosmos Wallpaper** – Procedurally generated 2‑D neon doodles with 3‑layer parallax and glow.  
- **Interactive Cursor** – Magnetic trailing ring, morphing shopping‑bag icon, and particle burst on click.  
- **Live Mini‑Map** – Leaflet radar marker, dark tiles, pulse rings, and IP‑based location scanner.  
- **Comparison Matrix** – Glassmorphic table with WCAG‑compliant contrast and an automatic AI verdict banner.  
- **Glassmorphic Cards** – 16 px backdrop‑blur, gradient border glows, and smooth hover lift.  

The UI reuses shared CSS tokens and Bootstrap 5.3 utilities for consistency.

## Getting Started  

### Prerequisites  

- **Java 21** (or newer)  
- **Maven 3.9+** – the project includes the `mvnw` wrapper, so no separate installation is required.  
- **Docker** (optional, for containerized runs).  

### Run Locally  

#### Windows  
`.mvnw.cmd spring-boot:run`  

#### macOS / Linux  
`./mvnw spring-boot:run`  

The application starts on **port 8080** (or the `$PORT` variable when deployed on Render).  

- Storefront: <http://localhost:8080/>  
- H2 Console: <http://localhost:8080/h2-console>  

### Build & Run with Docker  

```bash
docker build -t omnimart-ai:latest .
docker run -p 8080:8080 -e AI_PROVIDER=nvidia omnimart-ai:latest
```  

## Deployment  

### Render Blueprint  

1. Push the repository to GitHub.  
2. Open the Render Dashboard, click **New + → Blueprint**, and select the repository.  
3. Render automatically detects the Dockerfile and configures the service with the required environment variables.  

### Alternative Docker Deployment  

Push the image to a container registry, then create a Docker Web Service on Render, set the environment variables, and expose the `$PORT` variable (`server.port: ${PORT:8080}`).

## Demo Accounts  

- **Customer** – `user@omnimart.com` / `password123` – full storefront access, AI assistant, cart, orders, wishlist.  
- **Admin** – `admin@omnimart.com` / `admin123` – executive analytics, sentiment charts, admin AI Q&A.  

Credentials are auto‑filled on the `/login` page.

## Environment Variables  

| Variable                | Default | Description |
|-------------------------|---------|-------------|
| `PORT`                  | `8080`  | HTTP port (auto‑bound on Render). |
| `AI_PROVIDER`           | `nvidia`| Active AI provider (`nvidia`, `local`, or `mock`). |
| `NVIDIA_API_KEYS`       | *comma‑separated list* | Fallback pool of NVIDIA API keys. |
| `NVIDIA_MODEL`          | `nvidia/nemotron-3-ultra-550b-a55b` | Model identifier. |
| `BREVO_API_KEY`         | *configured key* | Brevo Transactional Email API key. |
| `BREVO_SENDER_EMAIL`    | `support@omnimart-ai.com` | Verified sender address. |
| `BREVO_SENDER_NAME`     | `OmniMart AI` | Email sender name. |

## Changelog  

- **v1.0.0 – 2026‑08‑20**  
  - Initial release with Spring Boot 3, Java 21, and NVIDIA Nemotron‑3‑Ultra integration.  
  - Implemented hybrid recommendation engine, zero‑hallucination guardrails, and the procedural UI toolkit.  
  - Added Docker multi‑stage build, Render deployment blueprint, and demo account workflow.  

<div align="center">

**Designed & Crafted by [Shubh Kumar](https://github.com/shubhyagami)**  

</div>
