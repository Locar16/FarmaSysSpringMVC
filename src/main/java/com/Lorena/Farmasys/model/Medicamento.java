package com.Lorena.Farmasys.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Medicamento {

    private Integer id;
    private String nome;
    private String principioAtivo;
    private String dosagem;
    private String laboratorio;
    private String lote;
    private LocalDate validade;
    private String classificacao;
    private BigDecimal precoVenda;
    private int estoqueAtual;
    private String tipo;
    private String grupoFarmacologico;

    public Medicamento() {}

    public Medicamento(String nome, String principioAtivo, String dosagem, String laboratorio,
                       String lote, LocalDate validade, String classificacao,
                       BigDecimal precoVenda, int estoqueAtual, String tipo, String grupoFarmacologico) {
        this.nome               = nome;
        this.principioAtivo     = principioAtivo;
        this.dosagem            = dosagem;
        this.laboratorio        = laboratorio;
        this.lote               = lote;
        this.validade           = validade;
        this.classificacao      = classificacao;
        this.precoVenda         = precoVenda;
        this.estoqueAtual       = estoqueAtual;
        this.tipo               = tipo;
        this.grupoFarmacologico = grupoFarmacologico;
    }

    public Medicamento(Integer id, String nome, String principioAtivo, String dosagem, String laboratorio,
                       String lote, LocalDate validade, String classificacao,
                       BigDecimal precoVenda, int estoqueAtual, String tipo, String grupoFarmacologico) {
        this.id                 = id;
        this.nome               = nome;
        this.principioAtivo     = principioAtivo;
        this.dosagem            = dosagem;
        this.laboratorio        = laboratorio;
        this.lote               = lote;
        this.validade           = validade;
        this.classificacao      = classificacao;
        this.precoVenda         = precoVenda;
        this.estoqueAtual       = estoqueAtual;
        this.tipo               = tipo;
        this.grupoFarmacologico = grupoFarmacologico;
    }

    public Integer getId()                          { return id; }
    public void setId(Integer id)                   { this.id = id; }

    public String getNome()                         { return nome; }
    public void setNome(String nome)                { this.nome = nome; }

    public String getPrincipioAtivo()                       { return principioAtivo; }
    public void setPrincipioAtivo(String principioAtivo)    { this.principioAtivo = principioAtivo; }

    public String getDosagem()                      { return dosagem; }
    public void setDosagem(String dosagem)          { this.dosagem = dosagem; }

    public String getLaboratorio()                  { return laboratorio; }
    public void setLaboratorio(String laboratorio)  { this.laboratorio = laboratorio; }

    public String getLote()                         { return lote; }
    public void setLote(String lote)                { this.lote = lote; }

    public LocalDate getValidade()                  { return validade; }
    public void setValidade(LocalDate validade)     { this.validade = validade; }

    public String getClassificacao()                        { return classificacao; }
    public void setClassificacao(String classificacao)      { this.classificacao = classificacao; }

    public BigDecimal getPrecoVenda()               { return precoVenda; }
    public void setPrecoVenda(BigDecimal precoVenda) { this.precoVenda = precoVenda; }

    public int getEstoqueAtual()                    { return estoqueAtual; }
    public void setEstoqueAtual(int estoqueAtual)   { this.estoqueAtual = estoqueAtual; }

    public String getTipo()                         { return tipo; }
    public void setTipo(String tipo)                { this.tipo = tipo; }

    public String getGrupoFarmacologico()                           { return grupoFarmacologico; }
    public void setGrupoFarmacologico(String grupoFarmacologico)    { this.grupoFarmacologico = grupoFarmacologico; }
}
