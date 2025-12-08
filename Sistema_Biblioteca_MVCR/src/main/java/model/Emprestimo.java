package model;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

@Entity
@Table(name = "emprestimos")
public class Emprestimo {

    public static final int PRAZO_MAXIMO_DIAS = 14;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "livro_id", nullable = false)
    private Livro livro;

    @Column(name = "data_emprestimo", nullable = false)
    private LocalDate dataEmprestimo;

    @Column(name = "data_devolucao_prevista", nullable = false)
    private LocalDate dataDevolucaoPrevista;

    @Column(name = "data_devolucao")
    private LocalDate dataDevolucao;

    @Column(name = "ativo", nullable = false)
    private Boolean ativo;

    // Construtores
    public Emprestimo() {
        this.ativo = true;
    }

    public Emprestimo(Usuario usuario, Livro livro, LocalDate dataEmprestimo) {
        this.usuario = usuario;
        this.livro = livro;
        this.dataEmprestimo = dataEmprestimo;
        this.dataDevolucaoPrevista = dataEmprestimo.plusDays(PRAZO_MAXIMO_DIAS);
        this.ativo = true;
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Livro getLivro() {
        return livro;
    }

    public void setLivro(Livro livro) {
        this.livro = livro;
    }

    public LocalDate getDataEmprestimo() {
        return dataEmprestimo;
    }

    public void setDataEmprestimo(LocalDate dataEmprestimo) {
        this.dataEmprestimo = dataEmprestimo;
        if (dataEmprestimo != null) {
            this.dataDevolucaoPrevista = dataEmprestimo.plusDays(PRAZO_MAXIMO_DIAS);
        }
    }

    public LocalDate getDataDevolucaoPrevista() {
        return dataDevolucaoPrevista;
    }

    public void setDataDevolucaoPrevista(LocalDate dataDevolucaoPrevista) {
        this.dataDevolucaoPrevista = dataDevolucaoPrevista;
    }

    public LocalDate getDataDevolucao() {
        return dataDevolucao;
    }

    public void setDataDevolucao(LocalDate dataDevolucao) {
        this.dataDevolucao = dataDevolucao;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    // Métodos auxiliares
    public boolean isAtrasado() {
        if (dataDevolucao != null) {
            return false; // Já foi devolvido
        }
        return LocalDate.now().isAfter(dataDevolucaoPrevista);
    }

    public long getDiasAtraso() {
        if (!isAtrasado()) {
            return 0;
        }
        return ChronoUnit.DAYS.between(dataDevolucaoPrevista, LocalDate.now());
    }

    public void registrarDevolucao() {
        this.dataDevolucao = LocalDate.now();
        this.ativo = false;
    }

    public boolean isDevolvido() {
        return dataDevolucao != null;
    }

    // equals, hashCode e toString
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Emprestimo that = (Emprestimo) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Emprestimo{" +
                "id=" + id +
                ", usuario=" + (usuario != null ? usuario.getNome() : "null") +
                ", livro=" + (livro != null ? livro.getTitulo() : "null") +
                ", dataEmprestimo=" + dataEmprestimo +
                ", dataDevolucaoPrevista=" + dataDevolucaoPrevista +
                ", dataDevolucao=" + dataDevolucao +
                ", ativo=" + ativo +
                '}';
    }
}
