// ==================== AUTENTICAÇÃO ====================

// Inicializar o formulário de login
document.addEventListener('DOMContentLoaded', function() {
    const loginForm = document.getElementById('loginForm');
    if (loginForm) {
        loginForm.addEventListener('submit', handleLogin);
    }
});

// Função para lidar com o login
async function handleLogin(event) {
    event.preventDefault();
    
    const username = document.getElementById('username').value.trim();
    const senha = document.getElementById('password').value;
    
    // Validações básicas
    if (!username || !senha) {
        showAlert('loginAlert', 'Por favor, preencha todos os campos!', 'warning');
        return;
    }
    
    // Desabilitar botão durante o processo
    const submitBtn = event.target.querySelector('button[type="submit"]');
    submitBtn.disabled = true;
    submitBtn.innerHTML = '<span class="spinner-border spinner-border-sm me-2"></span>Entrando...';
    
    try {
        const response = await apiRequest('/auth/login', {
            method: 'POST',
            body: JSON.stringify({ username, senha })
        });
        
        if (response.ok) {
            const userData = await response.json();
            setCurrentUser(userData);
            debug('Login bem-sucedido', userData);
            
            showAlert('loginAlert', 'Login realizado com sucesso!', 'success');
            
            // Aguardar um momento para mostrar a mensagem
            setTimeout(() => {
                showDashboard();
            }, 500);
        } else {
            const errorMessage = await response.text();
            showAlert('loginAlert', errorMessage || 'Credenciais inválidas!', 'danger');
            submitBtn.disabled = false;
            submitBtn.innerHTML = 'Entrar';
        }
    } catch (error) {
        console.error('Erro no login:', error);
        showAlert('loginAlert', 'Erro ao conectar com o servidor. Verifique se o back-end está rodando.', 'danger');
        submitBtn.disabled = false;
        submitBtn.innerHTML = 'Entrar';
    }
}

// Função para mostrar o dashboard
function showDashboard() {
    const user = getCurrentUser();
    
    if (!user) {
        debug('Nenhum usuário logado');
        return;
    }
    
    debug('Mostrando dashboard para', user);
    
    // Esconder tela de login
    document.getElementById('loginScreen').style.display = 'none';
    
    // Mostrar dashboard
    document.getElementById('dashboardScreen').style.display = 'block';
    
    // Atualizar informações do usuário na interface
    document.getElementById('userDisplay').textContent = user.username;
    document.getElementById('currentUser').textContent = user.username;
    document.getElementById('currentPerfil').textContent = user.perfil;
    
    // Atualizar permissões na interface
    updatePermissionsUI();
    
    // Mostrar seção inicial (home)
    showSection('home');
    
    // Carregar estatísticas
    loadStats();
}

// Função para atualizar a UI com base nas permissões
function updatePermissionsUI() {
    const perfil = getCurrentUser().perfil;
    
    // Definir permissões por perfil
    const permissions = {
        ADMIN: ['Consultar', 'Cadastrar', 'Editar', 'Excluir'],
        OPERADOR: ['Consultar', 'Cadastrar', 'Editar'],
        USUARIO: ['Consultar']
    };
    
    // Atualizar lista de permissões
    const permissoesList = document.getElementById('permissoesList');
    permissoesList.innerHTML = permissions[perfil]
        .map(p => `<li>✅ ${p}</li>`)
        .join('');
    
    // Controlar visibilidade dos botões de cadastro
    const canCreate = hasPermission('cadastrar');
    const btnNovoAluno = document.getElementById('btnNovoAluno');
    const btnNovoProfessor = document.getElementById('btnNovoProfessor');
    
    if (btnNovoAluno) {
        btnNovoAluno.style.display = canCreate ? 'inline-block' : 'none';
    }
    
    if (btnNovoProfessor) {
        btnNovoProfessor.style.display = canCreate ? 'inline-block' : 'none';
    }
    
    debug('Permissões atualizadas', { perfil, permissions: permissions[perfil] });
}

// Função para fazer logout
function logout() {
    debug('Fazendo logout');
    
    // Confirmar logout
    if (!confirm('Deseja realmente sair do sistema?')) {
        return;
    }
    
    // Limpar dados do usuário
    setCurrentUser(null);
    sessionStorage.clear();
    
    // Esconder dashboard
    document.getElementById('dashboardScreen').style.display = 'none';
    
    // Mostrar tela de login
    document.getElementById('loginScreen').style.display = 'flex';
    
    // Limpar formulário de login
    document.getElementById('loginForm').reset();
    
    // Limpar alertas
    document.getElementById('loginAlert').innerHTML = '';
    document.getElementById('dashboardAlert').innerHTML = '';
    
    showAlert('loginAlert', 'Logout realizado com sucesso!', 'success');
}

// Função para verificar se o usuário está logado
function checkAuth() {
    const user = getCurrentUser();
    
    if (!user) {
        debug('Usuário não autenticado, redirecionando para login');
        document.getElementById('dashboardScreen').style.display = 'none';
        document.getElementById('loginScreen').style.display = 'flex';
        return false;
    }
    
    return true;
}

// Verificar autenticação ao carregar a página
window.addEventListener('load', function() {
    if (loadUserFromStorage()) {
        debug('Usuário encontrado no storage, restaurando sessão');
        showDashboard();
    } else {
        debug('Nenhum usuário no storage, mostrando tela de login');
        document.getElementById('loginScreen').style.display = 'flex';
    }
});