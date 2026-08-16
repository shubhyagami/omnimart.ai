// OmniMart Main JavaScript

document.addEventListener('DOMContentLoaded', () => {
    // Quick Compare Bar Management
    let compareIds = JSON.parse(localStorage.getItem('omni_compare_ids') || '[]');
    updateCompareBarUI(compareIds);

    document.querySelectorAll('.btn-add-compare').forEach(btn => {
        btn.addEventListener('click', (e) => {
            e.preventDefault();
            const id = btn.getAttribute('data-product-id');
            if (id) {
                toggleProductComparison(id);
            }
        });
    });
});

function toggleProductComparison(id) {
    let compareIds = JSON.parse(localStorage.getItem('omni_compare_ids') || '[]');
    const index = compareIds.indexOf(id);
    if (index > -1) {
        compareIds.splice(index, 1);
    } else {
        if (compareIds.length >= 4) {
            alert('You can compare a maximum of 4 products at a time.');
            return;
        }
        compareIds.push(id);
    }
    localStorage.setItem('omni_compare_ids', JSON.stringify(compareIds));
    updateCompareBarUI(compareIds);
}

function updateCompareBarUI(compareIds) {
    const bar = document.getElementById('quickCompareBar');
    const countSpan = document.getElementById('compareCount');
    const link = document.getElementById('compareNowLink');

    if (!bar) return;

    if (compareIds.length > 0) {
        bar.style.display = 'block';
        if (countSpan) countSpan.textContent = compareIds.length;
        if (link) link.href = '/compare?ids=' + compareIds.join(',');
    } else {
        bar.style.display = 'none';
    }
}

function clearCompareList() {
    localStorage.removeItem('omni_compare_ids');
    updateCompareBarUI([]);
}
