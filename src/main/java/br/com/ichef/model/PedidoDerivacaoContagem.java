package br.com.ichef.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.com.ichef.arquitetura.BaseEntity;

@Entity
@Table(name = "vw_pedido_derivacao_contagem")
public class PedidoDerivacaoContagem extends BaseEntity {

	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private PedidoDerivacaoContagemID id;

	@Temporal(TemporalType.DATE)
	@Column(name = "DT_ENTREGA")
	private Date dataEntrega;

	@Column(name = "NR_QTD")
	private Integer quantidade;
	
	@Column(name = "NM_ENTREGADOR")
	private String nomeEntregador;

	@Column(name = "NR_ORDEM")
	private Integer ordem;

	@Column(name = "DS_CARDAPIO_PRATO")
	private String descricaoPrato;

	@Override
	public Object getId() {
		// TODO Auto-generated method stub
		return id;
	}

	@Override
	public void setId(Object id) {
		this.id = (PedidoDerivacaoContagemID) id;

	}

	@Override
	public String getColumnOrderBy() {
		return "nomeEntregador";
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

	public Date getDataEntrega() {
		return dataEntrega;
	}

	public void setDataEntrega(Date dataEntrega) {
		this.dataEntrega = dataEntrega;
	}

	public Integer getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(Integer quantidade) {
		this.quantidade = quantidade;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public void setId(PedidoDerivacaoContagemID id) {
		this.id = id;
	}

	public Integer getOrdem() {
		return ordem;
	}

	public void setOrdem(Integer ordem) {
		this.ordem = ordem;
	}

	public String getDescricaoPrato() {
		return "("+getOrdem()+")"+descricaoPrato;
	}

	public void setDescricaoPrato(String descricaoPrato) {
		this.descricaoPrato = descricaoPrato;
	}

	public String getNomeEntregador() {
		return nomeEntregador;
	}

	public void setNomeEntregador(String nomeEntregador) {
		this.nomeEntregador = nomeEntregador;
	}
	
	

}
