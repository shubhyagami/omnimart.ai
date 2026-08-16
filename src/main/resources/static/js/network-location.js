/**
 * OmniMart AI - Network IP Geolocation & Live Mini-Map Engine
 * Resolves user's network location, public IP, pincode, and renders network-based live map without GPS.
 */
(function() {
    'use strict';

    let mapInstance = null;
    let markerInstance = null;
    let currentLocationData = {
        ip: '106.219.133.195',
        city: 'Gurugram',
        region: 'Haryana',
        postal: '122001',
        country: 'India',
        lat: 28.4595,
        lon: 77.0266,
        isp: 'Bharti Airtel Broadband'
    };

    document.addEventListener('DOMContentLoaded', () => {
        initLocationDetection();
        setupLocationModalListener();
    });

    async function initLocationDetection() {
        const deliverTextEl = document.getElementById('deliverToLocation');
        
        // 1. Check Session Cache first for instant render
        const cached = sessionStorage.getItem('omnimart_network_location');
        if (cached) {
            try {
                currentLocationData = JSON.parse(cached);
                updatePillUI(currentLocationData);
            } catch (e) {}
        }

        // 2. Fetch fresh network IP location
        try {
            // Using ipwho.is (CORS enabled, fast, no API key required)
            const response = await fetch('https://ipwho.is/?fields=ip,success,city,region,postal,country,latitude,longitude,connection');
            const data = await response.json();

            if (data && data.success) {
                currentLocationData = {
                    ip: data.ip || '106.219.133.195',
                    city: data.city || 'Gurugram',
                    region: data.region || 'Haryana',
                    postal: data.postal || '122001',
                    country: data.country || 'India',
                    lat: data.latitude || 28.4595,
                    lon: data.longitude || 77.0266,
                    isp: data.connection && data.connection.isp ? data.connection.isp : 'High-Speed Broadband'
                };

                sessionStorage.setItem('omnimart_network_location', JSON.stringify(currentLocationData));
                updatePillUI(currentLocationData);
            } else {
                fallbackIpApi();
            }
        } catch (err) {
            fallbackIpApi();
        }
    }

    async function fallbackIpApi() {
        try {
            const res = await fetch('https://ipapi.co/json/');
            const data = await res.json();
            if (data && !data.error) {
                currentLocationData = {
                    ip: data.ip || '106.219.133.195',
                    city: data.city || 'Delhi',
                    region: data.region || 'Delhi',
                    postal: data.postal || '110001',
                    country: data.country_name || 'India',
                    lat: data.latitude || 28.6139,
                    lon: data.longitude || 77.2090,
                    isp: data.org || 'Internet Service Provider'
                };
                sessionStorage.setItem('omnimart_network_location', JSON.stringify(currentLocationData));
                updatePillUI(currentLocationData);
            }
        } catch (e) {
            // Default preset
            updatePillUI(currentLocationData);
        }
    }

    function updatePillUI(loc) {
        const deliverTextEl = document.getElementById('deliverToLocation');
        if (deliverTextEl) {
            deliverTextEl.textContent = `${loc.city} • ${loc.postal || '122001'}`;
        }
    }

    function setupLocationModalListener() {
        const modalEl = document.getElementById('networkLocationModal');
        if (!modalEl) return;

        modalEl.addEventListener('shown.bs.modal', () => {
            renderModalData(currentLocationData);
            renderLeafletMap(currentLocationData.lat, currentLocationData.lon, currentLocationData.city, currentLocationData.postal);
        });

        // Copy IP button
        const copyBtn = document.getElementById('btnCopyPublicIp');
        if (copyBtn) {
            copyBtn.addEventListener('click', () => {
                const ipText = document.getElementById('modalPublicIp').textContent;
                navigator.clipboard.writeText(ipText).then(() => {
                    copyBtn.innerHTML = '<i class="fa-solid fa-check text-success"></i> Copied';
                    setTimeout(() => {
                        copyBtn.innerHTML = '<i class="fa-regular fa-copy"></i> Copy';
                    }, 2000);
                });
            });
        }

        // Manual Pincode Update
        const pincodeForm = document.getElementById('formUpdatePincode');
        if (pincodeForm) {
            pincodeForm.addEventListener('submit', (e) => {
                e.preventDefault();
                const pinInput = document.getElementById('inputCustomPincode');
                const cityInput = document.getElementById('inputCustomCity');
                const newPin = pinInput ? pinInput.value.trim() : '';
                const newCity = cityInput ? cityInput.value.trim() : '';
                if (newPin) {
                    currentLocationData.postal = newPin;
                    if (newCity) currentLocationData.city = newCity;
                    sessionStorage.setItem('omnimart_network_location', JSON.stringify(currentLocationData));
                    updatePillUI(currentLocationData);
                    renderModalData(currentLocationData);
                    
                    const alertEl = document.getElementById('pincodeUpdateAlert');
                    if (alertEl) {
                        alertEl.classList.remove('d-none');
                        setTimeout(() => alertEl.classList.add('d-none'), 3000);
                    }
                }
            });
        }
    }

    function renderModalData(loc) {
        const ipEl = document.getElementById('modalPublicIp');
        const ispEl = document.getElementById('modalNetworkIsp');
        const cityEl = document.getElementById('modalCityRegion');
        const pinEl = document.getElementById('modalPincode');
        const coordsEl = document.getElementById('modalCoords');

        if (ipEl) ipEl.textContent = loc.ip;
        if (ispEl) ispEl.textContent = loc.isp;
        if (cityEl) cityEl.textContent = `${loc.city}, ${loc.region}, ${loc.country}`;
        if (pinEl) pinEl.textContent = loc.postal || '122001';
        if (coordsEl) coordsEl.textContent = `${loc.lat.toFixed(4)}° N, ${loc.lon.toFixed(4)}° E (Network Triangulation)`;
    }

    function renderLeafletMap(lat, lon, city, postal) {
        const mapContainer = document.getElementById('networkMiniMap');
        if (!mapContainer) return;

        if (typeof L === 'undefined') {
            mapContainer.innerHTML = `<iframe width="100%" height="220" frameborder="0" scrolling="no" src="https://www.openstreetmap.org/export/embed.html?bbox=${lon-0.08}%2C${lat-0.08}%2C${lon+0.08}%2C${lat+0.08}&layer=mapnik&marker=${lat}%2C${lon}" style="border-radius: 12px;"></iframe>`;
            return;
        }

        if (!mapInstance) {
            mapInstance = L.map('networkMiniMap', {
                center: [lat, lon],
                zoom: 13,
                zoomControl: false
            });

            // Modern Dark / CartoDB Voyager Map Tiles
            L.tileLayer('https://{s}.basemaps.cartocdn.com/rastertiles/voyager/{z}/{x}/{y}{r}.png', {
                attribution: '&copy; OpenStreetMap &copy; CARTO',
                maxZoom: 19
            }).addTo(mapInstance);

            L.control.zoom({ position: 'topright' }).addTo(mapInstance);
        } else {
            mapInstance.setView([lat, lon], 13);
            mapInstance.invalidateSize();
        }

        if (markerInstance) {
            markerInstance.remove();
        }

        // Custom Glowing Neon Marker
        const customIcon = L.divIcon({
            className: 'custom-network-marker',
            html: `<div style="background: #f59e0b; width: 18px; height: 18px; border-radius: 50%; border: 3px solid #ffffff; box-shadow: 0 0 15px #f59e0b, 0 0 25px #f59e0b;"></div>`,
            iconSize: [18, 18],
            iconAnchor: [9, 9]
        });

        markerInstance = L.marker([lat, lon], { icon: customIcon }).addTo(mapInstance)
            .bindPopup(`<strong>${city}</strong><br>Pincode: ${postal || '122001'}<br><small class="text-muted">Network Node</small>`)
            .openPopup();
    }
})();
