package br.com.ichef.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import br.com.ichef.arquitetura.BaseEntity;

@Entity
@Table(name = "vw_calendario_pedidos")
public class CalendarioPedidos extends BaseEntity {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private CalendarioPedidosID id;

	@Column(name = "NR_QTD")
	private Long quantidade;

	@Column(name = "DS_CARDAPIO_FICHA")
	private String descricaoCardapdioFicha;

	@Override
	public Object getId() {
		return id;
	}

	@Override
	public void setId(Object id) {
		this.id = (CalendarioPedidosID) id;

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

	public Long getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(Long quantidade) {
		this.quantidade = quantidade;
	}

	public String getDescricaoCardapdioFicha() {
		return descricaoCardapdioFicha;
	}

	public void setDescricaoCardapdioFicha(String descricaoCardapdioFicha) {
		this.descricaoCardapdioFicha = descricaoCardapdioFicha;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public void setId(CalendarioPedidosID id) {
		this.id = id;
	}

}