package br.com.ichef.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import br.com.ichef.arquitetura.BaseEntity;

@Entity
@Table(name = "vw_painel_pedido")
public class VwPainelPedido extends BaseEntity {

	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private VwPainelPedidoID id;

	@Column(name = "NR_QTD")
	private Integer quantidade;

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
		this.id = (VwPainelPedidoID) id;

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

	public Integer getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(Integer quantidade) {
		this.quantidade = quantidade;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public void setId(VwPainelPedidoID id) {
		this.id = id;
	}

	public Integer getOrdem() {
		return ordem;
	}

	public void setOrdem(Integer ordem) {
		this.ordem = ordem;
	}

	public String getDescricaoPrato() {
		return "(" + getOrdem() + ")" + descricaoPrato;
	}

	public void setDescricaoPrato(String descricaoPrato) {
		this.descricaoPrato = descricaoPrato;
	}

}
