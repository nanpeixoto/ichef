package br.com.ichef.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import br.com.ichef.arquitetura.BaseEntity;

@Entity
@Table(name = "vw_cliente_saldo")
public class VwClienteSaldo extends BaseEntity {

	private static final long serialVersionUID = 1L;

	@Id
	@ManyToOne
	@JoinColumn(name = "CD_CLIENTE")
	private Cliente cliente;

	@Column(name = "SALDO")
	private BigDecimal valorSaldo;

	// bi-directional many-to-one association to AreaLocalidade
	@OneToMany(mappedBy = "cliente")
	@LazyCollection(LazyCollectionOption.FALSE)
	private List<VwClienteCarteiraSaldo> saldos;
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cliente == null) ? 0 : cliente.hashCode());
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
		VwClienteSaldo other = (VwClienteSaldo) obj;
		if (cliente == null) {
			if (other.getId() != null)
				return false;
		} else if (!cliente.equals(other.getId()))
			return false;
		return true;
	}

	@Override
	public Object getId() {
		return this.cliente;
	}

	@Override
	public void setId(Object id) {
		this.cliente = (Cliente) id;

	}

	@Override
	public String getColumnOrderBy() {
		// TODO Auto-generated method stub
		return "cliente.nome";
	}

	@Override
	public String getAuditoria() {
		// TODO Auto-generated method stub
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

	public boolean isInclusao() {
		return (getId() == null ? true : false);
	}

	public boolean isEdicao() {
		return !isInclusao();
	}

	public Date getDataEntrega() {
		return null;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public List<VwClienteCarteiraSaldo> getSaldos() {
		return saldos;
	}

	public void setSaldos(List<VwClienteCarteiraSaldo> saldos) {
		this.saldos = saldos;
	}

	public BigDecimal getValorSaldo() {
		return valorSaldo;
	}

	public void setValorSaldo(BigDecimal valorSaldo) {
		this.valorSaldo = valorSaldo;
	}

}
