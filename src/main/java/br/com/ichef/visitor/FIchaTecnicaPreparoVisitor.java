package br.com.ichef.visitor;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import br.com.ichef.arquitetura.util.FilterVisitor;

public class FIchaTecnicaPreparoVisitor extends FilterVisitor {

	private Long codigoTipoMaterialExcluido;
	
	private String nomeInsumo;

	@Override
	public void visitCriteria(Criteria criteria) {
		if (codigoTipoMaterialExcluido != null) {
			criteria.add(Restrictions.ne("tipoInsumo.id", codigoTipoMaterialExcluido));

		}
		
		if (nomeInsumo != null) {
			criteria.add(Restrictions.eq("descricao", nomeInsumo));

		}

	}

	public Long getCodigoTipoMaterialExcluido() {
		return codigoTipoMaterialExcluido;
	}

	public void setCodigoTipoMaterialExcluido(Long codigoTipoMaterialExcluido) {
		this.codigoTipoMaterialExcluido = codigoTipoMaterialExcluido;
	}

	public String getNomeInsumo() {
		return nomeInsumo;
	}

	public void setNomeInsumo(String nomeInsumo) {
		this.nomeInsumo = nomeInsumo;
	}
	
	

}
