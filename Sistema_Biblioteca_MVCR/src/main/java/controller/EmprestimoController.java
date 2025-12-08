package controller;

import model.Emprestimo;
import model.Livro;
import model.Usuario;
import repository.EmprestimoRepository;
import repository.LivroRepository;
import repository.UsuarioRepository;

import java.time.LocalDate;
import java.util.List;

public class EmprestimoController {

    private static final int LIMITE_EMPRESTIMOS_POR_USUARIO = 5;

    private final EmprestimoRepository emprestimoRepository;
    private final LivroRepository livroRepository;
    private final UsuarioRepository usuarioRepository;

    public EmprestimoController() {
        this.emprestimoRepository = new EmprestimoRepository();
        this.livroRepository = new LivroRepository();
        this.usuarioRepository = new UsuarioRepository();
    }

    public Emprestimo registrarEmprestimo(Long usuarioId, Long livroId, LocalDate dataEmprestimo) {

        Usuario usuario = usuarioRepository.buscarPorId(usuarioId);
        if (usuario == null) {
            throw new IllegalArgumentException("Usuário não encontrado com ID: " + usuarioId);
        }

        Livro livro = livroRepository.buscarPorId(livroId);
        if (livro == null) {
            throw new IllegalArgumentException("Livro não encontrado com ID: " + livroId);
        }

        if (dataEmprestimo == null) {
            dataEmprestimo = LocalDate.now();
        }

        if (dataEmprestimo.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("A data do empréstimo não pode ser futura");
        }

        if (usuario.isEmMulta()) {
            throw new IllegalArgumentException(
                    String.format("BLOQUEADO: O usuário está em período de multa. Faltam %d dias.",
                            usuario.getDiasRestantesMulta())
            );
        }

        if (temAtrasoPendente(usuario)) {
            throw new IllegalArgumentException("BLOQUEADO: O usuário possui livros com devolução atrasada pendente.");
        }

        if (!livro.temExemplaresDisponiveis()) {
            throw new IllegalArgumentException("Não há exemplares disponíveis do livro: " + livro.getTitulo());
        }

        long emprestimosAtivos = emprestimoRepository.contarEmprestimosAtivos(usuario);
        if (emprestimosAtivos >= LIMITE_EMPRESTIMOS_POR_USUARIO) {
            throw new IllegalArgumentException("Limite de empréstimos atingido.");
        }

        Emprestimo emprestimo = new Emprestimo(usuario, livro, dataEmprestimo);

        livro.decrementarQuantidade();
        livroRepository.atualizar(livro);

        return emprestimoRepository.salvar(emprestimo);
    }

    public Emprestimo registrarDevolucao(Long emprestimoId) {

        Emprestimo emprestimo = emprestimoRepository.buscarPorId(emprestimoId);
        if (emprestimo == null) {
            throw new IllegalArgumentException("Empréstimo não encontrado com ID: " + emprestimoId);
        }

        if (emprestimo.isDevolvido()) {
            throw new IllegalArgumentException("Este empréstimo já foi devolvido em: " +
                    emprestimo.getDataDevolucao());
        }

        emprestimo.registrarDevolucao();

        Livro livro = emprestimo.getLivro();
        livro.incrementarQuantidade();
        livroRepository.atualizar(livro);

        Emprestimo emprestimoAtualizado = emprestimoRepository.atualizar(emprestimo);

        if (emprestimoAtualizado.isAtrasado()) {
            aplicarMulta(emprestimoAtualizado);
        }

        return emprestimoAtualizado;
    }

    private void aplicarMulta(Emprestimo emprestimo) {
        Usuario usuario = emprestimo.getUsuario();
        long diasAtraso = emprestimo.getDiasAtraso();

        int novaMulta = (int) diasAtraso;

        LocalDate dataBase = LocalDate.now();

        if (usuario.isEmMulta()) {
            dataBase = usuario.getDataFimMulta().plusDays(1);
        }

        LocalDate novaDataFimMulta = dataBase.plusDays(novaMulta);

        usuario.setDiasMulta(novaMulta);
        usuario.setDataFimMulta(novaDataFimMulta);

        usuarioRepository.atualizar(usuario);
    }

    private boolean temAtrasoPendente(Usuario usuario) {
        List<Emprestimo> ativos = emprestimoRepository.listarAtivosPorUsuario(usuario);
        for (Emprestimo emp : ativos) {
            if (emp.isAtrasado()) {
                return true;
            }
        }
        return false;
    }

    public String verificarStatusTexto(Long usuarioId) {
        Usuario usuario = usuarioRepository.buscarPorId(usuarioId);
        if (usuario == null) return "Erro";

        if (usuario.isEmMulta()) {
            return "MULTA (" + usuario.getDiasRestantesMulta() + " dias restantes)";
        }

        if (temAtrasoPendente(usuario)) {
            return "BLOQUEADO (Livro em Atraso)";
        }

        return "Livre";
    }

    public Emprestimo buscarPorId(Long id) {
        return emprestimoRepository.buscarPorId(id);
    }

    public List<Emprestimo> listarTodos() {
        return emprestimoRepository.listarTodos();
    }

    public List<Emprestimo> listarAtivos() {
        return emprestimoRepository.listarAtivos();
    }

    public List<Emprestimo> listarAtivosPorUsuario(Long usuarioId) {
        Usuario usuario = usuarioRepository.buscarPorId(usuarioId);
        if (usuario == null) {
            throw new IllegalArgumentException("Usuário não encontrado");
        }
        return emprestimoRepository.listarAtivosPorUsuario(usuario);
    }

    public List<Emprestimo> listarPorLivro(Long livroId) {
        Livro livro = livroRepository.buscarPorId(livroId);
        if (livro == null) {
            throw new IllegalArgumentException("Livro não encontrado");
        }
        return emprestimoRepository.listarPorLivro(livro);
    }

    public List<Emprestimo> listarAtrasados() {
        return emprestimoRepository.listarAtrasados();
    }

    public List<Emprestimo> listarHistoricoPorUsuario(Long usuarioId) {
        Usuario usuario = usuarioRepository.buscarPorId(usuarioId);
        if (usuario == null) {
            throw new IllegalArgumentException("Usuário não encontrado");
        }
        return emprestimoRepository.listarHistoricoPorUsuario(usuario);
    }

    public void removerEmprestimo(Long id) {
        Emprestimo emprestimo = emprestimoRepository.buscarPorId(id);
        if (emprestimo == null) {
            throw new IllegalArgumentException("Empréstimo não encontrado");
        }

        if (emprestimo.getAtivo() && !emprestimo.isDevolvido()) {
            Livro livro = emprestimo.getLivro();
            livro.incrementarQuantidade();
            livroRepository.atualizar(livro);
        }

        emprestimoRepository.remover(id);
    }
}