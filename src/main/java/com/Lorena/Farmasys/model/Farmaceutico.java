package com.Lorena.Farmasys.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Farmaceutico {

    private Integer id;
    private String usuario;
    private String crfNumero;
    private String crfUf;
    private String senha;
    private String nome;
    private String cpf;
    private String email;
    private String telefone;
    private BigDecimal salario;
    private LocalDate dataAdmissao;
    private boolean ativo;

    public Farmaceutico() {}

    public Farmaceutico(String usuario, String crfNumero, String crfUf, String senha, String nome,
                        String cpf, String email, String telefone, BigDecimal salario,
                        LocalDate dataAdmissao, boolean ativo) {
        this.usuario     = usuario;
        this.crfNumero   = crfNumero;
        this.crfUf       = crfUf;
        this.senha       = senha;
        this.nome        = nome;
        this.cpf         = cpf;
        this.email       = email;
        this.telefone    = telefone;
        this.salario     = salario;
        this.dataAdmissao = dataAdmissao;
        this.ativo       = ativo;
    }

    public Farmaceutico(Integer id, String usuario, String crfNumero, String crfUf, String senha,
                        String nome, String cpf, String email, String telefone,
                        BigDecimal salario, LocalDate dataAdmissao, boolean ativo) {
        this.id          = id;
        this.usuario     = usuario;
        this.crfNumero   = crfNumero;
        this.crfUf       = crfUf;
        this.senha       = senha;
        this.nome        = nome;
        this.cpf         = cpf;
        this.email       = email;
        this.telefone    = telefone;
        this.salario     = salario;
        this.dataAdmissao = dataAdmissao;
        this.ativo       = ativo;
    }

    public Integer getId()                      { return id; }
    public void setId(Integer id)               { this.id = id; }

    public String getUsuario()                  { return usuario; }
    public void setUsuario(String usuario)      { this.usuario = usuario; }

    public String getCrfNumero()                { return crfNumero; }
    public void setCrfNumero(String crfNumero)  { this.crfNumero = crfNumero; }

    public String getCrfUf()                    { return crfUf; }
    public void setCrfUf(String crfUf)          { this.crfUf = crfUf; }

    public String getSenha()                    { return senha; }
    public void setSenha(String senha)          { this.senha = senha; }

    public String getNome()                     { return nome; }
    public void setNome(String nome)            { this.nome = nome; }

    public String getCpf()                      { return cpf; }
    public void setCpf(String cpf)              { this.cpf = cpf; }

    public String getEmail()                    { return email; }
    public void setEmail(String email)          { this.email = email; }

    public String getTelefone()                 { return telefone; }
    public void setTelefone(String telefone)    { this.telefone = telefone; }

    public BigDecimal getSalario()              { return salario; }
    public void setSalario(BigDecimal salario)  { this.salario = salario; }

    public LocalDate getDataAdmissao()                   { return dataAdmissao; }
    public void setDataAdmissao(LocalDate dataAdmissao)  { this.dataAdmissao = dataAdmissao; }

    public boolean isAtivo()                    { return ativo; }
    public void setAtivo(boolean ativo)         { this.ativo = ativo; }
}
