package br.com.ichef.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import br.com.ichef.arquitetura.BaseEntity;

@Entity
@Table(name = "vw_previsao_insumo")
public class VwPrevisaoInsumo extends BaseEntity {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private VwPrevisaoInsumoID id;

	@Column(name = "DS_INSUMO")
	private String descricaoInsumo;

	@Column(name = "CD_TIP_INSUMO")
	private Long codigoTipoInsumo;

	@Column(name = "DS_TIP_INSUMO")
	private String descricaoTipoInsumo;

	@Column(name = "qtd_total")
	private Double qtdTotal;

	@Column(name = "SIGLA_UNIDADE")
	private String siglaUnidade;

	@Override
	public Object getId() {
		return id;
	}

	@Override
	public void setId(Object id) {
		this.id = (VwPrevisaoInsumoID) id;

	}

	@Override
	public String getColumnOrderBy() {
		return "descricaoInsumo";
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

	public String getDescricaoInsumo() {
		return descricaoInsumo;
	}

	public void setDescricaoInsumo(String descricaoInsumo) {
		this.descricaoInsumo = descricaoInsumo;
	}

	public Double getQtdTotal() {
		return qtdTotal;
	}

	public void setQtdTotal(Double qtdTotal) {
		this.qtdTotal = qtdTotal;
	}

	public String getSiglaUnidade() {
		return siglaUnidade;
	}

	public void setSiglaUnidade(String siglaUnidade) {
		this.siglaUnidade = siglaUnidade;
	}

	public Long getCodigoTipoInsumo() {
		return codigoTipoInsumo;
	}

	public void setCodigoTipoInsumo(Long codigoTipoInsumo) {
		this.codigoTipoInsumo = codigoTipoInsumo;
	}

	public String getDescricaoTipoInsumo() {
		return descricaoTipoInsumo;
	}

	public void setDescricaoTipoInsumo(String descricaoTipoInsumo) {
		this.descricaoTipoInsumo = descricaoTipoInsumo;
	}

}