package br.com.ichef.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import br.com.ichef.arquitetura.BaseEntity;

@Entity
@Table(name = "vw_dashboard")
public class Dashboard extends BaseEntity {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "id")
	private Integer id;

	@Column(name = "QTD_CLIENTES")
	private Long quantidadeClientes;

	@Column(name = "QTD_FICHA")
	private Long quantidadeFichaPratos;

	@Column(name = "QTD_PEDIDOS")
	private Long quantidadePedidos;

	@Column(name = "QTD_PEDIDOS_NAO_CONFIRMADOS")
	private Long quantidadePedidosNaoConfirmados;

	@Column(name = "CD_EMPRESA")
	private Long codigoEmpresa;

	@Override
	public Object getId() {
		return id;
	}

	@Override
	public void setId(Object id) {
		this.id = (Integer) id;

	}

	@Override
	public String getColumnOrderBy() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getAuditoria() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Usuario getUsuarioAlteracao() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Date getDataAlteracao() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Usuario getUsuarioCadastro() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Date getDataCadastro() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isEdicao() {
		// TODO Auto-generated method stub
		return false;
	}

	public Long getQuantidadeClientes() {
		return quantidadeClientes;
	}

	public void setQuantidadeClientes(Long quantidadeClientes) {
		this.quantidadeClientes = quantidadeClientes;
	}

	public Long getQuantidadeFichaPratos() {
		return quantidadeFichaPratos;
	}

	public void setQuantidadeFichaPratos(Long quantidadeFichaPratos) {
		this.quantidadeFichaPratos = quantidadeFichaPratos;
	}

	public Long getQuantidadePedidos() {
		return quantidadePedidos;
	}

	public void setQuantidadePedidos(Long quantidadePedidos) {
		this.quantidadePedidos = quantidadePedidos;
	}

	public Long getQuantidadePedidosNaoConfirmados() {
		return quantidadePedidosNaoConfirmados;
	}

	public void setQuantidadePedidosNaoConfirmados(Long quantidadePedidosNaoConfirmados) {
		this.quantidadePedidosNaoConfirmados = quantidadePedidosNaoConfirmados;
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

	public void setId(Integer id) {
		this.id = id;
	}

}