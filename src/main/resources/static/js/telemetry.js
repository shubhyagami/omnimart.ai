// OmniMart Behavioral Telemetry Tracker

let pageStartTime = Date.now();

document.addEventListener('DOMContentLoaded', () => {
    // Track clicks on products
    document.querySelectorAll('.omni-card, .omni-card-title').forEach(card => {
        card.addEventListener('click', () => {
            const prodId = card.getAttribute('data-product-id');
            if (prodId) {
                sendTelemetry('PRODUCT_CLICK', prodId, 0);
            }
        });
    });
});

// Track dwell time on window unload or visibility change
window.addEventListener('beforeunload', () => {
    const dwellSeconds = Math.round((Date.now() - pageStartTime) / 1000);
    const detailProdEl = document.getElementById('productDetailContainer');
    if (detailProdEl && dwellSeconds > 2) {
        const prodId = detailProdEl.getAttribute('data-product-id');
        if (prodId) {
            navigator.sendBeacon('/api/telemetry/interaction', JSON.stringify({
                eventType: 'DWELL_TIME',
                productId: parseInt(prodId),
                dwellTimeSeconds: dwellSeconds
            }));
        }
    }
});

function sendTelemetry(eventType, productId, dwellTime) {
    fetch('/api/telemetry/interaction', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
            eventType: eventType,
            productId: productId ? parseInt(productId) : null,
            dwellTimeSeconds: dwellTime || 0
        })
    }).catch(() => {});
}
