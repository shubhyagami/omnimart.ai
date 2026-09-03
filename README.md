# OmniMart AI

[![Java 21](https://img.shields.io/badge/Java-21-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)](https://openjdk.org/)
[![Spring Boot 3.3.4](https://img.shields.io/badge/Spring%20Boot-3.3.4-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)](https://spring.io/projects/spring-boot)
[![NVIDIA Nemotron 3 Ultra](https://img.shields.io/badge/NVIDIA-Nemotron%203-Ultra-76B900?style=for-the-badge&logo=nvidia&logoColor=white)](https://www.nvidia.com/)
[![Render‑Ready](https://img.shields.io/badge/Render-Docker%20Ready-46E3B7?style=for-the-badge&logo=render&logoColor=black)](https://render.com/)
[![Transactional Email](https://img.shields.io/badge/Brevo-Transactional%20OTP-0B99FF?style=for-the-badge&logo=brevo&logoColor=white)](https://www.brevo.com/)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg?style=for-the-badge)](LICENSE)

> **Architected by** [Shubh Kumar](https://github.com/shubhyagami)

---

## 📌 What Is OmniMart AI?

OmniMart AI is a Spring Boot 3 application that powers a conversational shopping assistant, AI‑driven product recommendations, and an interactive, glass‑morphic UI.  
All AI responses are generated from data stored in the database—no raw‑SQL hallucinations. The system can switch between NVIDIA, a local model, or a mock provider with automated fallback.

---

## 🚀 Key Features

| Category | Highlights |
|---------|------------|
| **Conversational Assistant** | NVIDIA Nemotron 3 Ultra powered, multi‑turn memory, tool‑based reasoning |
| **Recommendation Engine** | Blend of user preferences, browsing history, content relevance, rating & popularity |
| **Sentiment & Topic Mining** | Analyzes user reviews for insights |
| **AI‑Driven Comparison** | Side‑by‑side table, hardware stats, AI verdict banner |
| **Procedural UI** | Neon doodles, magnetic cursor, click‑burst particles, glassmorphic cards |
| **Geo‑Location** | IP‑based city/state/country on a responsive Leaflet map |
| **Transactional Email** | Brevo integration for OTP, order receipts and account actions |
| **Zero‑Hallucination Guardrails** | All data retrieved via secure service calls, never raw SQL |

---

## 🏗️ Architecture

```
┌─────────────────────┐   HTTP(S)   ┌───────────────────────┐
│  Web Layer (UI)      │───────────►│  Controller Layer     │
│  (React/Thymeleaf)   │            │  (Spring MVC)          │
└─────────────────────┘            └───────┬────────────────┘
                                         │
                                ┌───────▼────────────────┐
                                │  AI Orchestrator         │
                                │  (Memory, tool selector)│
                                └───────┬────────────────┘
                                         │
                                        ▼
                                ┌────────────────────────────┐
                                │  Tool Services (lookup,     │
                                │   profiling, comparison)    │
                                └────────────────────────────┘
                                         │
                                         ▼
                                ┌─────────────────────┐
                                │  Data Layer         │
                                │  (H2 / MySQL)       │
                                └─────────────────────┘
```

---

## 📱 UI Highlights

- **Cosmos Wallpaper** – 3‑layer parallax neon doodles + glow.
- **Interactive Cursor** – magnetic ring, bag icon morphing, click‑burst particles.
- **Live Mini‑Map** – Leaflet dark tiles, pulse rings, IP‑based location.
- **Glassmorphic Cards / Table** – 16 px backdrop‑blur, gradient borders, WCAG‑compliant contrast.

---

## 🛠️ Getting Started

### Prerequisites

| Tool | Minimum Version |
|------|-----------------|
| Java | 21 (or newer) |
| Maven | 3.9+ (wrapper `mvnw` is included) |
| Docker | Optional, but recommended for CI/CD |

### Run Locally

```bash
# Windows
./mvnw.cmd spring-boot:run

# macOS / Linux
./mvnw spring-boot:run
```

The application runs on **port 8080** (`$PORT` from Render).  
- Storefront: <http://localhost:8080>  
- H2 Console: <http://localhost:8080/h2-console>

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
3. Render will detect the Dockerfile, build the image, and prompt for required environment variables (see below).  

> Render exposes the web service on `$PORT`. The application uses `server.port: ${PORT:8080}` to align automatically.

---

## ⚙️ Environment Variables

| Variable | Default | Description |
|----------|---------|-------------|
| `PORT` | `8080` | HTTP port (overridden by Render). |
| `AI_PROVIDER` | `nvidia` | One of `nvidia`, `local`, or `mock`. |
| `NVIDIA_API_KEYS` | **required** | Comma‑separated list of NVIDIA API keys. |
| `NVIDIA_MODEL` | `nvidia/nemotron-3-ultra-550b-a55b` | Identifier for the NVIDIA model. |
| `BREVO_API_KEY` | **required** | Brevo transactional email key. |
| `BREVO_SENDER_EMAIL` | `support@omnimart-ai.com` | Verified sender address. |
| `BREVO_SENDER_NAME` | `OmniMart AI` | Sender name used in outgoing emails. |

---

## 👤 Demo Accounts

> Credentials are auto‑filled on `/login`.

| Role | Email | Password | Access |
|------|--------|----------|--------|
| Customer | `user@omnimart.com` | `password123` | Storefront, cart, orders, AI assistant |
| Admin | `admin@omnimart.com` | `admin123` | Analytics, sentiment charts, admin AI Q&A |

---

## 📜 Changelog

- **v1.0.0 – 2026‑08‑20** – Initial release: Spring Boot 3, NVIDIA Nemotron 3 Ultra, hybrid recommendation engine, zero‑hallucination guardrails, procedural UI toolkit, Docker multi‑stage build, Render blueprint, demo accounts.

---

✨ *Designed & crafted by [Shubh Kumar](https://github.com/shubhyagami)*

---
