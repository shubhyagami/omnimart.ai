// OmniMart AI Shopping Assistant JavaScript

let omniConversationId = localStorage.getItem('omni_chat_conversation_id') || '';

document.addEventListener('DOMContentLoaded', () => {
    const floatingBtn = document.getElementById('omniChatFloatingBtn');
    const chatWindow = document.getElementById('omniChatWindow');
    const closeBtn = document.getElementById('omniChatCloseBtn');
    const sendBtn = document.getElementById('omniChatSendBtn');
    const inputField = document.getElementById('omniChatInput');

    if (floatingBtn && chatWindow) {
        floatingBtn.addEventListener('click', () => {
            const isVisible = chatWindow.style.display === 'flex';
            chatWindow.style.display = isVisible ? 'none' : 'flex';
            if (!isVisible && inputField) {
                inputField.focus();
            }
        });
    }

    if (closeBtn && chatWindow) {
        closeBtn.addEventListener('click', () => {
            chatWindow.style.display = 'none';
        });
    }

    if (sendBtn && inputField) {
        sendBtn.addEventListener('click', () => submitChatMessage());
        inputField.addEventListener('keypress', (e) => {
            if (e.key === 'Enter') {
                e.preventDefault();
                submitChatMessage();
            }
        });
    }
});

function submitChatMessage(customText) {
    const inputField = document.getElementById('omniChatInput');
    const messagesContainer = document.getElementById('omniChatMessages');
    const chipsContainer = document.getElementById('omniChatChips');

    const text = customText || (inputField ? inputField.value.trim() : '');
    if (!text) return;

    if (inputField) inputField.value = '';

    // Append User Message Bubble
    appendUserMessage(text);

    // Show Typing Indicator
    const typingId = showTypingIndicator();

    // Extract current product ID from URL or page context if viewing a product
    let currentProdId = null;
    const pathMatch = window.location.pathname.match(/\/products\/(\d+)/);
    if (pathMatch && pathMatch[1]) {
        currentProdId = parseInt(pathMatch[1], 10);
    }

    // Prepare Payload
    const payload = {
        conversationId: omniConversationId,
        message: text,
        currentProductId: currentProdId
    };

    fetch('/api/chat', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(payload)
    })
    .then(response => response.json())
    .then(data => {
        removeTypingIndicator(typingId);
        if (data.conversationId) {
            omniConversationId = data.conversationId;
            localStorage.setItem('omni_chat_conversation_id', omniConversationId);
        }

        // Update provider label
        const providerBadge = document.getElementById('omniChatProviderBadge');
        if (providerBadge && data.provider) {
            providerBadge.textContent = data.provider.split(' ')[0];
        }

        appendAssistantMessage(data);
        updateFollowUpChips(data.suggestedFollowUps);
    })
    .catch(err => {
        removeTypingIndicator(typingId);
        appendAssistantMessage({
            message: "I'm having trouble connecting to the AI engine right now. Please try again.",
            reasoningSummary: null,
            products: []
        });
    });
}

function appendUserMessage(text) {
    const container = document.getElementById('omniChatMessages');
    if (!container) return;

    const div = document.createElement('div');
    div.className = 'omni-msg omni-msg-user';
    div.innerHTML = `<div class="omni-msg-bubble">${escapeHtml(text)}</div>`;
    container.appendChild(div);
    container.scrollTop = container.scrollHeight;
}

function appendAssistantMessage(data) {
    const container = document.getElementById('omniChatMessages');
    if (!container) return;

    const div = document.createElement('div');
    div.className = 'omni-msg omni-msg-ai';

    let reasoningHtml = '';
    if (data.reasoningSummary) {
        reasoningHtml = `<div class="omni-msg-reasoning"><i class="fa-solid fa-brain text-primary me-1"></i> ${escapeHtml(data.reasoningSummary)}</div>`;
    }

    if (data.products && data.products.length > 0) {
        if (window.wallpaper) {
            window.wallpaper.highlightProduct(data.products[0].name);
        }
        productsHtml = `<div class="omni-chat-products">`;
        data.products.forEach(p => {
            productsHtml += `
                <div class="omni-chat-product-card">
                    <img src="${p.primaryImageUrl || '/images/placeholder.jpg'}" alt="${escapeHtml(p.name)}" class="omni-chat-product-img" onerror="this.src='https://placehold.co/200x200?text=Product'">
                    <div class="omni-chat-product-name" title="${escapeHtml(p.name)}">${escapeHtml(p.name)}</div>
                    <div class="omni-chat-product-price">₹${p.price.toLocaleString()}</div>
                    <div class="text-warning small mb-2"><i class="fa-solid fa-star"></i> ${p.rating}★</div>
                    <div class="omni-chat-card-actions">
                        <a href="/products/${p.id}" class="omni-chat-btn-view" target="_blank">View</a>
                        <button type="button" class="omni-chat-btn-add" onclick="chatAddToCart(${p.id})">Add</button>
                    </div>
                </div>
            `;
        });
        productsHtml += `</div>`;
    }

    div.innerHTML = `
        <div class="omni-msg-bubble">
            <div>${escapeHtml(data.message)}</div>
            ${reasoningHtml}
            ${productsHtml}
        </div>
    `;

    container.appendChild(div);
    container.scrollTop = container.scrollHeight;
}

function updateFollowUpChips(chips) {
    const container = document.getElementById('omniChatChips');
    if (!container) return;

    container.innerHTML = '';
    if (chips && chips.length > 0) {
        chips.forEach(chipText => {
            const btn = document.createElement('button');
            btn.className = 'omni-chip';
            btn.textContent = chipText;
            btn.onclick = () => submitChatMessage(chipText);
            container.appendChild(btn);
        });
    }
}

function showTypingIndicator() {
    const container = document.getElementById('omniChatMessages');
    if (!container) return null;

    const id = 'typing_' + Date.now();
    const div = document.createElement('div');
    div.id = id;
    div.className = 'omni-msg omni-msg-ai';
    div.innerHTML = `
        <div class="omni-typing-indicator">
            <div class="omni-typing-dot"></div>
            <div class="omni-typing-dot"></div>
            <div class="omni-typing-dot"></div>
        </div>
    `;
    container.appendChild(div);
    container.scrollTop = container.scrollHeight;
    return id;
}

function removeTypingIndicator(id) {
    if (!id) return;
    const el = document.getElementById(id);
    if (el) el.remove();
}

function chatAddToCart(productId) {
    const form = document.createElement('form');
    form.method = 'POST';
    form.action = '/cart/add';

    const input = document.createElement('input');
    input.type = 'hidden';
    input.name = 'productId';
    input.value = productId;
    form.appendChild(input);

    const qtyInput = document.createElement('input');
    qtyInput.type = 'hidden';
    qtyInput.name = 'quantity';
    qtyInput.value = '1';
    form.appendChild(qtyInput);

    document.body.appendChild(form);
    form.submit();
}

function escapeHtml(string) {
    if (!string) return '';
    return String(string).replace(/&/g, '&amp;').replace(/</g, '&lt;').replace(/>/g, '&gt;').replace(/"/g, '&quot;');
}
