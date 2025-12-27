// ==================== GERENCIAMENTO DE PROFESSORES ====================

// Inicializar formulário de professores
document.addEventListener('DOMContentLoaded', function() {
    const professorForm = document.getElementById('professorForm');
    if (professorForm) {
        professorForm.addEventListener('submit', handleProfessorSubmit);
    }
    
    // Adicionar formatação automática ao campo RG
    const professorRgInput = document.getElementById('professorRg');
    if (professorRgInput) {
        professorRgInput.addEventListener('input', function(e) {
            e.target.value = formatarRG(e.target.value);
        });
    }
});

// Carregar lista de professores
async function loadProfessores() {
    if (!checkAuth()) return;
    
    const user = getCurrentUser();
    debug('Carregando professores para perfil', user.perfil);
    
    try {
        const response = await apiRequest(`/professores?perfil=${user.perfil}`);
        
        if (response.ok) {
            const professores = await response.json();
            debug('Professores carregados', professores);
            renderProfessoresTable(professores);
        } else if (response.status === 403) {
            showAlert('dashboardAlert', 'Você não tem permissão para consultar professores!', 'danger');
        } else {
            showAlert('dashboardAlert', 'Erro ao carregar professores!', 'danger');
        }
    } catch (error) {
        console.error('Erro ao carregar professores:', error);
        showAlert('dashboardAlert', 'Erro ao conectar com o servidor!', 'danger');
    }
}

// Renderizar tabela de professores
function renderProfessoresTable(professores) {
    const tbody = document.getElementById('professoresTable');
    
    if (!professores || professores.length === 0) {
        tbody.innerHTML = `
            <tr>
                <td colspan="5" class="text-center text-muted">
                    Nenhum professor cadastrado
                </td>
            </tr>
        `;
        return;
    }
    
    const canEdit = hasPermission('editar');
    const canDelete = hasPermission('excluir');
    
    tbody.innerHTML = professores.map(professor => `
        <tr>
            <td>${professor.id}</td>
            <td>${professor.nome}</td>
            <td>${professor.rg}</td>
            <td>${professor.departamento}</td>
            <td>
                ${canEdit ? `
                    <button class="btn btn-warning btn-sm btn-action" onclick="editProfessor(${professor.id})">
                        ✏️ Editar
                    </button>
                ` : ''}
                ${canDelete ? `
                    <button class="btn btn-danger btn-sm btn-action" onclick="deleteProfessor(${professor.id})">
                        🗑️ Excluir
                    </button>
                ` : ''}
                ${!canEdit && !canDelete ? '<span class="text-muted">Sem ações disponíveis</span>' : ''}
            </td>
        </tr>
    `).join('');
}

// Mostrar formulário SEM resetar (para edição)
function showFormProfessor() {
    document.getElementById('formProfessor').style.display = 'block';
    document.getElementById('professorNome').focus();
}

// Mostrar formulário resetado (para novo cadastro)
function showFormNovoProfessor() {
    if (!hasPermission('cadastrar')) {
        showAlert('dashboardAlert', 'Você não tem permissão para cadastrar professores!', 'warning');
        return;
    }
    
    document.getElementById('formProfessor').style.display = 'block';
    document.getElementById('professorForm').reset();
    document.getElementById('professorId').value = '';
    document.getElementById('professorNome').focus();
    
    debug('Formulário de novo professor exibido');
}

// Esconder formulário de professor
function hideFormProfessor() {
    document.getElementById('formProfessor').style.display = 'none';
    document.getElementById('professorForm').reset();
    debug('Formulário de professor escondido');
}

// Submeter formulário de professor (criar ou editar)
async function handleProfessorSubmit(event) {
    event.preventDefault();
    
    const user = getCurrentUser();
    const id = document.getElementById('professorId').value;
    const professor = {
        nome: document.getElementById('professorNome').value.trim(),
        rg: document.getElementById('professorRg').value.trim(),
        departamento: document.getElementById('professorDepartamento').value.trim()
    };
    
    // Validações
    if (!professor.nome || !professor.rg || !professor.departamento) {
        showAlert('dashboardAlert', 'Por favor, preencha todos os campos!', 'warning');
        return;
    }
    
    if (!validarRG(professor.rg)) {
        showAlert('dashboardAlert', 'RG inválido! Digite um RG válido.', 'warning');
        return;
    }
    
    const isEdit = id !== '';
    const action = isEdit ? 'editar' : 'cadastrar';
    
    if (!hasPermission(action)) {
        showAlert('dashboardAlert', `Você não tem permissão para ${action} professores!`, 'danger');
        return;
    }
    
    debug(isEdit ? 'Editando professor' : 'Cadastrando novo professor', professor);
    
    try {
        const url = isEdit 
            ? `/professores/${id}?perfil=${user.perfil}` 
            : `/professores?perfil=${user.perfil}`;
        
        const method = isEdit ? 'PUT' : 'POST';
        
        const response = await apiRequest(url, {
            method: method,
            body: JSON.stringify(professor)
        });
        
        if (response.ok) {
            const savedProfessor = await response.json();
            showAlert('dashboardAlert', 
                `Professor ${isEdit ? 'atualizado' : 'cadastrado'} com sucesso!`, 
                'success');
            hideFormProfessor();
            loadProfessores();
            debug('Professor salvo', savedProfessor);
        } else {
            const errorMessage = await response.text();
            showAlert('dashboardAlert', errorMessage || 'Erro ao salvar professor!', 'danger');
        }
    } catch (error) {
        console.error('Erro ao salvar professor:', error);
        showAlert('dashboardAlert', 'Erro ao conectar com o servidor!', 'danger');
    }
}

// Editar professor
async function editProfessor(id) {
    if (!hasPermission('editar')) {
        showAlert('dashboardAlert', 'Você não tem permissão para editar professores!', 'warning');
        return;
    }
    
    debug('Carregando professor para edição', id);
    
    const user = getCurrentUser();
    
    try {
        const response = await apiRequest(`/professores?perfil=${user.perfil}`);
        
        if (response.ok) {
            const professores = await response.json();
            const professor = professores.find(p => p.id === id);
            
            if (professor) {
                document.getElementById('professorId').value = professor.id;
                document.getElementById('professorNome').value = professor.nome;
                document.getElementById('professorRg').value = professor.rg;
                document.getElementById('professorDepartamento').value = professor.departamento;
                showFormProfessor();
                
                // Scroll para o formulário
                document.getElementById('formProfessor').scrollIntoView({ 
                    behavior: 'smooth', 
                    block: 'start' 
                });
                
                debug('Professor carregado para edição', professor);
            } else {
                showAlert('dashboardAlert', 'Professor não encontrado!', 'danger');
            }
        } else {
            showAlert('dashboardAlert', 'Erro ao carregar dados do professor!', 'danger');
        }
    } catch (error) {
        console.error('Erro ao carregar professor:', error);
        showAlert('dashboardAlert', 'Erro ao conectar com o servidor!', 'danger');
    }
}

// Excluir professor
async function deleteProfessor(id) {
    if (!hasPermission('excluir')) {
        showAlert('dashboardAlert', 'Você não tem permissão para excluir professores!', 'warning');
        return;
    }
    
    if (!confirm('Deseja realmente excluir este professor? Esta ação não pode ser desfeita!')) {
        return;
    }
    
    debug('Excluindo professor', id);
    
    const user = getCurrentUser();
    
    try {
        const response = await apiRequest(`/professores/${id}?perfil=${user.perfil}`, {
            method: 'DELETE'
        });
        
        if (response.ok) {
            showAlert('dashboardAlert', 'Professor excluído com sucesso!', 'success');
            loadProfessores();
            debug('Professor excluído', id);
        } else if (response.status === 403) {
            showAlert('dashboardAlert', 'Você não tem permissão para excluir professores!', 'danger');
        } else {
            showAlert('dashboardAlert', 'Erro ao excluir professor!', 'danger');
        }
    } catch (error) {
        console.error('Erro ao excluir professor:', error);
        showAlert('dashboardAlert', 'Erro ao conectar com o servidor!', 'danger');
    }
}