package br.com.ichef.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class PedidoDerivacaoContagemID implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name = "CD_DERIVACAO")
	private Long id;

	@Column(name = "DS_DERIVACAO")
	private String descricao;

	@Column(name = "CD_EMPRESA")
	private Long codigoEmpresa;

	@Column(name = "CD_ENTREGADOR")
	private Long codigoEntregador;

	@Column(name = "CD_CARDAPIO_PRATO")
	private Long codigoCardapioPrato;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Long getCodigoEmpresa() {
		return codigoEmpresa;
	}

	public void setCodigoEmpresa(Long codigoEmpresa) {
		this.codigoEmpresa = codigoEmpresa;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Long getCodigoEntregador() {
		return codigoEntregador;
	}

	public void setCodigoEntregador(Long codigoEntregador) {
		this.codigoEntregador = codigoEntregador;
	}

	public Long getCodigoCardapioPrato() {
		return codigoCardapioPrato;
	}

	public void setCodigoCardapioPrato(Long codigoCardapioPrato) {
		this.codigoCardapioPrato = codigoCardapioPrato;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codigoCardapioPrato == null) ? 0 : codigoCardapioPrato.hashCode());
		result = prime * result + ((codigoEmpresa == null) ? 0 : codigoEmpresa.hashCode());
		result = prime * result + ((codigoEntregador == null) ? 0 : codigoEntregador.hashCode());
		result = prime * result + ((descricao == null) ? 0 : descricao.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PedidoDerivacaoContagemID other = (PedidoDerivacaoContagemID) obj;
		if (codigoCardapioPrato == null) {
			if (other.codigoCardapioPrato != null)
				return false;
		} else if (!codigoCardapioPrato.equals(other.codigoCardapioPrato))
			return false;
		if (codigoEmpresa == null) {
			if (other.codigoEmpresa != null)
				return false;
		} else if (!codigoEmpresa.equals(other.codigoEmpresa))
			return false;
		if (codigoEntregador == null) {
			if (other.codigoEntregador != null)
				return false;
		} else if (!codigoEntregador.equals(other.codigoEntregador))
			return false;
		if (descricao == null) {
			if (other.descricao != null)
				return false;
		} else if (!descricao.equals(other.descricao))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
