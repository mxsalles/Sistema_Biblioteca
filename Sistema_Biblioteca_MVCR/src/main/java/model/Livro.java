package model;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Objects;

/**
 * Entidade que representa um Livro no sistema de biblioteca.
 * Contém informações sobre título, autor, tema, ISBN, data de publicação e quantidade disponível.
 */
@Entity
@Table(name = "livros")
public class Livro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "titulo", nullable = false, length = 200)
    private String titulo;

    @Column(name = "tema", nullable = false, length = 100)
    private String tema;

    @Column(name = "autor", nullable = false, length = 150)
    private String autor;

    @Column(name = "isbn", nullable = false, unique = true, length = 20)
    private String isbn;

    @Column(name = "data_publicacao", nullable = false)
    private LocalDate dataPublicacao;

    @Column(name = "quantidade_disponivel", nullable = false)
    private Integer quantidadeDisponivel;

    // Construtores
    public Livro() {
    }

    public Livro(String titulo, String tema, String autor, String isbn, LocalDate dataPublicacao, Integer quantidadeDisponivel) {
        this.titulo = titulo;
        this.tema = tema;
        this.autor = autor;
        this.isbn = isbn;
        this.dataPublicacao = dataPublicacao;
        this.quantidadeDisponivel = quantidadeDisponivel;
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getTema() {
        return tema;
    }

    public void setTema(String tema) {
        this.tema = tema;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public LocalDate getDataPublicacao() {
        return dataPublicacao;
    }

    public void setDataPublicacao(LocalDate dataPublicacao) {
        this.dataPublicacao = dataPublicacao;
    }

    public Integer getQuantidadeDisponivel() {
        return quantidadeDisponivel;
    }

    public void setQuantidadeDisponivel(Integer quantidadeDisponivel) {
        this.quantidadeDisponivel = quantidadeDisponivel;
    }

    // Métodos auxiliares
    public boolean temExemplaresDisponiveis() {
        return quantidadeDisponivel != null && quantidadeDisponivel > 0;
    }

    public void decrementarQuantidade() {
        if (temExemplaresDisponiveis()) {
            this.quantidadeDisponivel--;
        }
    }

    public void incrementarQuantidade() {
        if (this.quantidadeDisponivel != null) {
            this.quantidadeDisponivel++;
        }
    }

    // equals, hashCode e toString
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Livro livro = (Livro) o;
        return Objects.equals(id, livro.id) && Objects.equals(isbn, livro.isbn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, isbn);
    }

    @Override
    public String toString() {
        return "Livro{" +
                "id=" + id +
                ", titulo='" + titulo + '\'' +
                ", autor='" + autor + '\'' +
                ", isbn='" + isbn + '\'' +
                ", quantidadeDisponivel=" + quantidadeDisponivel +
                '}';
    }
}
