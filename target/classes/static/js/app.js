// ==================== INICIALIZAÇÃO DA APLICAÇÃO ====================

// Configurações iniciais quando o DOM estiver pronto
document.addEventListener('DOMContentLoaded', function() {
    debug('Aplicação inicializada');
    initializeApp();
});

// Função principal de inicialização
function initializeApp() {
    debug('Iniciando aplicação...');
    
    // Verificar se há usuário logado no sessionStorage
    if (loadUserFromStorage()) {
        debug('Sessão recuperada do storage');
        showDashboard();
    } else {
        debug('Nenhuma sessão encontrada, exibindo login');
        showLoginScreen();
    }
    
    // Adicionar listeners globais
    setupGlobalListeners();
    
    // Verificar conexão com o servidor
    checkServerConnection();
}

// Mostrar tela de login
function showLoginScreen() {
    document.getElementById('loginScreen').style.display = 'flex';
    document.getElementById('dashboardScreen').style.display = 'none';
    
    // Focar no campo de usuário
    const usernameInput = document.getElementById('username');
    if (usernameInput) {
        usernameInput.focus();
    }
}

// Configurar listeners globais
function setupGlobalListeners() {
    // Prevenir submissão de formulários duplicada
    const forms = document.querySelectorAll('form');
    forms.forEach(form => {
        form.addEventListener('submit', function(e) {
            const submitBtn = form.querySelector('button[type="submit"]');
            if (submitBtn && submitBtn.disabled) {
                e.preventDefault();
                return false;
            }
        });
    });
    
    // Listener para fechar alertas automaticamente
    document.addEventListener('click', function(e) {
        if (e.target.classList.contains('btn-close')) {
            const alert = e.target.closest('.alert');
            if (alert) {
                alert.remove();
            }
        }
    });
    
    // Prevenir saída acidental da página quando houver formulários preenchidos
    window.addEventListener('beforeunload', function(e) {
        const alunoForm = document.getElementById('formAluno');
        const professorForm = document.getElementById('formProfessor');
        
        const alunoFormVisible = alunoForm && alunoForm.style.display !== 'none';
        const professorFormVisible = professorForm && professorForm.style.display !== 'none';
        
        if (alunoFormVisible || professorFormVisible) {
            e.preventDefault();
            e.returnValue = '';
        }
    });
    
    debug('Listeners globais configurados');
}

// Verificar conexão com o servidor
async function checkServerConnection() {
    try {
        const response = await fetch(`${API_URL}/auth/login`, {
            method: 'OPTIONS'
        });
        
        debug('Servidor acessível', response.ok);
        
        if (!response.ok) {
            console.warn('Servidor pode estar com problemas');
        }
    } catch (error) {
        console.error('Erro ao conectar com o servidor:', error);
        debug('Servidor pode estar offline');
    }
}

// Função para tratamento de erros global
window.addEventListener('error', function(event) {
    console.error('Erro global capturado:', event.error);
    
    // Não mostrar alerta para erros de script
    if (event.error && event.error.name !== 'NetworkError') {
        // Log para debug
        debug('Erro detectado', {
            message: event.message,
            filename: event.filename,
            lineno: event.lineno,
            colno: event.colno
        });
    }
});

// Função para tratamento de promessas rejeitadas
window.addEventListener('unhandledrejection', function(event) {
    console.error('Promise rejeitada não tratada:', event.reason);
    debug('Promise rejection', event.reason);
});

// Verificar sessão periodicamente (a cada 5 minutos)
setInterval(function() {
    if (getCurrentUser()) {
        debug('Sessão ativa, verificando validade...');
        // Aqui você pode adicionar uma chamada para verificar se o token ainda é válido
    }
}, 300000); // 5 minutos

// Função de utilidade para limpar o console em produção
function clearConsoleInProduction() {
    if (!DEBUG) {
        console.log = function() {};
        console.debug = function() {};
        console.info = function() {};
    }
}

// Informações sobre a aplicação
const APP_INFO = {
    name: 'Sistema de Cadastro',
    version: '1.0.0',
    author: 'Seu Nome',
    description: 'Sistema de gerenciamento de alunos e professores'
};

// Mostrar informações da aplicação no console
console.log('%c' + APP_INFO.name, 'font-size: 20px; font-weight: bold; color: #667eea;');
console.log('%cVersão: ' + APP_INFO.version, 'color: #666;');
console.log('%cDesenvolvido por: ' + APP_INFO.author, 'color: #666;');
console.log('%c' + APP_INFO.description, 'color: #999; font-style: italic;');
console.log('%c━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━', 'color: #667eea;');

// Exportar funções úteis para debug no console
if (DEBUG) {
    window.appDebug = {
        getCurrentUser,
        getCurrentSection,
        loadStats,
        loadAlunos,
        loadProfessores,
        refreshAllData,
        checkServerConnection,
        appInfo: APP_INFO
    };
    
    console.log('%cModo DEBUG ativado!', 'color: orange; font-weight: bold;');
    console.log('Digite "appDebug" no console para acessar funções de debug');
}

// Limpar console em produção
clearConsoleInProduction();