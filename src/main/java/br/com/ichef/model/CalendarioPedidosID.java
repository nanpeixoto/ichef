package br.com.ichef.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class CalendarioPedidosID implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name = "CD_CARDAPIO_PRATO")
	private Long codigoCardapioPrato;

	@Column(name = "DT_ENTREGA")
	private Date dataEntrega;

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codigoCardapioPrato == null) ? 0 : codigoCardapioPrato.hashCode());
		result = prime * result + ((dataEntrega == null) ? 0 : dataEntrega.hashCode());
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
		CalendarioPedidosID other = (CalendarioPedidosID) obj;
		if (codigoCardapioPrato == null) {
			if (other.codigoCardapioPrato != null)
				return false;
		} else if (!codigoCardapioPrato.equals(other.codigoCardapioPrato))
			return false;
		if (dataEntrega == null) {
			if (other.dataEntrega != null)
				return false;
		} else if (!dataEntrega.equals(other.dataEntrega))
			return false;
		return true;
	}

	public Long getCodigoCardapioPrato() {
		return codigoCardapioPrato;
	}

	public void setCodigoCardapioPrato(Long codigoCardapioPrato) {
		this.codigoCardapioPrato = codigoCardapioPrato;
	}

	public Date getDataEntrega() {
		return dataEntrega;
	}

	public void setDataEntrega(Date dataEntrega) {
		this.dataEntrega = dataEntrega;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}