package controller;

import model.Livro;
import repository.LivroRepository;

import java.time.LocalDate;
import java.util.List;


public class LivroController {

    private final LivroRepository livroRepository;

    public LivroController() {
        this.livroRepository = new LivroRepository();
    }

    public Livro cadastrarLivro(String titulo, String tema, String autor, String isbn, 
                                 LocalDate dataPublicacao, Integer quantidadeDisponivel) {
        
        // Validações
        validarDadosLivro(titulo, tema, autor, isbn, dataPublicacao, quantidadeDisponivel);
        
        // Verifica se já existe livro com o mesmo ISBN
        Livro livroExistente = livroRepository.buscarPorIsbn(isbn);
        if (livroExistente != null) {
            throw new IllegalArgumentException("Já existe um livro cadastrado com o ISBN: " + isbn);
        }
        
        // Cria e salva o livro
        Livro livro = new Livro(titulo, tema, autor, isbn, dataPublicacao, quantidadeDisponivel);
        return livroRepository.salvar(livro);
    }

    public Livro atualizarLivro(Long id, String titulo, String tema, String autor, String isbn,
                                LocalDate dataPublicacao, Integer quantidadeDisponivel) {
        
        // Validações
        validarDadosLivro(titulo, tema, autor, isbn, dataPublicacao, quantidadeDisponivel);
        
        // Busca o livro existente
        Livro livro = livroRepository.buscarPorId(id);
        if (livro == null) {
            throw new IllegalArgumentException("Livro não encontrado com ID: " + id);
        }
        
        // Verifica se o ISBN foi alterado e se já existe outro livro com o novo ISBN
        if (!livro.getIsbn().equals(isbn)) {
            Livro livroComMesmoIsbn = livroRepository.buscarPorIsbn(isbn);
            if (livroComMesmoIsbn != null && !livroComMesmoIsbn.getId().equals(id)) {
                throw new IllegalArgumentException("Já existe outro livro cadastrado com o ISBN: " + isbn);
            }
        }
        
        // Atualiza os dados
        livro.setTitulo(titulo);
        livro.setTema(tema);
        livro.setAutor(autor);
        livro.setIsbn(isbn);
        livro.setDataPublicacao(dataPublicacao);
        livro.setQuantidadeDisponivel(quantidadeDisponivel);
        
        return livroRepository.atualizar(livro);
    }

    public void removerLivro(Long id) {
        Livro livro = livroRepository.buscarPorId(id);
        if (livro == null) {
            throw new IllegalArgumentException("Livro não encontrado com ID: " + id);
        }
        livroRepository.remover(id);
    }

    public Livro buscarPorId(Long id) {
        return livroRepository.buscarPorId(id);
    }
    public Livro buscarPorIsbn(String isbn) {
        return livroRepository.buscarPorIsbn(isbn);
    }
    public List<Livro> listarTodos() {
        return livroRepository.listarTodos();
    }
    public List<Livro> listarDisponiveis() {
        return livroRepository.listarDisponiveis();
    }

    public List<Livro> buscarPorTitulo(String titulo) {
        if (titulo == null || titulo.trim().isEmpty()) {
            return listarTodos();
        }
        return livroRepository.buscarPorTitulo(titulo);
    }

    public List<Livro> buscarPorAutor(String autor) {
        if (autor == null || autor.trim().isEmpty()) {
            return listarTodos();
        }
        return livroRepository.buscarPorAutor(autor);
    }

    public List<Livro> buscarPorTema(String tema) {
        if (tema == null || tema.trim().isEmpty()) {
            return listarTodos();
        }
        return livroRepository.buscarPorTema(tema);
    }
    private void validarDadosLivro(String titulo, String tema, String autor, String isbn,
                                   LocalDate dataPublicacao, Integer quantidadeDisponivel) {
        
        if (titulo == null || titulo.trim().isEmpty()) {
            throw new IllegalArgumentException("O título do livro é obrigatório");
        }
        
        if (tema == null || tema.trim().isEmpty()) {
            throw new IllegalArgumentException("O tema do livro é obrigatório");
        }
        
        if (autor == null || autor.trim().isEmpty()) {
            throw new IllegalArgumentException("O autor do livro é obrigatório");
        }
        
        if (isbn == null || isbn.trim().isEmpty()) {
            throw new IllegalArgumentException("O ISBN do livro é obrigatório");
        }
        
        if (!validarFormatoIsbn(isbn)) {
            throw new IllegalArgumentException("O ISBN informado não é válido. Use formato ISBN-10 ou ISBN-13");
        }
        
        if (dataPublicacao == null) {
            throw new IllegalArgumentException("A data de publicação é obrigatória");
        }
        
        if (dataPublicacao.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("A data de publicação não pode ser futura");
        }
        
        if (quantidadeDisponivel == null || quantidadeDisponivel < 0) {
            throw new IllegalArgumentException("A quantidade disponível deve ser maior ou igual a zero");
        }
    }

    private boolean validarFormatoIsbn(String isbn) {
        if (isbn == null) {
            return false;
        }
        
        // Remove hífens e espaços
        String isbnLimpo = isbn.replaceAll("[\\s-]", "");
        
        // Verifica se é ISBN-10 (10 dígitos) ou ISBN-13 (13 dígitos)
        return isbnLimpo.matches("\\d{10}") || isbnLimpo.matches("\\d{13}");
    }
}
