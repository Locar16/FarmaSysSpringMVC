package com.Lorena.Farmasys.model;

import java.time.LocalDate;

public class Cliente {

    private Integer id;
    private String nome;
    private String cpf;
    private String telefone;
    private String email;
    private String endereco;
    private LocalDate dataNascimento;

    public Cliente() {}

    public Cliente(String nome, String cpf, String telefone, String email,
                   String endereco, LocalDate dataNascimento) {
        this.nome           = nome;
        this.cpf            = cpf;
        this.telefone       = telefone;
        this.email          = email;
        this.endereco       = endereco;
        this.dataNascimento = dataNascimento;
    }

    public Cliente(Integer id, String nome, String cpf, String telefone, String email,
                   String endereco, LocalDate dataNascimento) {
        this.id             = id;
        this.nome           = nome;
        this.cpf            = cpf;
        this.telefone       = telefone;
        this.email          = email;
        this.endereco       = endereco;
        this.dataNascimento = dataNascimento;
    }

    public Integer getId()                               { return id; }
    public void setId(Integer id)                        { this.id = id; }

    public String getNome()                              { return nome; }
    public void setNome(String nome)                     { this.nome = nome; }

    public String getCpf()                               { return cpf; }
    public void setCpf(String cpf)                       { this.cpf = cpf; }

    public String getTelefone()                          { return telefone; }
    public void setTelefone(String telefone)             { this.telefone = telefone; }

    public String getEmail()                             { return email; }
    public void setEmail(String email)                   { this.email = email; }

    public String getEndereco()                          { return endereco; }
    public void setEndereco(String endereco)             { this.endereco = endereco; }

    public LocalDate getDataNascimento()                          { return dataNascimento; }
    public void setDataNascimento(LocalDate dataNascimento)       { this.dataNascimento = dataNascimento; }
}
