package br.com.ichef.model;

import java.math.BigDecimal;
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
	
	@Column(name = "QTD_CORTESIAS")
	private Long quantidadeCortesias;

	@Column(name = "VALOR_DEVIDO")
	private BigDecimal valorRecebido;

	@Column(name = "VALOR_PAGO")
	private BigDecimal valorPago;

	@Column(name = "VALOR_TOTAL_PAGO")
	private BigDecimal valorTotalPago;

	@Column(name = "VALOR_TOTAL_DEVIDO")
	private BigDecimal valorTotalRecebido;

	@Column(name = "QTD_TOTAL_CORTESIAS")
	private Long quantidadeTotalCortesias;

	@Column(name = "QTD_TOTAL_PEDIDOS")
	private Long quantidadeTotalPedidos;

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

	public BigDecimal getValorRecebido() {
		return valorRecebido;
	}

	public void setValorRecebido(BigDecimal valorRecebido) {
		this.valorRecebido = valorRecebido;
	}

	public BigDecimal getValorPago() {
		return valorPago;
	}

	public void setValorPago(BigDecimal valorPago) {
		this.valorPago = valorPago;
	}

	public BigDecimal getValorTotalPago() {
		return valorTotalPago;
	}

	public void setValorTotalPago(BigDecimal valorTotalPago) {
		this.valorTotalPago = valorTotalPago;
	}

	public BigDecimal getValorTotalRecebido() {
		return valorTotalRecebido;
	}

	public void setValorTotalRecebido(BigDecimal valorTotalRecebido) {
		this.valorTotalRecebido = valorTotalRecebido;
	}

	public Long getQuantidadeTotalCortesias() {
		return quantidadeTotalCortesias;
	}

	public void setQuantidadeTotalCortesias(Long quantidadeTotalCortesias) {
		this.quantidadeTotalCortesias = quantidadeTotalCortesias;
	}

	public Long getQuantidadeTotalPedidos() {
		return quantidadeTotalPedidos;
	}

	public void setQuantidadeTotalPedidos(Long quantidadeTotalPedidos) {
		this.quantidadeTotalPedidos = quantidadeTotalPedidos;
	}

	public Long getQuantidadeCortesias() {
		return quantidadeCortesias;
	}

	public void setQuantidadeCortesias(Long quantidadeCortesias) {
		this.quantidadeCortesias = quantidadeCortesias;
	}
	
	

}