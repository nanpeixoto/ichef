package br.com.ichef.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class VwResumoDiarioPedidosID implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name = "CD_FORMA_PAGAMENTO")
	private Long codigoFormaPagamento;

	@Column(name = "DATA")
	private Date data;

	@Column(name = "CD_EMPRESA")
	private Long codigoEmpresa;

	@Column(name = "CD_CARDAPIO_PRATO")
	private Long codigoCardapioFicha;

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codigoCardapioFicha == null) ? 0 : codigoCardapioFicha.hashCode());
		result = prime * result + ((codigoEmpresa == null) ? 0 : codigoEmpresa.hashCode());
		result = prime * result + ((codigoFormaPagamento == null) ? 0 : codigoFormaPagamento.hashCode());
		result = prime * result + ((data == null) ? 0 : data.hashCode());
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
		VwResumoDiarioPedidosID other = (VwResumoDiarioPedidosID) obj;
		if (codigoCardapioFicha == null) {
			if (other.codigoCardapioFicha != null)
				return false;
		} else if (!codigoCardapioFicha.equals(other.codigoCardapioFicha))
			return false;
		if (codigoEmpresa == null) {
			if (other.codigoEmpresa != null)
				return false;
		} else if (!codigoEmpresa.equals(other.codigoEmpresa))
			return false;
		if (codigoFormaPagamento == null) {
			if (other.codigoFormaPagamento != null)
				return false;
		} else if (!codigoFormaPagamento.equals(other.codigoFormaPagamento))
			return false;
		if (data == null) {
			if (other.data != null)
				return false;
		} else if (!data.equals(other.data))
			return false;
		return true;
	}

	public Long getCodigoCardapioFicha() {
		return codigoCardapioFicha;
	}

	public void setCodigoCardapioFicha(Long codigoCardapioFicha) {
		this.codigoCardapioFicha = codigoCardapioFicha;
	}

	public Long getCodigoEmpresa() {
		return codigoEmpresa;
	}

	public void setCodigoEmpresa(Long codigoEmpresa) {
		this.codigoEmpresa = codigoEmpresa;
	}

	public Long getCodigoFormaPagamento() {
		return codigoFormaPagamento;
	}

	public void setCodigoFormaPagamento(Long codigoFormaPagamento) {
		this.codigoFormaPagamento = codigoFormaPagamento;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}