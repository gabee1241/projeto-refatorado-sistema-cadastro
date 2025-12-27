// ==================== DASHBOARD E NAVEGAÇÃO ====================

// Mostrar seção específica do dashboard
function showSection(section) {
    debug('Mostrando seção', section);
    
    // Esconder todas as seções
    document.querySelectorAll('.form-section').forEach(s => {
        s.style.display = 'none';
    });
    
    // Limpar alertas ao trocar de seção
    document.getElementById('dashboardAlert').innerHTML = '';
    
    // Mostrar a seção selecionada e carregar dados correspondentes
    switch(section) {
        case 'home':
            document.getElementById('homeSection').style.display = 'block';
            loadStats();
            break;
            
        case 'alunos':
            document.getElementById('alunosSection').style.display = 'block';
            loadAlunos();
            // Esconder formulário ao entrar na seção
            hideFormAluno();
            break;
            
        case 'professores':
            document.getElementById('professoresSection').style.display = 'block';
            loadProfessores();
            // Esconder formulário ao entrar na seção
            hideFormProfessor();
            break;
            
        default:
            debug('Seção desconhecida', section);
            document.getElementById('homeSection').style.display = 'block';
            loadStats();
    }
}

// Carregar estatísticas do dashboard
async function loadStats() {
    if (!checkAuth()) return;
    
    const user = getCurrentUser();
    debug('Carregando estatísticas para perfil', user.perfil);
    
    try {
        // Carregar alunos
        const alunosResponse = await apiRequest(`/alunos?perfil=${user.perfil}`);
        let totalAlunos = 0;
        
        if (alunosResponse.ok) {
            const alunos = await alunosResponse.json();
            totalAlunos = alunos.length;
        }
        
        // Carregar professores
        const professoresResponse = await apiRequest(`/professores?perfil=${user.perfil}`);
        let totalProfessores = 0;
        
        if (professoresResponse.ok) {
            const professores = await professoresResponse.json();
            totalProfessores = professores.length;
        }
        
        // Atualizar cards de estatísticas com animação
        animateValue('totalAlunos', 0, totalAlunos, 1000);
        animateValue('totalProfessores', 0, totalProfessores, 1000);
        
        debug('Estatísticas carregadas', { totalAlunos, totalProfessores });
        
    } catch (error) {
        console.error('Erro ao carregar estatísticas:', error);
        document.getElementById('totalAlunos').textContent = '?';
        document.getElementById('totalProfessores').textContent = '?';
    }
}

// Função para animar números (efeito contador)
function animateValue(elementId, start, end, duration) {
    const element = document.getElementById(elementId);
    if (!element) return;
    
    const range = end - start;
    const increment = range / (duration / 16); // 60fps
    let current = start;
    
    const timer = setInterval(() => {
        current += increment;
        if ((increment > 0 && current >= end) || (increment < 0 && current <= end)) {
            current = end;
            clearInterval(timer);
        }
        element.textContent = Math.floor(current);
    }, 16);
}

// Atualizar estatísticas periodicamente (a cada 30 segundos)
let statsInterval = null;

function startStatsAutoUpdate() {
    // Limpar intervalo anterior se existir
    if (statsInterval) {
        clearInterval(statsInterval);
    }
    
    // Atualizar a cada 30 segundos
    statsInterval = setInterval(() => {
        const currentSection = getCurrentSection();
        if (currentSection === 'home') {
            loadStats();
            debug('Estatísticas atualizadas automaticamente');
        }
    }, 30000);
}

function stopStatsAutoUpdate() {
    if (statsInterval) {
        clearInterval(statsInterval);
        statsInterval = null;
    }
}

// Obter seção atual
function getCurrentSection() {
    const sections = ['homeSection', 'alunosSection', 'professoresSection'];
    
    for (const sectionId of sections) {
        const section = document.getElementById(sectionId);
        if (section && section.style.display !== 'none') {
            return sectionId.replace('Section', '');
        }
    }
    
    return 'home';
}

// Função para atualizar todas as listas
function refreshAllData() {
    const currentSection = getCurrentSection();
    
    switch(currentSection) {
        case 'home':
            loadStats();
            break;
        case 'alunos':
            loadAlunos();
            break;
        case 'professores':
            loadProfessores();
            break;
    }
    
    showAlert('dashboardAlert', 'Dados atualizados!', 'info');
}

// Adicionar atalhos de teclado
document.addEventListener('keydown', function(event) {
    // Ctrl + R para atualizar dados
    if (event.ctrlKey && event.key === 'r') {
        event.preventDefault();
        refreshAllData();
    }
    
    // Esc para fechar formulários
    if (event.key === 'Escape') {
        hideFormAluno();
        hideFormProfessor();
    }
});

// Iniciar auto-update quando o dashboard for mostrado
window.addEventListener('load', function() {
    if (getCurrentUser()) {
        startStatsAutoUpdate();
    }
});

// Parar auto-update quando o usuário fizer logout
const originalLogout = window.logout;
window.logout = function() {
    stopStatsAutoUpdate();
    originalLogout();
};