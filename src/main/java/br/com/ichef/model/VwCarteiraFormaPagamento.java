package br.com.ichef.model;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import br.com.ichef.arquitetura.BaseEntity;

@Entity
@Table(name = "vw_carteira_forma_pagamento")
public class VwCarteiraFormaPagamento extends BaseEntity {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private VwCarteiraFormaPagamentoID id;

	@Column(name = "VALOR_PAGO")
	private BigDecimal valorPago;

	@Column(name = "DS_FORMA_PAGAMENTO")
	private String descricaoFormaPagamento;

	@Column(name = "NM_FANTASIA")
	private String nomeFantasia;

	@Override
	public Object getId() {
		// TODO Auto-generated method stub
		return id;
	}

	@Override
	public void setId(Object id) {
		this.id = (VwCarteiraFormaPagamentoID) id;

	}

	@Override
	public String getColumnOrderBy() {
		return "id.data";
	}

	@Override
	public String getAuditoria() {
		return null;
	}

	@Override
	public Usuario getUsuarioAlteracao() {
		return null;
	}

	@Override
	public Date getDataAlteracao() {
		return null;
	}

	@Override
	public Usuario getUsuarioCadastro() {
		return null;
	}

	@Override
	public Date getDataCadastro() {
		return null;
	}

	@Override
	public boolean isEdicao() {
		return false;
	}

	public BigDecimal getValorPago() {
		return valorPago;
	}

	public void setValorPago(BigDecimal valorPago) {
		this.valorPago = valorPago;
	}

	public String getDescricaoFormaPagamento() {
		return descricaoFormaPagamento;
	}

	public void setDescricaoFormaPagamento(String descricaoFormaPagamento) {
		this.descricaoFormaPagamento = descricaoFormaPagamento;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public void setId(VwCarteiraFormaPagamentoID id) {
		this.id = id;
	}

	public String getNomeFantasia() {
		return nomeFantasia;
	}

	public void setNomeFantasia(String nomeFantasia) {
		this.nomeFantasia = nomeFantasia;
	}

}