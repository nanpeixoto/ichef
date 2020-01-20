package br.com.ichef.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import br.com.ichef.arquitetura.BaseEntity;

@Entity
@Table(name = "vw_calendario_pedidos")
public class CalendarioPedidos extends BaseEntity {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "id")
	private Long id;

	@Column(name = "CD_CARDAPIO_PRATO")
	private Long codigoCardapioPrato;

	@Column(name = "NR_QTD")
	private Long quantidade;

	@Column(name = "DS_CARDAPIO_FICHA")
	private String descricaoCardapdioFicha;

	@Column(name = "DT_ENTREGA")
	private Date dataEntrega;

	@Override
	public Object getId() {
		return id;
	}

	@Override
	public void setId(Object id) {
		this.id = (Long) id;

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

	public Long getCodigoCardapioPrato() {
		return codigoCardapioPrato;
	}

	public void setCodigoCardapioPrato(Long codigoCardapioPrato) {
		this.codigoCardapioPrato = codigoCardapioPrato;
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

	public Date getDataEntrega() {
		return dataEntrega;
	}

	public void setDataEntrega(Date dataEntrega) {
		this.dataEntrega = dataEntrega;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public void setId(Long id) {
		this.id = id;
	}

}