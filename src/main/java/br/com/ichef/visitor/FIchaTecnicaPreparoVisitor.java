package br.com.ichef.visitor;

import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;

import br.com.ichef.arquitetura.util.FilterVisitor;
import br.com.ichef.model.FichaTecnicaPratoPreparo;

public class FIchaTecnicaPreparoVisitor extends FilterVisitor {

	private Long codigoTipoMaterialExcluido;
	
	private String nomeInsumo;
	
	private Boolean obterPreparoDesvinculadoPrato;

	@Override
	public void visitCriteria(Criteria criteria) {
		if (codigoTipoMaterialExcluido != null) {
			criteria.add(Restrictions.ne("tipoInsumo.id", codigoTipoMaterialExcluido));

		}
		
		if (nomeInsumo != null) {
			criteria.add(Restrictions.eq("descricao", nomeInsumo));

		}
		
		if (obterPreparoDesvinculadoPrato!=null && obterPreparoDesvinculadoPrato) {
			DetachedCriteria fichapratoPreparo = DetachedCriteria.forClass(FichaTecnicaPratoPreparo.class, "rc")
					.setProjection(Projections.projectionList().add(Projections.groupProperty("rc.fichaTecnicaPreparo.id")));

			criteria.add(Subqueries.propertiesNotIn(new String[] { "id" }, fichapratoPreparo));

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

	public Boolean getObterPreparoDesvinculadoPrato() {
		return obterPreparoDesvinculadoPrato;
	}

	public void setObterPreparoDesvinculadoPrato(Boolean obterPreparoDesvinculadoPrato) {
		this.obterPreparoDesvinculadoPrato = obterPreparoDesvinculadoPrato;
	}
	
	

}
