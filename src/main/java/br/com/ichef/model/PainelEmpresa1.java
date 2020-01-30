package br.com.ichef.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import br.com.ichef.arquitetura.BaseEntity;

@Entity
@Table(name = "vw_painel_empresa1")
public class PainelEmpresa1 extends BaseEntity {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "PAINEL")
	private String dados;

	@Override
	public Object getId() {
		return dados;
	}

	@Override
	public void setId(Object id) {
		this.dados = (String) id;

	}

	@Override
	public String getColumnOrderBy() {
		return null;
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

}
