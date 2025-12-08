package controller;

import model.Usuario;
import repository.UsuarioRepository;

import java.util.List;
import java.util.regex.Pattern;

/**
 * Controller para gerenciar operações relacionadas a Usuários.
 * Implementa regras de negócio e coordena interações entre View e Repository.
 */
public class UsuarioController {

    private final UsuarioRepository usuarioRepository;
    
    // Padrão para validação de e-mail
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    );
    
    // Padrão para validação de celular (formato: (XX) XXXXX-XXXX)
    private static final Pattern CELULAR_PATTERN = Pattern.compile(
        "^\\(\\d{2}\\)\\s?\\d{5}-\\d{4}$"
    );

    public UsuarioController() {
        this.usuarioRepository = new UsuarioRepository();
    }

    /**
     * Cadastra um novo usuário no sistema.
     *
     * @param nome Nome do usuário
     * @param sexo Sexo do usuário
     * @param celular Celular do usuário
     * @param email E-mail do usuário
     * @return Usuario cadastrado
     * @throws IllegalArgumentException se os dados forem inválidos
     */
    public Usuario cadastrarUsuario(String nome, String sexo, String celular, String email) {
        
        // Validações
        validarDadosUsuario(nome, sexo, celular, email);
        
        // Verifica se já existe usuário com o mesmo e-mail
        Usuario usuarioExistente = usuarioRepository.buscarPorEmail(email);
        if (usuarioExistente != null) {
            throw new IllegalArgumentException("Já existe um usuário cadastrado com o e-mail: " + email);
        }
        
        // Cria e salva o usuário
        Usuario usuario = new Usuario(nome, sexo, celular, email);
        return usuarioRepository.salvar(usuario);
    }

    /**
     * Atualiza os dados de um usuário existente.
     *
     * @param id ID do usuário a ser atualizado
     * @param nome Novo nome
     * @param sexo Novo sexo
     * @param celular Novo celular
     * @param email Novo e-mail
     * @return Usuario atualizado
     * @throws IllegalArgumentException se os dados forem inválidos
     */
    public Usuario atualizarUsuario(Long id, String nome, String sexo, String celular, String email) {
        
        // Validações
        validarDadosUsuario(nome, sexo, celular, email);
        
        // Busca o usuário existente
        Usuario usuario = usuarioRepository.buscarPorId(id);
        if (usuario == null) {
            throw new IllegalArgumentException("Usuário não encontrado com ID: " + id);
        }
        
        // Verifica se o e-mail foi alterado e se já existe outro usuário com o novo e-mail
        if (!usuario.getEmail().equals(email)) {
            Usuario usuarioComMesmoEmail = usuarioRepository.buscarPorEmail(email);
            if (usuarioComMesmoEmail != null && !usuarioComMesmoEmail.getId().equals(id)) {
                throw new IllegalArgumentException("Já existe outro usuário cadastrado com o e-mail: " + email);
            }
        }
        
        // Atualiza os dados
        usuario.setNome(nome);
        usuario.setSexo(sexo);
        usuario.setCelular(celular);
        usuario.setEmail(email);
        
        return usuarioRepository.atualizar(usuario);
    }

    /**
     * Remove um usuário do sistema.
     *
     * @param id ID do usuário a ser removido
     * @throws IllegalArgumentException se o usuário não existir
     */
    public void removerUsuario(Long id) {
        Usuario usuario = usuarioRepository.buscarPorId(id);
        if (usuario == null) {
            throw new IllegalArgumentException("Usuário não encontrado com ID: " + id);
        }
        usuarioRepository.remover(id);
    }

    /**
     * Busca um usuário pelo ID.
     *
     * @param id ID do usuário
     * @return Usuario encontrado ou null
     */
    public Usuario buscarPorId(Long id) {
        return usuarioRepository.buscarPorId(id);
    }

    /**
     * Busca um usuário pelo e-mail.
     *
     * @param email E-mail do usuário
     * @return Usuario encontrado ou null
     */
    public Usuario buscarPorEmail(String email) {
        return usuarioRepository.buscarPorEmail(email);
    }

    /**
     * Lista todos os usuários cadastrados.
     *
     * @return Lista de usuários
     */
    public List<Usuario> listarTodos() {
        return usuarioRepository.listarTodos();
    }

    /**
     * Busca usuários por nome.
     *
     * @param nome Nome ou parte do nome
     * @return Lista de usuários encontrados
     */
    public List<Usuario> buscarPorNome(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            return listarTodos();
        }
        return usuarioRepository.buscarPorNome(nome);
    }

    /**
     * Busca usuários por celular.
     *
     * @param celular Número de celular
     * @return Lista de usuários encontrados
     */
    public List<Usuario> buscarPorCelular(String celular) {
        if (celular == null || celular.trim().isEmpty()) {
            return listarTodos();
        }
        return usuarioRepository.buscarPorCelular(celular);
    }

    /**
     * Valida os dados de um usuário.
     *
     * @throws IllegalArgumentException se algum dado for inválido
     */
    private void validarDadosUsuario(String nome, String sexo, String celular, String email) {
        
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("O nome do usuário é obrigatório");
        }
        
        if (nome.trim().length() < 3) {
            throw new IllegalArgumentException("O nome deve ter pelo menos 3 caracteres");
        }
        
        if (sexo == null || sexo.trim().isEmpty()) {
            throw new IllegalArgumentException("O sexo do usuário é obrigatório");
        }
        
        if (!sexo.equalsIgnoreCase("Masculino") && 
            !sexo.equalsIgnoreCase("Feminino") && 
            !sexo.equalsIgnoreCase("Outro")) {
            throw new IllegalArgumentException("O sexo deve ser: Masculino, Feminino ou Outro");
        }
        
        if (celular == null || celular.trim().isEmpty()) {
            throw new IllegalArgumentException("O celular do usuário é obrigatório");
        }
        
        if (!validarFormatoCelular(celular)) {
            throw new IllegalArgumentException("O celular deve estar no formato: (XX) XXXXX-XXXX");
        }
        
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("O e-mail do usuário é obrigatório");
        }
        
        if (!validarFormatoEmail(email)) {
            throw new IllegalArgumentException("O e-mail informado não é válido");
        }
    }

    /**
     * Valida o formato do e-mail.
     *
     * @param email E-mail a validar
     * @return true se o formato for válido
     */
    private boolean validarFormatoEmail(String email) {
        if (email == null) {
            return false;
        }
        return EMAIL_PATTERN.matcher(email.trim()).matches();
    }

    /**
     * Valida o formato do celular.
     *
     * @param celular Celular a validar
     * @return true se o formato for válido
     */
    private boolean validarFormatoCelular(String celular) {
        if (celular == null) {
            return false;
        }
        return CELULAR_PATTERN.matcher(celular.trim()).matches();
    }
}
