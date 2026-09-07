package br.com.joavlr03.nutrilink_api.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.UUID;

import br.com.joavlr03.nutrilink_api.model.enums.StatusCadastro;

@Entity
@Table(name = "tb_doadoras")
public class Doadora {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 150)
    private String nomeCompleto;

    @Column(nullable = false, unique = true, length = 11)
    private String cpf;

    @Column(nullable = false)
    private LocalDate dataNascimento;

    @Column(nullable = false, length = 20)
    private String telefone;

    @Column(nullable = false, length = 8)
    private String cep;

    @Column(nullable = false, length = 300)
    private String enderecoCompleto;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatusCadastro statusCadastro;

    public Doadora() {}

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getNomeCompleto() { return nomeCompleto; }
    public void setNomeCompleto(String nomeCompleto) { this.nomeCompleto = nomeCompleto; }

    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }

    public LocalDate getDataNascimento() { return dataNascimento; }
    public void setDataNascimento(LocalDate dataNascimento) { this.dataNascimento = dataNascimento; }

    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }

    public String getCep() { return cep; }
    public void setCep(String cep) { this.cep = cep; }

    public String getEnderecoCompleto() { return enderecoCompleto; }
    public void setEnderecoCompleto(String enderecoCompleto) { this.enderecoCompleto = enderecoCompleto; }

    public StatusCadastro getStatusCadastro() { return statusCadastro; }
    public void setStatusCadastro(StatusCadastro statusCadastro) { this.statusCadastro = statusCadastro; }
}