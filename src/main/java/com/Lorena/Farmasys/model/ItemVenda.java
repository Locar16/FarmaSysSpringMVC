package com.Lorena.Farmasys.model;

import java.math.BigDecimal;

public class ItemVenda {

    private Integer id;
    private Integer vendaId;
    private int medicamentoId;
    private String medicamentoNome;
    private String medicamentoClassificacao;
    private int quantidade;
    private BigDecimal valorUnitario;

    public ItemVenda() {}

    public ItemVenda(Integer vendaId, int medicamentoId, String medicamentoNome,
                     int quantidade, BigDecimal valorUnitario) {
        this.vendaId        = vendaId;
        this.medicamentoId  = medicamentoId;
        this.medicamentoNome = medicamentoNome;
        this.quantidade     = quantidade;
        this.valorUnitario  = valorUnitario;
    }

    public ItemVenda(Integer id, Integer vendaId, int medicamentoId, String medicamentoNome,
                     int quantidade, BigDecimal valorUnitario) {
        this.id             = id;
        this.vendaId        = vendaId;
        this.medicamentoId  = medicamentoId;
        this.medicamentoNome = medicamentoNome;
        this.quantidade     = quantidade;
        this.valorUnitario  = valorUnitario;
    }

    public BigDecimal getSubtotal() {
        if (valorUnitario == null) return BigDecimal.ZERO;
        return valorUnitario.multiply(BigDecimal.valueOf(quantidade));
    }

    public Integer getId()                           { return id; }
    public void setId(Integer id)                    { this.id = id; }

    public Integer getVendaId()                      { return vendaId; }
    public void setVendaId(Integer vendaId)          { this.vendaId = vendaId; }

    public int getMedicamentoId()                    { return medicamentoId; }
    public void setMedicamentoId(int medicamentoId)  { this.medicamentoId = medicamentoId; }

    public String getMedicamentoNome()                                   { return medicamentoNome; }
    public void setMedicamentoNome(String medicamentoNome)               { this.medicamentoNome = medicamentoNome; }

    public String getMedicamentoClassificacao()                                      { return medicamentoClassificacao; }
    public void setMedicamentoClassificacao(String medicamentoClassificacao)         { this.medicamentoClassificacao = medicamentoClassificacao; }

    public int getQuantidade()                       { return quantidade; }
    public void setQuantidade(int quantidade)        { this.quantidade = quantidade; }

    public BigDecimal getValorUnitario()                      { return valorUnitario; }
    public void setValorUnitario(BigDecimal valorUnitario)    { this.valorUnitario = valorUnitario; }
}
