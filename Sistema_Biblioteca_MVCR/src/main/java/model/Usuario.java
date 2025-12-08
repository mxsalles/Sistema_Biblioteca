package model;

import javax.persistence.*;
import java.util.Objects;
import java.time.LocalDate;

@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "nome", nullable = false, length = 150)
    private String nome;

    @Column(name = "sexo", nullable = false, length = 20)
    private String sexo;

    @Column(name = "celular", nullable = false, length = 20)
    private String celular;

    @Column(name = "email", nullable = false, unique = true, length = 100)
    private String email;

    @Column(name = "dias_multa")
    private Integer diasMulta; // Dias de multa sem poder emprestar

    @Column(name = "data_fim_multa")
    private java.time.LocalDate dataFimMulta; // Data em que a multa termina

    // Construtores
    public Usuario() {
        this.diasMulta = 0;
        this.dataFimMulta = null;
    }

    public Usuario(String nome, String sexo, String celular, String email) {
        this.nome = nome;
        this.sexo = sexo;
        this.celular = celular;
        this.email = email;
        this.diasMulta = 0;
        this.dataFimMulta = null;
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getDiasMulta() {
        return diasMulta;
    }

    public void setDiasMulta(Integer diasMulta) {
        this.diasMulta = diasMulta;
    }

    public LocalDate getDataFimMulta() {
        return dataFimMulta;
    }

    public void setDataFimMulta(LocalDate dataFimMulta) {
        this.dataFimMulta = dataFimMulta;
    }

    /**
     * Verifica se o usuário está atualmente sob multa.
     * @return true se estiver em período de multa.
     */
    public boolean isEmMulta() {
        if (dataFimMulta == null) {
            return false;
        }
        // Verifica se a data de fim da multa é igual ou posterior à data atual
        return dataFimMulta.isAfter(LocalDate.now()) || dataFimMulta.isEqual(LocalDate.now());
    }

    /**
     * Calcula os dias restantes de multa.
     * @return O número de dias restantes de multa (0 se não estiver em multa).
     */
    public long getDiasRestantesMulta() {
        if (!isEmMulta()) {
            return 0;
        }
        return java.time.temporal.ChronoUnit.DAYS.between(LocalDate.now(), dataFimMulta) + 1; // +1 para incluir o dia atual
    }

    // equals, hashCode e toString
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Usuario usuario = (Usuario) o;
        return Objects.equals(id, usuario.id) && Objects.equals(email, usuario.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email);
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", sexo='" + sexo + '\'' +
                ", celular='" + celular + '\'' +
                ", email='" + email + '\'' +
                ", diasMulta=" + diasMulta +
                ", dataFimMulta=" + dataFimMulta +
                '}';
    }
}
