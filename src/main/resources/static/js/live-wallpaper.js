/**
 * OmniMart AI - Interactive Live Wallpaper Canvas Engine
 * Futuristic E-Commerce + AI Hand-Drawn Doodle Universe
 */
(function() {
    'use strict';

    class LiveWallpaper {
        constructor() {
            this.canvas = null;
            this.ctx = null;
            this.width = window.innerWidth;
            this.height = window.innerHeight;
            this.dpr = Math.min(window.devicePixelRatio || 1, 2);
            
            this.objects = [];
            this.totalObjects = this.calculateDensity();
            
            this.mouseX = this.width / 2;
            this.mouseY = this.height / 2;
            this.targetMouseX = this.mouseX;
            this.targetMouseY = this.mouseY;
            this.isMouseMoving = false;
            this.mouseIdleTimer = null;
            
            this.mode = 'default'; // 'default', 'ai', 'shopping', 'recommendation', 'quiet'
            this.intensity = 1.0;
            this.isPaused = false;
            this.reducedMotion = window.matchMedia('(prefers-reduced-motion: reduce)').matches;
            this.animFrameId = null;

            this.palette = ['#6C7CFF', '#8175FF', '#A66CFF', '#C38CFF', '#38BDF8'];
            this.highlightCategory = null;
            this.highlightTimer = null;

            this.init();
        }

        calculateDensity() {
            const area = window.innerWidth * window.innerHeight;
            if (window.innerWidth < 768) return 30; // Mobile
            if (window.innerWidth < 1200) return 55; // Tablet / Laptop
            return 85; // Desktop
        }

        init() {
            // 1. Setup Canvas Element
            this.canvas = document.getElementById('live-wallpaper');
            if (!this.canvas) {
                this.canvas = document.createElement('canvas');
                this.canvas.id = 'live-wallpaper';
                document.body.prepend(this.canvas);
            }
            this.ctx = this.canvas.getContext('2d', { alpha: true });
            this.resize();

            // 2. Spawn Interactive Doodle Objects
            this.spawnObjects();

            // 3. Setup Listeners
            window.addEventListener('resize', () => this.resize());
            window.addEventListener('mousemove', (e) => this.onMouseMove(e));
            window.addEventListener('touchmove', (e) => {
                if (e.touches.length > 0) {
                    this.targetMouseX = e.touches[0].clientX;
                    this.targetMouseY = e.touches[0].clientY;
                }
            }, { passive: true });

            // 4. Start Render Loop
            this.render();
        }

        resize() {
            this.width = window.innerWidth;
            this.height = window.innerHeight;
            this.dpr = Math.min(window.devicePixelRatio || 1, 2);
            
            this.canvas.width = this.width * this.dpr;
            this.canvas.height = this.height * this.dpr;
            this.canvas.style.width = `${this.width}px`;
            this.canvas.style.height = `${this.height}px`;
            this.ctx.scale(this.dpr, this.dpr);

            if (this.objects.length === 0) {
                this.spawnObjects();
            }
        }

        onMouseMove(e) {
            this.targetMouseX = e.clientX;
            this.targetMouseY = e.clientY;
            this.isMouseMoving = true;

            clearTimeout(this.mouseIdleTimer);
            this.mouseIdleTimer = setTimeout(() => {
                this.isMouseMoving = false;
            }, 2500);
        }

        spawnObjects() {
            this.objects = [];
            const types = [
                'smartphone', 'laptop', 'headphones', 'earbuds', 'smartwatch',
                'camera', 'shoppingCart', 'shoppingBag', 'deliveryTruck', 'packageBox',
                'barcode', 'aiRobot', 'neuralNode', 'microchip', 'gameController',
                'starRating', 'wishlistHeart', 'chatBubble', 'amazonSmile', 'sparkle'
            ];

            const cols = 9;
            const rows = 7;
            const cellW = this.width / cols;
            const cellH = this.height / rows;

            let idx = 0;
            for (let r = 0; r < rows; r++) {
                for (let c = 0; c < cols; c++) {
                    if (this.objects.length >= this.totalObjects) break;
                    
                    // Jitter placement inside grid cell to prevent regular rows
                    const baseX = (c + 0.15 + Math.random() * 0.7) * cellW;
                    const baseY = (r + 0.15 + Math.random() * 0.7) * cellH;

                    const layer = Math.random() < 0.3 ? 0 : (Math.random() < 0.65 ? 1 : 2);
                    const type = types[idx % types.length];
                    const color = this.palette[Math.floor(Math.random() * this.palette.length)];
                    
                    const baseScale = layer === 0 ? (0.65 + Math.random() * 0.25) : 
                                      layer === 1 ? (0.95 + Math.random() * 0.35) : 
                                                    (1.3 + Math.random() * 0.45);

                    this.objects.push({
                        id: idx,
                        type: type,
                        x: baseX,
                        y: baseY,
                        origX: baseX,
                        origY: baseY,
                        vx: (Math.random() - 0.5) * 0.35,
                        vy: (Math.random() - 0.5) * 0.35,
                        layer: layer, // 0: back (0.04x), 1: mid (0.10x), 2: fore (0.18x)
                        parallaxFactor: layer === 0 ? 0.04 : layer === 1 ? 0.10 : 0.18,
                        scale: baseScale,
                        targetScale: baseScale,
                        baseScale: baseScale,
                        rotation: (Math.random() - 0.5) * 0.6,
                        rotSpeed: (Math.random() - 0.5) * 0.008,
                        color: color,
                        opacity: layer === 0 ? 0.28 : layer === 1 ? 0.50 : 0.75,
                        baseOpacity: layer === 0 ? 0.28 : layer === 1 ? 0.50 : 0.75,
                        glow: layer === 2 ? 8 : 4,
                        isHighlighted: false,
                        pulsePhase: Math.random() * Math.PI * 2
                    });

                    idx++;
                }
            }
        }

        render() {
            if (this.isPaused) return;

            // Smooth Mouse Interpolation
            this.mouseX += (this.targetMouseX - this.mouseX) * 0.08;
            this.mouseY += (this.targetMouseY - this.mouseY) * 0.08;

            const ctx = this.ctx;
            ctx.clearRect(0, 0, this.width, this.height);

            // 1. Draw Subtle Radial Cursor Glow
            this.drawCursorAura(ctx);

            // 2. Update and Draw Dynamic Constellation Connections
            if (this.mode !== 'quiet') {
                this.drawConnections(ctx);
            }

            // 3. Render Each Doodle Object with Layer Parallax & Proximity
            const time = Date.now() * 0.0015;
            for (let i = 0; i < this.objects.length; i++) {
                const obj = this.objects[i];
                this.updateObject(obj, time);
                this.drawDoodle(ctx, obj);
            }

            this.animFrameId = requestAnimationFrame(() => this.render());
        }

        drawCursorAura(ctx) {
            const auraRadius = 240 * this.intensity;
            const gradient = ctx.createRadialGradient(
                this.mouseX, this.mouseY, 0,
                this.mouseX, this.mouseY, auraRadius
            );
            
            gradient.addColorStop(0, 'rgba(108, 124, 255, 0.09)');
            gradient.addColorStop(0.5, 'rgba(166, 108, 255, 0.04)');
            gradient.addColorStop(1, 'rgba(0, 0, 0, 0)');

            ctx.fillStyle = gradient;
            ctx.fillRect(0, 0, this.width, this.height);
        }

        drawConnections(ctx) {
            const maxDistance = 140;
            ctx.lineWidth = 1;

            // Connect nearby objects to cursor
            for (let i = 0; i < this.objects.length; i++) {
                const obj = this.objects[i];
                const dx = this.mouseX - obj.x;
                const dy = this.mouseY - obj.y;
                const dist = Math.hypot(dx, dy);

                if (dist < 180) {
                    const alpha = (1 - dist / 180) * 0.25 * this.intensity;
                    ctx.strokeStyle = `rgba(108, 124, 255, ${alpha})`;
                    ctx.beginPath();
                    ctx.moveTo(this.mouseX, this.mouseY);
                    ctx.lineTo(obj.x, obj.y);
                    ctx.stroke();
                }

                // Connect neighbor objects
                for (let j = i + 1; j < this.objects.length; j++) {
                    const obj2 = this.objects[j];
                    const dxx = obj.x - obj2.x;
                    const dyy = obj.y - obj2.y;
                    const d = Math.hypot(dxx, dyy);

                    if (d < maxDistance) {
                        const alpha = (1 - d / maxDistance) * 0.12 * this.intensity;
                        ctx.strokeStyle = `rgba(166, 108, 255, ${alpha})`;
                        ctx.beginPath();
                        ctx.moveTo(obj.x, obj.y);
                        ctx.lineTo(obj2.x, obj2.y);
                        ctx.stroke();
                    }
                }
            }
        }

        updateObject(obj, time) {
            if (this.reducedMotion) return;

            // Slow floating drift
            obj.x += obj.vx;
            obj.y += obj.vy;

            // Wrap around edges
            const padding = 60;
            if (obj.x < -padding) obj.x = this.width + padding;
            if (obj.x > this.width + padding) obj.x = -padding;
            if (obj.y < -padding) obj.y = this.height + padding;
            if (obj.y > this.height + padding) obj.y = -padding;

            // Mouse Parallax Offset
            const parallaxX = (this.mouseX - this.width / 2) * obj.parallaxFactor;
            const parallaxY = (this.mouseY - this.height / 2) * obj.parallaxFactor;

            // Mouse Proximity Repulsion & Scale Flare
            const dx = obj.x - this.mouseX;
            const dy = obj.y - this.mouseY;
            const dist = Math.hypot(dx, dy);
            const proximityThreshold = 150;

            if (dist < proximityThreshold && dist > 0) {
                const force = (1 - dist / proximityThreshold) * 22;
                obj.x += (dx / dist) * force * 0.12;
                obj.y += (dy / dist) * force * 0.12;
                obj.targetScale = obj.baseScale * 1.35;
                obj.opacity = Math.min(1, obj.baseOpacity * 1.6);
            } else {
                obj.targetScale = obj.baseScale;
                obj.opacity += (obj.baseOpacity - obj.opacity) * 0.05;
            }

            obj.scale += (obj.targetScale - obj.scale) * 0.1;
            obj.rotation += obj.rotSpeed;

            // Subtle Intelligence Pulse
            if (obj.isHighlighted || (this.mode === 'ai' && (obj.type === 'aiRobot' || obj.type === 'neuralNode'))) {
                obj.scale = obj.baseScale * (1.2 + Math.sin(time * 4 + obj.pulsePhase) * 0.15);
                obj.opacity = 1.0;
            }
        }

        drawDoodle(ctx, obj) {
            ctx.save();
            ctx.translate(obj.x, obj.y);
            ctx.rotate(obj.rotation);
            ctx.scale(obj.scale * this.intensity, obj.scale * this.intensity);

            ctx.strokeStyle = obj.color;
            ctx.fillStyle = 'transparent';
            ctx.lineWidth = 1.6;
            ctx.lineCap = 'round';
            ctx.lineJoin = 'round';
            ctx.globalAlpha = obj.opacity;

            ctx.shadowColor = obj.color;
            ctx.shadowBlur = obj.isHighlighted ? 18 : obj.glow;

            // Render specific doodle path
            switch (obj.type) {
                case 'smartphone': this.drawSmartphone(ctx); break;
                case 'laptop': this.drawLaptop(ctx); break;
                case 'headphones': this.drawHeadphones(ctx); break;
                case 'earbuds': this.drawEarbuds(ctx); break;
                case 'smartwatch': this.drawSmartwatch(ctx); break;
                case 'camera': this.drawCamera(ctx); break;
                case 'shoppingCart': this.drawShoppingCart(ctx); break;
                case 'shoppingBag': this.drawShoppingBag(ctx); break;
                case 'deliveryTruck': this.drawDeliveryTruck(ctx); break;
                case 'packageBox': this.drawPackageBox(ctx); break;
                case 'barcode': this.drawBarcode(ctx); break;
                case 'aiRobot': this.drawAiRobot(ctx); break;
                case 'neuralNode': this.drawNeuralNode(ctx); break;
                case 'microchip': this.drawMicrochip(ctx); break;
                case 'gameController': this.drawGameController(ctx); break;
                case 'starRating': this.drawStarRating(ctx); break;
                case 'wishlistHeart': this.drawWishlistHeart(ctx); break;
                case 'chatBubble': this.drawChatBubble(ctx); break;
                case 'amazonSmile': this.drawAmazonSmile(ctx); break;
                default: this.drawSparkle(ctx); break;
            }

            ctx.restore();
        }

        // ================= Vector Line Art Procedural Paths ================= //

        drawSmartphone(ctx) {
            ctx.beginPath();
            ctx.roundRect(-12, -22, 24, 44, 5);
            ctx.stroke();
            // Screen notch & home bar
            ctx.beginPath();
            ctx.arc(0, -17, 1.5, 0, Math.PI * 2);
            ctx.moveTo(-6, 17);
            ctx.lineTo(6, 17);
            ctx.stroke();
        }

        drawLaptop(ctx) {
            ctx.beginPath();
            ctx.roundRect(-18, -14, 36, 22, 3); // Screen
            ctx.moveTo(-24, 10);
            ctx.lineTo(24, 10); // Base
            ctx.lineTo(21, 14);
            ctx.lineTo(-21, 14);
            ctx.closePath();
            ctx.stroke();
            // Keyboard line
            ctx.beginPath();
            ctx.moveTo(-10, 12);
            ctx.lineTo(10, 12);
            ctx.stroke();
        }

        drawHeadphones(ctx) {
            ctx.beginPath();
            ctx.arc(0, 0, 16, Math.PI, 0); // Headband
            ctx.stroke();
            ctx.beginPath();
            ctx.roundRect(-19, -2, 6, 14, 3); // Left earcup
            ctx.roundRect(13, -2, 6, 14, 3);  // Right earcup
            ctx.stroke();
        }

        drawEarbuds(ctx) {
            ctx.beginPath();
            ctx.arc(-8, -6, 5, 0, Math.PI * 2);
            ctx.moveTo(-8, -1);
            ctx.lineTo(-8, 12);
            ctx.arc(8, -6, 5, 0, Math.PI * 2);
            ctx.moveTo(8, -1);
            ctx.lineTo(8, 12);
            ctx.stroke();
        }

        drawSmartwatch(ctx) {
            ctx.beginPath();
            ctx.roundRect(-12, -12, 24, 24, 6); // Dial
            ctx.stroke();
            ctx.beginPath();
            ctx.moveTo(-7, -12); ctx.lineTo(-7, -20); ctx.lineTo(7, -20); ctx.lineTo(7, -12); // Top strap
            ctx.moveTo(-7, 12); ctx.lineTo(-7, 20); ctx.lineTo(7, 20); ctx.lineTo(7, 12);   // Bottom strap
            ctx.stroke();
            // Pulse wave
            ctx.beginPath();
            ctx.moveTo(-6, 0); ctx.lineTo(-2, 0); ctx.lineTo(0, -4); ctx.lineTo(2, 4); ctx.lineTo(4, 0); ctx.lineTo(6, 0);
            ctx.stroke();
        }

        drawCamera(ctx) {
            ctx.beginPath();
            ctx.roundRect(-18, -10, 36, 22, 4); // Body
            ctx.roundRect(-6, -14, 12, 4, 1);   // Viewfinder top
            ctx.stroke();
            ctx.beginPath();
            ctx.arc(0, 1, 7, 0, Math.PI * 2);  // Lens
            ctx.stroke();
        }

        drawShoppingCart(ctx) {
            ctx.beginPath();
            ctx.moveTo(-16, -14);
            ctx.lineTo(-10, -14);
            ctx.lineTo(-4, 6);
            ctx.lineTo(14, 6);
            ctx.lineTo(18, -8);
            ctx.lineTo(-7, -8);
            ctx.stroke();
            // Wheels
            ctx.beginPath();
            ctx.arc(-2, 11, 2.5, 0, Math.PI * 2);
            ctx.arc(12, 11, 2.5, 0, Math.PI * 2);
            ctx.stroke();
        }

        drawShoppingBag(ctx) {
            ctx.beginPath();
            ctx.roundRect(-14, -10, 28, 26, 4); // Bag body
            ctx.stroke();
            ctx.beginPath();
            ctx.arc(0, -10, 6, Math.PI, 0); // Handle
            ctx.stroke();
            // Tag crease
            ctx.beginPath();
            ctx.moveTo(-4, 0); ctx.lineTo(4, 0);
            ctx.stroke();
        }

        drawDeliveryTruck(ctx) {
            ctx.beginPath();
            ctx.roundRect(-18, -12, 22, 18, 2); // Cargo box
            ctx.moveTo(4, -4);
            ctx.lineTo(14, -4);
            ctx.lineTo(18, 6);
            ctx.lineTo(4, 6);
            ctx.closePath(); // Cabin
            ctx.stroke();
            // Wheels
            ctx.beginPath();
            ctx.arc(-8, 9, 3, 0, Math.PI * 2);
            ctx.arc(11, 9, 3, 0, Math.PI * 2);
            ctx.stroke();
        }

        drawPackageBox(ctx) {
            ctx.beginPath();
            // Isometric box
            ctx.moveTo(0, -16);
            ctx.lineTo(14, -8);
            ctx.lineTo(14, 8);
            ctx.lineTo(0, 16);
            ctx.lineTo(-14, 8);
            ctx.lineTo(-14, -8);
            ctx.closePath();
            ctx.moveTo(0, -16); ctx.lineTo(0, 16);
            ctx.moveTo(0, 0); ctx.lineTo(14, -8);
            ctx.moveTo(0, 0); ctx.lineTo(-14, -8);
            ctx.stroke();
        }

        drawBarcode(ctx) {
            ctx.beginPath();
            ctx.roundRect(-15, -12, 30, 24, 3);
            ctx.stroke();
            ctx.beginPath();
            ctx.moveTo(-10, -7); ctx.lineTo(-10, 7);
            ctx.moveTo(-6, -7); ctx.lineTo(-6, 7);
            ctx.moveTo(-2, -7); ctx.lineTo(-2, 7);
            ctx.moveTo(2, -7); ctx.lineTo(2, 7);
            ctx.moveTo(6, -7); ctx.lineTo(6, 7);
            ctx.moveTo(10, -7); ctx.lineTo(10, 7);
            ctx.stroke();
        }

        drawAiRobot(ctx) {
            ctx.beginPath();
            ctx.roundRect(-14, -12, 28, 24, 5); // Head
            ctx.moveTo(0, -12); ctx.lineTo(0, -18); // Antenna
            ctx.stroke();
            ctx.beginPath();
            ctx.arc(0, -18, 2, 0, Math.PI * 2); // Antenna tip
            ctx.arc(-6, -2, 2.5, 0, Math.PI * 2); // Eye L
            ctx.arc(6, -2, 2.5, 0, Math.PI * 2);  // Eye R
            ctx.moveTo(-6, 6); ctx.lineTo(6, 6); // Mouth
            ctx.stroke();
        }

        drawNeuralNode(ctx) {
            ctx.beginPath();
            ctx.arc(0, 0, 4, 0, Math.PI * 2);
            ctx.arc(-12, -10, 3, 0, Math.PI * 2);
            ctx.arc(12, -10, 3, 0, Math.PI * 2);
            ctx.arc(-10, 12, 3, 0, Math.PI * 2);
            ctx.arc(10, 12, 3, 0, Math.PI * 2);
            ctx.stroke();
            ctx.beginPath();
            ctx.moveTo(-12, -10); ctx.lineTo(0, 0);
            ctx.moveTo(12, -10); ctx.lineTo(0, 0);
            ctx.moveTo(-10, 12); ctx.lineTo(0, 0);
            ctx.moveTo(10, 12); ctx.lineTo(0, 0);
            ctx.stroke();
        }

        drawMicrochip(ctx) {
            ctx.beginPath();
            ctx.roundRect(-12, -12, 24, 24, 3);
            ctx.stroke();
            ctx.beginPath();
            // Pins
            ctx.moveTo(-16, -6); ctx.lineTo(-12, -6);
            ctx.moveTo(-16, 0); ctx.lineTo(-12, 0);
            ctx.moveTo(-16, 6); ctx.lineTo(-12, 6);
            ctx.moveTo(12, -6); ctx.lineTo(16, -6);
            ctx.moveTo(12, 0); ctx.lineTo(16, 0);
            ctx.moveTo(12, 6); ctx.lineTo(16, 6);
            ctx.moveTo(-6, -16); ctx.lineTo(-6, -12);
            ctx.moveTo(0, -16); ctx.lineTo(0, -12);
            ctx.moveTo(6, -16); ctx.lineTo(6, -12);
            ctx.moveTo(-6, 12); ctx.lineTo(-6, 16);
            ctx.moveTo(0, 12); ctx.lineTo(0, 16);
            ctx.moveTo(6, 12); ctx.lineTo(6, 16);
            ctx.stroke();
        }

        drawGameController(ctx) {
            ctx.beginPath();
            ctx.roundRect(-18, -10, 36, 20, 8); // Controller body
            ctx.stroke();
            // D-Pad + Buttons
            ctx.beginPath();
            ctx.moveTo(-10, -3); ctx.lineTo(-10, 3);
            ctx.moveTo(-13, 0); ctx.lineTo(-7, 0);
            ctx.arc(10, -2, 1.5, 0, Math.PI * 2);
            ctx.arc(8, 2, 1.5, 0, Math.PI * 2);
            ctx.stroke();
        }

        drawStarRating(ctx) {
            const spikes = 5;
            const outerR = 12;
            const innerR = 5;
            let rot = Math.PI / 2 * 3;
            const step = Math.PI / spikes;

            ctx.beginPath();
            ctx.moveTo(0, -outerR);
            for (let i = 0; i < spikes; i++) {
                let x = Math.cos(rot) * outerR;
                let y = Math.sin(rot) * outerR;
                ctx.lineTo(x, y);
                rot += step;
                x = Math.cos(rot) * innerR;
                y = Math.sin(rot) * innerR;
                ctx.lineTo(x, y);
                rot += step;
            }
            ctx.closePath();
            ctx.stroke();
        }

        drawWishlistHeart(ctx) {
            ctx.beginPath();
            ctx.moveTo(0, 10);
            ctx.bezierCurveTo(-14, 0, -14, -12, 0, -6);
            ctx.bezierCurveTo(14, -12, 14, 0, 0, 10);
            ctx.stroke();
        }

        drawChatBubble(ctx) {
            ctx.beginPath();
            ctx.roundRect(-14, -12, 28, 20, 5);
            ctx.moveTo(-6, 8);
            ctx.lineTo(-10, 14);
            ctx.lineTo(-2, 8);
            ctx.stroke();
            // 3 dots
            ctx.beginPath();
            ctx.arc(-5, -2, 1.5, 0, Math.PI * 2);
            ctx.arc(0, -2, 1.5, 0, Math.PI * 2);
            ctx.arc(5, -2, 1.5, 0, Math.PI * 2);
            ctx.stroke();
        }

        drawAmazonSmile(ctx) {
            // Subtle smile arrow
            ctx.beginPath();
            ctx.arc(0, -4, 14, 0.25 * Math.PI, 0.75 * Math.PI, false);
            ctx.stroke();
            // Arrow tip
            ctx.beginPath();
            ctx.moveTo(8, 7);
            ctx.lineTo(12, 4);
            ctx.lineTo(10, 1);
            ctx.stroke();
        }

        drawSparkle(ctx) {
            ctx.beginPath();
            ctx.moveTo(0, -12);
            ctx.quadraticCurveTo(0, 0, 12, 0);
            ctx.quadraticCurveTo(0, 0, 0, 12);
            ctx.quadraticCurveTo(0, 0, -12, 0);
            ctx.quadraticCurveTo(0, 0, 0, -12);
            ctx.stroke();
        }

        // ================= Public API ================= //

        setMode(mode) {
            this.mode = mode;
            if (mode === 'quiet') {
                this.intensity = 0.4;
            } else if (mode === 'ai') {
                this.intensity = 1.3;
            } else {
                this.intensity = 1.0;
            }
        }

        setIntensity(val) {
            this.intensity = Math.max(0.1, Math.min(2.0, val));
        }

        highlightProduct(categoryOrType) {
            if (!categoryOrType) return;
            const term = categoryOrType.toLowerCase();
            let typeToHighlight = null;
            if (term.includes('laptop') || term.includes('legion') || term.includes('macbook') || term.includes('tuf')) typeToHighlight = 'laptop';
            else if (term.includes('phone') || term.includes('iphone') || term.includes('galaxy') || term.includes('s24') || term.includes('nord')) typeToHighlight = 'smartphone';
            else if (term.includes('headphone') || term.includes('anc') || term.includes('audio') || term.includes('wh-1000')) typeToHighlight = 'headphones';
            else if (term.includes('earbud') || term.includes('airpods') || term.includes('tws')) typeToHighlight = 'earbuds';
            else if (term.includes('watch')) typeToHighlight = 'smartwatch';
            else if (term.includes('camera') || term.includes('lens') || term.includes('sony a')) typeToHighlight = 'camera';
            else if (term.includes('game') || term.includes('controller') || term.includes('playstation') || term.includes('xbox')) typeToHighlight = 'gameController';
            else if (term.includes('cart') || term.includes('order')) typeToHighlight = 'shoppingCart';
            else if (term.includes('ai') || term.includes('assistant') || term.includes('smart')) typeToHighlight = 'aiRobot';
            
            if (!typeToHighlight) {
                typeToHighlight = term;
            }

            this.objects.forEach(obj => {
                if (obj.type.toLowerCase().includes(typeToHighlight) || typeToHighlight.includes(obj.type.toLowerCase())) {
                    obj.isHighlighted = true;
                    obj.color = '#F59E0B'; // Glowing amber highlight
                    obj.glow = 25;
                }
            });

            clearTimeout(this.highlightTimer);
            this.highlightTimer = setTimeout(() => {
                this.objects.forEach(obj => {
                    obj.isHighlighted = false;
                    obj.color = this.palette[Math.floor(Math.random() * this.palette.length)];
                    obj.glow = obj.layer === 2 ? 8 : 4;
                });
            }, 6000);
        }

        pause() {
            this.isPaused = true;
            if (this.animFrameId) cancelAnimationFrame(this.animFrameId);
        }

        resume() {
            if (this.isPaused) {
                this.isPaused = false;
                this.render();
            }
        }
    }

    // Expose global API
    document.addEventListener('DOMContentLoaded', () => {
        window.wallpaper = new LiveWallpaper();
    });
})();
