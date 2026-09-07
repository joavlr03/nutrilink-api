package br.com.joavlr03.nutrilink_api.dto.doadora;

import java.time.LocalDate;
import java.util.UUID;

import br.com.joavlr03.nutrilink_api.model.enums.StatusCadastro;

public class DoadoraResponse {
    private UUID id;
    private String nomeCompleto;
    private String cpf;
    private LocalDate dataNascimento;
    private String telefone;
    private String cep;
    private String enderecoCompleto;
    private StatusCadastro statusCadastro;

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
