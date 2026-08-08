const API_BASE = '/api/computers';

const form = document.getElementById('computer-form');
const formTitle = document.getElementById('form-title');
const submitBtn = document.getElementById('submit-btn');
const cancelBtn = document.getElementById('cancel-btn');
const formError = document.getElementById('form-error');
const tbody = document.getElementById('computers-body');
const refreshBtn = document.getElementById('refresh-btn');

const fields = ['proce', 'ram', 'hardDrive', 'price', 'macAddress'];

async function loadComputers() {
    renderMessage('⏳', 'Chargement…');

    try {
        const res = await fetch(`${API_BASE}?size=100&sort=idPc,asc`);
        if (!res.ok) throw new Error(`HTTP ${res.status}`);
        const page = await res.json();
        renderTable(page.content ?? []);
    } catch (err) {
        renderMessage('⚠️', `Impossible de charger les données (${err.message}). L'API tourne-t-elle bien ?`);
    }
}

function renderMessage(icon, text) {
    tbody.innerHTML = '';
    const row = document.createElement('tr');
    const cell = document.createElement('td');
    cell.colSpan = 7;
    cell.className = 'empty-state';

    const iconSpan = document.createElement('span');
    iconSpan.className = 'empty-icon';
    iconSpan.textContent = icon;
    cell.append(iconSpan, text);

    row.appendChild(cell);
    tbody.appendChild(row);
}

function renderTable(computers) {
    if (computers.length === 0) {
        renderMessage('🗂️', 'Aucun ordinateur pour le moment — ajoutes-en un ci-dessus.');
        return;
    }

    tbody.innerHTML = '';
    for (const computer of computers) {
        tbody.appendChild(buildRow(computer));
    }
}

function buildRow(computer) {
    const row = document.createElement('tr');

    const cells = [
        { label: 'Id', value: `#${computer.idPc}`, className: 'mono' },
        { label: 'Processeur', value: computer.proce },
        { label: 'RAM', value: `${computer.ram} Go` },
        { label: 'Disque dur', value: `${computer.hardDrive} Go` },
        { label: 'Prix', value: `${computer.price.toLocaleString('fr-FR')} €`, className: 'price-tag' },
        { label: 'Adresse MAC', value: computer.macAddress, className: 'mono' },
    ];
    for (const { label, value, className } of cells) {
        const td = document.createElement('td');
        td.textContent = value;
        td.dataset.label = label;
        if (className) td.className = className;
        row.appendChild(td);
    }

    const actionsCell = document.createElement('td');
    actionsCell.className = 'row-actions';
    actionsCell.dataset.label = 'Actions';

    const editBtn = document.createElement('button');
    editBtn.type = 'button';
    editBtn.className = 'secondary';
    editBtn.textContent = 'Modifier';
    editBtn.addEventListener('click', () => startEdit(computer));

    const deleteBtn = document.createElement('button');
    deleteBtn.type = 'button';
    deleteBtn.className = 'danger';
    deleteBtn.textContent = 'Supprimer';
    deleteBtn.addEventListener('click', () => deleteComputer(computer.idPc));

    actionsCell.append(editBtn, deleteBtn);
    row.appendChild(actionsCell);

    return row;
}

function startEdit(computer) {
    document.getElementById('idPc').value = computer.idPc;
    for (const field of fields) {
        document.getElementById(field).value = computer[field];
    }
    formTitle.textContent = `Modifier l'ordinateur #${computer.idPc}`;
    submitBtn.textContent = 'Enregistrer';
    cancelBtn.hidden = false;
    hideError();
    form.scrollIntoView({ behavior: 'smooth', block: 'start' });
}

function resetForm() {
    form.reset();
    document.getElementById('idPc').value = '';
    formTitle.textContent = 'Ajouter un ordinateur';
    submitBtn.textContent = 'Ajouter';
    cancelBtn.hidden = true;
    hideError();
}

function showError(message) {
    formError.textContent = message;
    formError.hidden = false;
}

function hideError() {
    formError.hidden = true;
    formError.textContent = '';
}

async function submitForm(event) {
    event.preventDefault();
    hideError();

    const idPc = document.getElementById('idPc').value;
    const payload = {
        proce: document.getElementById('proce').value.trim(),
        ram: Number(document.getElementById('ram').value),
        hardDrive: Number(document.getElementById('hardDrive').value),
        price: Number(document.getElementById('price').value),
        macAddress: document.getElementById('macAddress').value.trim(),
    };

    const isEdit = idPc !== '';
    const url = isEdit ? `${API_BASE}/${idPc}` : API_BASE;
    const method = isEdit ? 'PUT' : 'POST';

    submitBtn.disabled = true;
    try {
        const res = await fetch(url, {
            method,
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(payload),
        });

        if (!res.ok) {
            const body = await res.json().catch(() => null);
            showError(formatApiError(body, res.status));
            return;
        }

        resetForm();
        await loadComputers();
    } catch (err) {
        showError(`Erreur réseau : ${err.message}`);
    } finally {
        submitBtn.disabled = false;
    }
}

function formatApiError(body, status) {
    if (!body) return `Erreur ${status}`;
    if (body.fieldErrors) {
        const details = Object.entries(body.fieldErrors)
            .map(([field, msg]) => `${field} : ${msg}`)
            .join(' — ');
        return `${body.message} (${details})`;
    }
    return body.message ?? `Erreur ${status}`;
}

async function deleteComputer(id) {
    if (!confirm(`Supprimer l'ordinateur #${id} ?`)) return;

    try {
        const res = await fetch(`${API_BASE}/${id}`, { method: 'DELETE' });
        if (!res.ok && res.status !== 404) {
            const body = await res.json().catch(() => null);
            alert(formatApiError(body, res.status));
            return;
        }
        await loadComputers();
    } catch (err) {
        alert(`Erreur réseau : ${err.message}`);
    }
}

form.addEventListener('submit', submitForm);
cancelBtn.addEventListener('click', resetForm);
refreshBtn.addEventListener('click', loadComputers);

loadComputers();
