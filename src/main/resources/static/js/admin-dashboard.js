// OmniMart Admin Analytics & AI Intelligence Dashboard

document.addEventListener('DOMContentLoaded', () => {
    initCharts();

    const askBtn = document.getElementById('adminAiAskBtn');
    const input = document.getElementById('adminAiQuestionInput');

    if (askBtn && input) {
        askBtn.addEventListener('click', () => submitAdminAiQuestion());
        input.addEventListener('keypress', (e) => {
            if (e.key === 'Enter') {
                e.preventDefault();
                submitAdminAiQuestion();
            }
        });
    }

    document.querySelectorAll('.admin-ai-prompt-chip').forEach(chip => {
        chip.addEventListener('click', () => {
            if (input) input.value = chip.textContent.trim();
            submitAdminAiQuestion();
        });
    });
});

function initCharts() {
    fetch('/api/admin/analytics-data')
        .then(res => res.json())
        .then(data => {
            renderSentimentChart(data.sentimentDistribution);
            renderTopicChart(data.topComplaintTopics);
        })
        .catch(() => {});
}

function renderSentimentChart(distribution) {
    const ctx = document.getElementById('sentimentChart');
    if (!ctx || !distribution) return;

    new Chart(ctx, {
        type: 'doughnut',
        data: {
            labels: Object.keys(distribution),
            datasets: [{
                data: Object.values(distribution),
                backgroundColor: ['#10b981', '#ef4444', '#f59e0b', '#64748b']
            }]
        },
        options: {
            responsive: true,
            plugins: {
                legend: { position: 'bottom' }
            }
        }
    });
}

function renderTopicChart(topics) {
    const ctx = document.getElementById('topicsChart');
    if (!ctx || !topics) return;

    new Chart(ctx, {
        type: 'bar',
        data: {
            labels: Object.keys(topics),
            datasets: [{
                label: 'Negative Complaints Volume',
                data: Object.values(topics),
                backgroundColor: '#ef4444'
            }]
        },
        options: {
            responsive: true,
            scales: {
                y: { beginAtZero: true }
            }
        }
    });
}

function submitAdminAiQuestion() {
    const input = document.getElementById('adminAiQuestionInput');
    const resultBox = document.getElementById('adminAiResultBox');
    const textSpan = document.getElementById('adminAiAnswerText');
    const providerSpan = document.getElementById('adminAiProviderText');

    const question = input ? input.value.trim() : '';
    if (!question) return;

    if (resultBox) resultBox.style.display = 'block';
    if (textSpan) textSpan.innerHTML = '<i class="fa-solid fa-spinner fa-spin me-2"></i> Analyzing catalog telemetry and customer reviews...';

    fetch('/api/admin/ask-ai', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ question: question })
    })
    .then(res => res.json())
    .then(data => {
        if (textSpan) textSpan.textContent = data.answer;
        if (providerSpan) providerSpan.textContent = data.provider;
    })
    .catch(() => {
        if (textSpan) textSpan.textContent = 'Error querying the AI analytics engine. Please try again.';
    });
}
