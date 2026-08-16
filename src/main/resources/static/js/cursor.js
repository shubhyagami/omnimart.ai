/**
 * OmniMart AI - Animated Custom Shopping Cursor Engine
 */
(function() {
    // Only initialize on devices with fine pointer (mouse)
    if (window.matchMedia('(pointer: coarse)').matches) {
        return;
    }

    document.addEventListener('DOMContentLoaded', () => {
        // 1. Create Cursor Elements
        const dot = document.createElement('div');
        dot.className = 'omni-cursor-dot';

        const follower = document.createElement('div');
        follower.className = 'omni-cursor-follower';
        follower.innerHTML = '<i class="fa-solid fa-bag-shopping cursor-icon"></i>';

        document.body.appendChild(dot);
        document.body.appendChild(follower);

        let mouseX = window.innerWidth / 2;
        let mouseY = window.innerHeight / 2;
        let followerX = mouseX;
        let followerY = mouseY;
        let isVisible = false;

        // 2. Mouse Move Listener
        window.addEventListener('mousemove', (e) => {
            mouseX = e.clientX;
            mouseY = e.clientY;

            if (!isVisible) {
                isVisible = true;
                dot.style.opacity = '1';
                follower.style.opacity = '1';
            }

            dot.style.left = `${mouseX}px`;
            dot.style.top = `${mouseY}px`;
        });

        // 3. Smooth Lerp Animation Loop for Trailing Follower
        function animateFollower() {
            // Lerp factor
            followerX += (mouseX - followerX) * 0.18;
            followerY += (mouseY - followerY) * 0.18;

            follower.style.left = `${followerX}px`;
            follower.style.top = `${followerY}px`;

            requestAnimationFrame(animateFollower);
        }
        requestAnimationFrame(animateFollower);

        // 4. Hover Listeners on Interactive Elements
        function setupHoverListeners() {
            const targets = document.querySelectorAll('a, button, input, select, textarea, .omni-card, .btn-add-compare, .omni-chip, .badge, [role="button"]');
            
            targets.forEach(target => {
                target.addEventListener('mouseenter', () => {
                    document.body.classList.add('cursor-hover');
                });
                target.addEventListener('mouseleave', () => {
                    document.body.classList.remove('cursor-hover');
                });
            });
        }
        setupHoverListeners();

        // Observe dynamic DOM changes (e.g. Chat messages, AJAX filters)
        const observer = new MutationObserver(() => {
            setupHoverListeners();
        });
        observer.observe(document.body, { childList: true, subtree: true });

        // 5. Click Particle Burst Effect
        window.addEventListener('click', (e) => {
            document.body.classList.add('cursor-click');
            setTimeout(() => document.body.classList.remove('cursor-click'), 200);

            createClickParticles(e.clientX, e.clientY);
        });

        function createClickParticles(x, y) {
            const count = 8;
            const colors = ['#f59e0b', '#fbbf24', '#10b981', '#38bdf8', '#ffffff'];

            for (let i = 0; i < count; i++) {
                const particle = document.createElement('div');
                particle.className = 'omni-click-particle';
                
                const size = Math.floor(Math.random() * 6) + 4;
                const angle = Math.random() * Math.PI * 2;
                const distance = Math.floor(Math.random() * 45) + 25;
                const tx = `${Math.cos(angle) * distance}px`;
                const ty = `${Math.sin(angle) * distance}px`;
                const color = colors[Math.floor(Math.random() * colors.length)];

                particle.style.width = `${size}px`;
                particle.style.height = `${size}px`;
                particle.style.background = color;
                particle.style.boxShadow = `0 0 8px ${color}`;
                particle.style.left = `${x}px`;
                particle.style.top = `${y}px`;
                particle.style.setProperty('--tx', tx);
                particle.style.setProperty('--ty', ty);

                document.body.appendChild(particle);

                setTimeout(() => {
                    particle.remove();
                }, 650);
            }
        }

        // Hide when mouse leaves window
        document.addEventListener('mouseleave', () => {
            dot.style.opacity = '0';
            follower.style.opacity = '0';
            isVisible = false;
        });
    });
})();
