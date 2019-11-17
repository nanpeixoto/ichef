package br.com.ichef.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class VwPrevisaoInsumoID implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column(name = "CD_INSUMO")
	private Long codigoInsumo;

	@Column(name = "CD_EMPRESA")
	private Long codigoEmpresa;

	// must have a default construcot
	public VwPrevisaoInsumoID() {
	}

	public VwPrevisaoInsumoID(Long codigoInsumo, Long codigoEmpresa) {
		this.codigoInsumo = codigoInsumo;
		this.codigoEmpresa = codigoEmpresa;
	}

	public Long getCodigoInsumo() {
		return codigoInsumo;
	}

	public void setCodigoInsumo(Long codigoInsumo) {
		this.codigoInsumo = codigoInsumo;
	}

	public Long getCodigoEmpresa() {
		return codigoEmpresa;
	}

	public void setCodigoEmpresa(Long codigoEmpresa) {
		this.codigoEmpresa = codigoEmpresa;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codigoEmpresa == null) ? 0 : codigoEmpresa.hashCode());
		result = prime * result + ((codigoInsumo == null) ? 0 : codigoInsumo.hashCode());
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
		VwPrevisaoInsumoID other = (VwPrevisaoInsumoID) obj;
		if (codigoEmpresa == null) {
			if (other.codigoEmpresa != null)
				return false;
		} else if (!codigoEmpresa.equals(other.codigoEmpresa))
			return false;
		if (codigoInsumo == null) {
			if (other.codigoInsumo != null)
				return false;
		} else if (!codigoInsumo.equals(other.codigoInsumo))
			return false;
		return true;
	}

}
