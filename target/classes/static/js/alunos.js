// ==================== GERENCIAMENTO DE ALUNOS ====================

// Inicializar formulário de alunos
document.addEventListener('DOMContentLoaded', function() {
    const alunoForm = document.getElementById('alunoForm');
    if (alunoForm) {
        alunoForm.addEventListener('submit', handleAlunoSubmit);
    }
    
    // Adicionar formatação automática ao campo RG
    const alunoRgInput = document.getElementById('alunoRg');
    if (alunoRgInput) {
        alunoRgInput.addEventListener('input', function(e) {
            e.target.value = formatarRG(e.target.value);
        });
    }
});

// Carregar lista de alunos
async function loadAlunos() {
    if (!checkAuth()) return;
    
    const user = getCurrentUser();
    debug('Carregando alunos para perfil', user.perfil);
    
    try {
        const response = await apiRequest(`/alunos?perfil=${user.perfil}`);
        
        if (response.ok) {
            const alunos = await response.json();
            debug('Alunos carregados', alunos);
            renderAlunosTable(alunos);
        } else if (response.status === 403) {
            showAlert('dashboardAlert', 'Você não tem permissão para consultar alunos!', 'danger');
        } else {
            showAlert('dashboardAlert', 'Erro ao carregar alunos!', 'danger');
        }
    } catch (error) {
        console.error('Erro ao carregar alunos:', error);
        showAlert('dashboardAlert', 'Erro ao conectar com o servidor!', 'danger');
    }
}

// Renderizar tabela de alunos
function renderAlunosTable(alunos) {
    const tbody = document.getElementById('alunosTable');
    
    if (!alunos || alunos.length === 0) {
        tbody.innerHTML = `
            <tr>
                <td colspan="5" class="text-center text-muted">
                    Nenhum aluno cadastrado
                </td>
            </tr>
        `;
        return;
    }
    
    const canEdit = hasPermission('editar');
    const canDelete = hasPermission('excluir');
    
    tbody.innerHTML = alunos.map(aluno => `
        <tr>
            <td>${aluno.id}</td>
            <td>${aluno.nome}</td>
            <td>${aluno.rg}</td>
            <td>${aluno.curso}</td>
            <td>
                ${canEdit ? `
                    <button class="btn btn-warning btn-sm btn-action" onclick="editAluno(${aluno.id})">
                        ✏️ Editar
                    </button>
                ` : ''}
                ${canDelete ? `
                    <button class="btn btn-danger btn-sm btn-action" onclick="deleteAluno(${aluno.id})">
                        🗑️ Excluir
                    </button>
                ` : ''}
                ${!canEdit && !canDelete ? '<span class="text-muted">Sem ações disponíveis</span>' : ''}
            </td>
        </tr>
    `).join('');
}



// Mostrar formulário SEM resetar (usado para edição)
function showFormAluno() {
    document.getElementById('formAluno').style.display = 'block';
    document.getElementById('alunoNome').focus();
    debug('Formulário de aluno exibido'); // ← Mude esta mensagem!
}

// Mostrar formulário COM reset (usado para novo cadastro)
function showFormNovoAluno() {
    if (!hasPermission('cadastrar')) {
        showAlert('dashboardAlert', 'Você não tem permissão para cadastrar alunos!', 'warning');
        return;
    }
    
    document.getElementById('formAluno').style.display = 'block';
    document.getElementById('alunoForm').reset(); // ← Só reseta aqui!
    document.getElementById('alunoId').value = '';
    document.getElementById('alunoNome').focus();
    
    debug('Formulário de novo aluno exibido'); // ← Esta mensagem só deve aparecer no NOVO
}

// Esconder formulário de aluno
function hideFormAluno() {
    document.getElementById('formAluno').style.display = 'none';
    document.getElementById('alunoForm').reset();
    debug('Formulário de aluno escondido');
}

// Submeter formulário de aluno (criar ou editar)
async function handleAlunoSubmit(event) {
    event.preventDefault();
    
    const user = getCurrentUser();
    const id = document.getElementById('alunoId').value;
    
    // ========== DEBUG ==========
    console.log('===== DEBUG EDIÇÃO =====');
    console.log('ID capturado:', id);
    console.log('Tipo do ID:', typeof id);
    console.log('ID vazio?', id === '');
    console.log('ID tem valor?', Boolean(id));
    console.log('========================');
    // ===========================
    
    
    const aluno = {
        nome: document.getElementById('alunoNome').value.trim(),
        rg: document.getElementById('alunoRg').value.trim(),
        curso: document.getElementById('alunoCurso').value.trim()
    };
    
    // Validações
    if (!aluno.nome || !aluno.rg || !aluno.curso) {
        showAlert('dashboardAlert', 'Por favor, preencha todos os campos!', 'warning');
        return;
    }
    
    if (!validarRG(aluno.rg)) {
        showAlert('dashboardAlert', 'RG inválido! Digite um RG válido.', 'warning');
        return;
    }
    
    const isEdit = id !== '';
    const action = isEdit ? 'editar' : 'cadastrar';
    
    // ========== DEBUG ==========
    console.log('É edição?', isEdit);
    console.log('Ação:', action);
    console.log('========================');
    // ===========================
    
    
    if (!hasPermission(action)) {
        showAlert('dashboardAlert', `Você não tem permissão para ${action} alunos!`, 'danger');
        return;
    }
    
    debug(isEdit ? 'Editando aluno' : 'Cadastrando novo aluno', aluno);
    
    try {
        const url = isEdit 
            ? `/alunos/${id}?perfil=${user.perfil}` 
            : `/alunos?perfil=${user.perfil}`;
        
        const method = isEdit ? 'PUT' : 'POST';
        
        // ========== DEBUG ==========
        console.log('URL:', url);
        console.log('Method:', method);
        console.log('Body:', aluno);
        console.log('========================');
        // ===========================
        
        
        const response = await apiRequest(url, {
            method: method,
            body: JSON.stringify(aluno)
        });
        
        if (response.ok) {
            const savedAluno = await response.json();
            showAlert('dashboardAlert', 
                `Aluno ${isEdit ? 'atualizado' : 'cadastrado'} com sucesso!`, 
                'success');
            hideFormAluno();
            loadAlunos();
            debug('Aluno salvo', savedAluno);
        } else {
            const errorMessage = await response.text();
            showAlert('dashboardAlert', errorMessage || 'Erro ao salvar aluno!', 'danger');
        }
    } catch (error) {
        console.error('Erro ao salvar aluno:', error);
        showAlert('dashboardAlert', 'Erro ao conectar com o servidor!', 'danger');
    }
}

// Editar aluno
async function editAluno(id) {
    if (!hasPermission('editar')) {
        showAlert('dashboardAlert', 'Você não tem permissão para editar alunos!', 'warning');
        return;
    }
    
    // ========== DEBUG ==========
    console.log('===== INICIANDO EDIÇÃO =====');
    console.log('ID recebido na função:', id);
    console.log('============================');
    // ===========================
    
    debug('Carregando aluno para edição', id);
    
    const user = getCurrentUser();
    
    try {
        const response = await apiRequest(`/alunos?perfil=${user.perfil}`);
        
        if (response.ok) {
            const alunos = await response.json();
            const aluno = alunos.find(a => a.id === id);
            
            // ========== DEBUG ==========
            console.log('Aluno encontrado:', aluno);
            console.log('============================');
            // ===========================
            
            if (aluno) {
                
                // ========== DEBUG ==========
                console.log('Preenchendo campos...');
                console.log('ID antes:', document.getElementById('alunoId').value);
                // ===========================
                
                document.getElementById('alunoId').value = aluno.id;
                document.getElementById('alunoNome').value = aluno.nome;
                document.getElementById('alunoRg').value = aluno.rg;
                document.getElementById('alunoCurso').value = aluno.curso;
                
                // ========== DEBUG ==========
                console.log('ID depois:', document.getElementById('alunoId').value);
                console.log('============================');
                // ===========================
                
                showFormAluno();
                
                // ========== DEBUG - CRÍTICO ==========
                console.log('ID após showFormAluno:', document.getElementById('alunoId').value);
                console.log('=====================================');
                // ===========================
                
                
                // Scroll para o formulário
                document.getElementById('formAluno').scrollIntoView({ 
                    behavior: 'smooth', 
                    block: 'start' 
                });
                
                debug('Aluno carregado para edição', aluno);
            } else {
                showAlert('dashboardAlert', 'Aluno não encontrado!', 'danger');
            }
        } else {
            showAlert('dashboardAlert', 'Erro ao carregar dados do aluno!', 'danger');
        }
    } catch (error) {
        console.error('Erro ao carregar aluno:', error);
        showAlert('dashboardAlert', 'Erro ao conectar com o servidor!', 'danger');
    }
}

// Excluir aluno
async function deleteAluno(id) {
    if (!hasPermission('excluir')) {
        showAlert('dashboardAlert', 'Você não tem permissão para excluir alunos!', 'warning');
        return;
    }
    
    if (!confirm('Deseja realmente excluir este aluno? Esta ação não pode ser desfeita!')) {
        return;
    }
    
    debug('Excluindo aluno', id);
    
    const user = getCurrentUser();
    
    try {
        const response = await apiRequest(`/alunos/${id}?perfil=${user.perfil}`, {
            method: 'DELETE'
        });
        
        if (response.ok) {
            showAlert('dashboardAlert', 'Aluno excluído com sucesso!', 'success');
            loadAlunos();
            debug('Aluno excluído', id);
        } else if (response.status === 403) {
            showAlert('dashboardAlert', 'Você não tem permissão para excluir alunos!', 'danger');
        } else {
            showAlert('dashboardAlert', 'Erro ao excluir aluno!', 'danger');
        }
    } catch (error) {
        console.error('Erro ao excluir aluno:', error);
        showAlert('dashboardAlert', 'Erro ao conectar com o servidor!', 'danger');
    }
}