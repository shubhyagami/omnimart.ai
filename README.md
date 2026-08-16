<div align="center">

# 🌌 OMNIMART AI — NEXT-GEN E-COMMERCE UNIVERSE

### *Autonomous AI-Powered E-Commerce Platform with Live Interactive Canvas, Hyper-Personalization, and Feedback Intelligence*

[![Java 21](https://img.shields.io/badge/Java-21-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)](https://openjdk.org/)
[![Spring Boot 3.3.4](https://img.shields.io/badge/Spring%20Boot-3.3.4-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)](https://spring.io/projects/spring-boot)
[![NVIDIA Nemotron AI](https://img.shields.io/badge/NVIDIA-Nemotron--3--Ultra-76B900?style=for-the-badge&logo=nvidia&logoColor=white)](https://build.nvidia.com/)
[![Render Deploy](https://img.shields.io/badge/Render-Docker%20Ready-46E3B7?style=for-the-badge&logo=render&logoColor=black)](https://render.com/)
[![Brevo Email API](https://img.shields.io/badge/Brevo-Transactional%20OTP-0B99FF?style=for-the-badge&logo=brevo&logoColor=white)](https://www.brevo.com/)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg?style=for-the-badge)](https://opensource.org/licenses/MIT)

<br/>

**Designed & Architected by [Shubh Kumar](https://github.com/)**  
*© 2026 OmniMart AI Technologies Inc. All rights reserved.*

---

</div>

## 📑 Table of Contents
- [🌟 Key Innovations & Features](#-key-innovations--features)
- [🎨 Dynamic UI & Particle Animation Engine](#-dynamic-ui--particle-animation-engine)
- [🤖 AI Architecture & Guardrail System](#-ai-architecture--guardrail-system)
- [📍 Network IP Geolocation & Live Mini-Map](#-network-ip-geolocation--live-mini-map)
- [✉️ Brevo Transactional Email & 6-Digit OTP](#️-brevo-transactional-email--6-digit-otp)
- [📊 Side-by-Side AI Comparison Matrix](#-side-by-side-ai-comparison-matrix)
- [🏗️ System Architecture & Workflow](#️-system-architecture--workflow)
- [☁️ One-Click Deployment to Render](#️-one-click-deployment-to-render)
- [💻 Local Development & Docker Guide](#-local-development--docker-guide)
- [🔑 Demo Accounts & Presets](#-demo-accounts--presets)
- [⚙️ Environment Variables Reference](#️-environment-variables-reference)

---

## 🌟 Key Innovations & Features

1. **Procedural HTML5 Canvas Live Cosmos Wallpaper**:
   - Deep vector doodle engine rendering futuristic tech and e-commerce artifacts (Smartphones, Gaming Rigs, Delivery Pods, AI Neural Nodes, Microchips).
   - 3-Layer Parallax depth with physics-based proximity repulsion, glowing cursor auras, and dynamic constellation lines.
   - Synchronized with AI Chat: background doodles highlight in amber when products are suggested.

2. **Fluid Animated Shopping Mouse Cursor**:
   - Glowing amber cursor with fluid trailing ring and magnetic physics.
   - Interactive hover morph: transforms into an animated bouncing shopping bag over actionable buttons.
   - Multicolored particle burst celebration on user clicks.

3. **Autonomous Agentic AI Shopping Assistant (NVIDIA Nemotron-3-Ultra)**:
   - Queries real database inventory via structured safe tools (`toolRouter.searchProducts`).
   - Categorically accurate retrieval: queries for *"gaming laptops"* return verified high-refresh GPU laptops; queries for *"camera phone under 40k"* return top-rated camera smartphones.
   - Context-aware multi-turn conversational memory.

4. **Multi-Factor Hybrid Recommendation Engine**:
   - Weighted ranking algorithm:
     $$\text{Score} = 0.35 \times \text{UserPreference} + 0.25 \times \text{BehavioralSim} + 0.20 \times \text{ContentRelevance} + 0.10 \times \text{Rating} + 0.10 \times \text{Popularity}$$
   - Explainable *"Why this product?"* badges on every card.

5. **Customer Feedback Sentiment & Emotion Intelligence**:
   - Deep sentiment extraction across customer reviews, classifying topics (*Battery, Display, Camera, Performance, Delivery*).
   - Real-time Chart.js visual analytics in the Executive Admin Dashboard.

6. **Network IP Geolocation & Live Leaflet Mini-Map**:
   - Automated public IP and ISP triangulation (Airtel, Jio, etc.) without battery-draining GPS.
   - Interactive modal with live Leaflet mini-map, 1-click IP copy, and manual destination overrides.

7. **Transactional 6-Digit Email OTP (Brevo SMTP API)**:
   - High-contrast responsive dark HTML email templates for OTP verification and live shipment confirmation receipts.

---

## 🎨 Dynamic UI & Particle Animation Engine

| Component | Visual Feature | Implementation |
|---|---|---|
| **Cosmos Wallpaper** | Procedural 2D neon doodles, 3-layer parallax, glowing cursor aura | [`live-wallpaper.js`](file:///d:/portfolio/Amazon/src/main/resources/static/js/live-wallpaper.js) & [`live-wallpaper.css`](file:///d:/portfolio/Amazon/src/main/resources/static/css/live-wallpaper.css) |
| **Interactive Cursor** | Magnetic trailing ring, shopping bag morph, click particle burst | [`cursor.js`](file:///d:/portfolio/Amazon/src/main/resources/static/js/cursor.js) & [`main.css`](file:///d:/portfolio/Amazon/src/main/resources/static/css/main.css) |
| **Live Mini-Map** | Leaflet radar marker, dark tiles, pulse rings, IP scanner | [`network-location.js`](file:///d:/portfolio/Amazon/src/main/resources/static/js/network-location.js) |
| **Comparison Matrix** | High-contrast glassmorphic table, WCAG 16.5:1 ratio, spec breakdown | [`compare.html`](file:///d:/portfolio/Amazon/src/main/resources/templates/compare/compare.html) |
| **Glassmorphic Cards** | 16px backdrop-blur, gradient border glows, hover lift | Bootstrap 5.3 + Custom CSS Tokens |

---

## 🤖 AI Architecture & Guardrail System

```mermaid
graph TD
    UserClient[Web Browser / User UI] -->|HTTP / AJAX| Security[Spring Security 6 & Filters]
    Security --> Controllers[MVC & REST Controllers]
    Controllers --> AIOrch[AI Orchestrator & Multi-Turn Memory]
    
    AIOrch --> ToolRouter[Safe Backend Tool Execution]
    ToolRouter --> ProductService[Product & Inventory Service]
    ToolRouter --> PreferenceService[User Preference Profiler]
    ToolRouter --> ComparisonService[Product Comparison Engine]
    ToolRouter --> FeedbackService[Customer Feedback Intelligence]
    
    AIOrch --> ProviderSwitch[AI Provider Layer]
    ProviderSwitch --> Nvidia[NvidiaAIProvider (Nemotron-3-Ultra-550B)]
    ProviderSwitch --> LocalAI[LocalAIProvider (vLLM / Ollama)]
    ProviderSwitch --> MockAI[MockAIProvider (Deterministic Spec Engine)]
    
    ProductService --> DB[(H2 Database / MySQL)]
    PreferenceService --> DB
    FeedbackService --> DB
```

### 🛡️ Core AI Guardrails
- **Zero Hallucination Guarantee**: Product recommendations and spec comparisons pull candidate items strictly from JPA database queries.
- **No Raw SQL Execution**: Natural language queries are transformed into structured criteria without executing untrusted strings.
- **Sequential Multi-Key Fallback**: Automatic failover between 3 NVIDIA API keys with instant fallback to the local deterministic engine on network timeouts.

---

## 📍 Network IP Geolocation & Live Mini-Map

- **Automatic Network Triangulation**: Determines City, State, Country, and Postal Code using public IP intelligence without requiring browser GPS permissions.
- **Navbar Delivery Pill**: Shows live destination (e.g. `Kolkata • 700009` or `Gurugram • 122001`).
- **Interactive Leaflet Mini-Map**: Centered on network coordinates with radar pulse animations and manual destination updates.

---

## ✉️ Brevo Transactional Email & 6-Digit OTP

- **API Integration**: Connects via `https://api.brevo.com/v3/smtp/email`.
- **Security Use-Cases**:
  - 6-digit OTP verification for new user registration.
  - 6-digit OTP verification for profile email changes.
  - Rich HTML order receipts with tracking links and itemized invoice details.
- **Email Compatibility**: Tested across Gmail, Apple Mail, and Outlook Dark Mode with inline table layouts.

---

## 📊 Side-by-Side AI Comparison Matrix

- Multi-product comparator supporting up to 4 simultaneous items.
- Hardware matrix covering: **Processor, RAM, Storage, Display, Battery, Camera, OS, and Price**.
- Automated AI verdict banner highlighting:
  - 🏆 **Best Overall**: Highest composite hardware score.
  - 💡 **Best Value for Money**: Optimal feature-to-price ratio.
  - ⚡ **Best Performance**: Peak throughput and benchmark leader.

---

## ☁️ One-Click Deployment to Render

Deploy OmniMart AI to **Render** in under 3 minutes using Docker or Render Blueprints.

### Option 1: Render Blueprint (`render.yaml`) — Recommended
1. Fork or push this repository to GitHub.
2. Log in to [Render Dashboard](https://dashboard.render.com/).
3. Click **New +** $\to$ **Blueprint**.
4. Select your repository. Render will automatically detect [`render.yaml`](file:///d:/portfolio/Amazon/render.yaml) and configure the web service.
5. Click **Apply**.

### Option 2: Deploy as a Docker Web Service
1. In Render Dashboard, click **New +** $\to$ **Web Service**.
2. Connect your GitHub repository.
3. Choose **Docker** as the runtime.
4. Set Environment Variables:
   - `AI_PROVIDER`: `nvidia` (or `mock`)
   - `NVIDIA_API_KEYS`: *Your NVIDIA API key pool*
   - `BREVO_API_KEY`: *Your Brevo API Key*
5. Click **Create Web Service**.

> **Note**: The application is configured to automatically read Render's dynamic `$PORT` environment variable (`server.port: ${PORT:8080}`).

---

## 💻 Local Development & Docker Guide

### Prerequisites
- **Java 21** or later (`java -version`)
- **Maven 3.9+** (or use included `mvnw`)
- **Docker** (optional, for containerized run)

### 1. Run Locally with Maven Wrapper
```bash
# Windows
.\mvnw.cmd spring-boot:run

# macOS / Linux
./mvnw spring-boot:run
```

Access the application:
- **Storefront**: [http://localhost:8080](http://localhost:8080)
- **H2 DB Console**: [http://localhost:8080/h2-console](http://localhost:8080/h2-console) (JDBC URL: `jdbc:h2:mem:omnimartdb`, User: `sa`, Password: `""`)

### 2. Build and Run with Docker
```bash
# Build multi-stage Docker image
docker build -t omnimart-ai:latest .

# Run container
docker run -p 8080:8080 -e AI_PROVIDER=nvidia omnimart-ai:latest
```

---

## 🔑 Demo Accounts & Presets

Click **"🔑 Demo Account Credentials"** on the `/login` page to auto-fill credentials with 1 click:

| Role | Email | Password | Permissions |
|---|---|---|---|
| **Demo Customer** | `user@omnimart.com` | `password123` | Storefront, AI Assistant, Profile, Cart, Orders, Wishlist |
| **System Admin** | `admin@omnimart.com` | `admin123` | Executive Analytics Dashboard, Sentiment Charts, Admin AI Q&A |

---

## ⚙️ Environment Variables Reference

| Variable | Default Value | Description |
|---|---|---|
| `PORT` | `8080` | Server HTTP port (auto-bound by Render) |
| `AI_PROVIDER` | `nvidia` | Active AI provider (`nvidia`, `local`, or `mock`) |
| `NVIDIA_API_KEYS` | *3-Key Fallback Pool* | Comma-separated NVIDIA API Keys |
| `NVIDIA_MODEL` | `nvidia/nemotron-3-ultra-550b-a55b` | Model identifier |
| `BREVO_API_KEY` | *Configured Key* | Brevo Transactional Email API Key |
| `BREVO_SENDER_EMAIL` | `support@omnimart-ai.com` | Verified sender address |
| `BREVO_SENDER_NAME` | `OmniMart AI` | Email sender name |

---

<div align="center">

### 🌟 Designed & Crafted with Excellence by **Shubh Kumar**
*© 2026 OmniMart AI Technologies Inc. All rights reserved. Designed for enterprise e-commerce demonstration.*

</div>
