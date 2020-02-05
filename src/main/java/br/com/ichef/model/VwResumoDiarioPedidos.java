package br.com.ichef.model;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import br.com.ichef.arquitetura.BaseEntity;

@Entity
@Table(name = "vw_resumo_diario_pedidos")
public class VwResumoDiarioPedidos extends BaseEntity {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private VwResumoDiarioPedidosID id;

	@Column(name = "VALOR_PAGO")
	private BigDecimal valorPago;

	@Column(name = "NR_ORDEM")
	private int ordem;

	@Column(name = "NR_QTD")
	private int quantidade;

	@Column(name = "DS_CARDAPIO_FICHA")
	private String descricaoCardapioFicha;

	@Column(name = "NM_FANTASIA")
	private String nomeFantasia;

	@Override
	public Object getId() {
		// TODO Auto-generated method stub
		return id;
	}

	@Override
	public void setId(Object id) {
		this.id = (VwResumoDiarioPedidosID) id;

	}

	@Override
	public String getColumnOrderBy() {
		return "id.data";
	}

	@Override
	public String getAuditoria() {
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

	@Override
	public boolean isEdicao() {
		return false;
	}

	public BigDecimal getValorPago() {
		return valorPago;
	}

	public void setValorPago(BigDecimal valorPago) {
		this.valorPago = valorPago;
	}

	public String getDescricaoCardapioFicha() {
		return descricaoCardapioFicha;
	}

	public void setDescricaoCardapioFicha(String descricaoCardapioFicha) {
		this.descricaoCardapioFicha = descricaoCardapioFicha;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public void setId(VwResumoDiarioPedidosID id) {
		this.id = id;
	}

	public String getNomeFantasia() {
		return nomeFantasia;
	}

	public void setNomeFantasia(String nomeFantasia) {
		this.nomeFantasia = nomeFantasia;
	}

	public int getOrdem() {
		return ordem;
	}

	public void setOrdem(int ordem) {
		this.ordem = ordem;
	}

	public int getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(int quantidade) {
		this.quantidade = quantidade;
	}

}