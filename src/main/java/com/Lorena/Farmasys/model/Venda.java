package com.Lorena.Farmasys.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Venda {

    private Integer id;
    private LocalDateTime dataHora;
    private BigDecimal valorTotal;
    private String formaPagamento;
    private int farmaceuticoId;
    private Integer clienteId;
    private String clienteNome;
    private String farmaceuticoNome;
    private List<ItemVenda> itens = new ArrayList<>();

    public Venda() {}

    public Venda(LocalDateTime dataHora, BigDecimal valorTotal,
                 String formaPagamento, int farmaceuticoId) {
        this.dataHora       = dataHora;
        this.valorTotal     = valorTotal;
        this.formaPagamento = formaPagamento;
        this.farmaceuticoId = farmaceuticoId;
    }

    public Venda(Integer id, LocalDateTime dataHora, BigDecimal valorTotal,
                 String formaPagamento, int farmaceuticoId) {
        this.id             = id;
        this.dataHora       = dataHora;
        this.valorTotal     = valorTotal;
        this.formaPagamento = formaPagamento;
        this.farmaceuticoId = farmaceuticoId;
    }

    public Integer getId()                           { return id; }
    public void setId(Integer id)                    { this.id = id; }

    public LocalDateTime getDataHora()               { return dataHora; }
    public void setDataHora(LocalDateTime dataHora)  { this.dataHora = dataHora; }

    public BigDecimal getValorTotal()                         { return valorTotal; }
    public void setValorTotal(BigDecimal valorTotal)          { this.valorTotal = valorTotal; }

    public String getFormaPagamento()                         { return formaPagamento; }
    public void setFormaPagamento(String formaPagamento)      { this.formaPagamento = formaPagamento; }

    public int getFarmaceuticoId()                            { return farmaceuticoId; }
    public void setFarmaceuticoId(int farmaceuticoId)         { this.farmaceuticoId = farmaceuticoId; }

    public Integer getClienteId()                             { return clienteId; }
    public void setClienteId(Integer clienteId)               { this.clienteId = clienteId; }

    public String getClienteNome()                            { return clienteNome; }
    public void setClienteNome(String clienteNome)            { this.clienteNome = clienteNome; }

    public String getFarmaceuticoNome()                           { return farmaceuticoNome; }
    public void setFarmaceuticoNome(String farmaceuticoNome)      { this.farmaceuticoNome = farmaceuticoNome; }

    public List<ItemVenda> getItens()                         { return itens; }
    public void setItens(List<ItemVenda> itens)               { this.itens = itens; }
}
