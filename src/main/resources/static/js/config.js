// ==================== CONFIGURAÇÕES GLOBAIS ====================

// URL da API
const API_URL = '/api';

// Variável global do usuário atual
let currentUser = null;

// Função para obter o usuário atual
function getCurrentUser() {
    return currentUser;
}

// Função para definir o usuário atual
function setCurrentUser(user) {
    currentUser = user;
    if (user) {
        sessionStorage.setItem('user', JSON.stringify(user));
    } else {
        sessionStorage.removeItem('user');
    }
}

// Função para carregar usuário do sessionStorage
function loadUserFromStorage() {
    const savedUser = sessionStorage.getItem('user');
    if (savedUser) {
        currentUser = JSON.parse(savedUser);
        return true;
    }
    return false;
}

// Configurações de permissões
const PERMISSIONS = {
    ADMIN: {
        consultar: true,
        cadastrar: true,
        editar: true,
        excluir: true
    },
    OPERADOR: {
        consultar: true,
        cadastrar: true,
        editar: true,
        excluir: false
    },
    USUARIO: {
        consultar: true,
        cadastrar: false,
        editar: false,
        excluir: false
    }
};

// Função para verificar permissão
function hasPermission(action) {
    if (!currentUser) return false;
    return PERMISSIONS[currentUser.perfil][action] === true;
}

// Função para exibir alertas
function showAlert(elementId, message, type) {
    const alertDiv = document.getElementById(elementId);
    alertDiv.innerHTML = `
        <div class="alert alert-${type} alert-dismissible fade show alert-custom" role="alert">
            ${message}
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
    `;
    
    // Auto-hide após 5 segundos
    setTimeout(() => {
        alertDiv.innerHTML = '';
    }, 5000);
}

// Função para fazer requisições à API
async function apiRequest(endpoint, options = {}) {
    const defaultOptions = {
        headers: {
            'Content-Type': 'application/json'
        }
    };
    
    const finalOptions = {
        ...defaultOptions,
        ...options,
        headers: {
            ...defaultOptions.headers,
            ...options.headers
        }
    };
    
    try {
        const response = await fetch(`${API_URL}${endpoint}`, finalOptions);
        return response;
    } catch (error) {
        console.error('Erro na requisição:', error);
        throw error;
    }
}

// Função para formatar data
function formatDate(dateString) {
    const date = new Date(dateString);
    return date.toLocaleDateString('pt-BR');
}

// Função para validar RG
function validarRG(rg) {
    // Remove caracteres especiais
    const rgLimpo = rg.replace(/[^\d]/g, '');
    return rgLimpo.length >= 7 && rgLimpo.length <= 12;
}

// Função para formatar RG (adicionar pontos e hífen)
function formatarRG(rg) {
    // Remove caracteres não numéricos
    let rgLimpo = rg.replace(/[^\d]/g, '');
    
    // Formata para XX.XXX.XXX-X
    if (rgLimpo.length <= 2) return rgLimpo;
    if (rgLimpo.length <= 5) return rgLimpo.slice(0, 2) + '.' + rgLimpo.slice(2);
    if (rgLimpo.length <= 8) return rgLimpo.slice(0, 2) + '.' + rgLimpo.slice(2, 5) + '.' + rgLimpo.slice(5);
    return rgLimpo.slice(0, 2) + '.' + rgLimpo.slice(2, 5) + '.' + rgLimpo.slice(5, 8) + '-' + rgLimpo.slice(8, 9);
}

// Função para capitalizar texto
function capitalize(text) {
    return text.charAt(0).toUpperCase() + text.slice(1).toLowerCase();
}

// Função de debug (ativar apenas em desenvolvimento)
const DEBUG = true;

function debug(message, data = null) {
    if (DEBUG) {
        console.log(`[DEBUG] ${message}`, data || '');
    }
}