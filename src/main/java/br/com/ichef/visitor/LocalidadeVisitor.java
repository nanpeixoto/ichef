package br.com.ichef.visitor;

import java.util.ArrayList;

import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;

import br.com.ichef.arquitetura.util.FilterVisitor;
import br.com.ichef.model.AreaLocalidade;
import br.com.ichef.model.Localidade;

public class LocalidadeVisitor extends FilterVisitor {

	private ArrayList<Localidade> localidadesNotIn;

	private Boolean listaDesvinculadosDasAreas;

	@Override
	public void visitCriteria(Criteria criteria) {
		if (localidadesNotIn != null) {
			for (Localidade localidade : localidadesNotIn) {
				criteria.add(Restrictions.ne("id", localidade.getId()));
			}

		}
		if (listaDesvinculadosDasAreas) {
			DetachedCriteria areaLocalidade = DetachedCriteria.forClass(AreaLocalidade.class, "rc")
					.setProjection(Projections.projectionList().add(Projections.groupProperty("rc.localidade.id")));

			criteria.add(Subqueries.propertiesNotIn(new String[] { "id" }, areaLocalidade));

		}

	}

	public ArrayList<Localidade> getLocalidadesNotIn() {
		return localidadesNotIn;
	}

	public void setLocalidadesNotIn(ArrayList<Localidade> localidadesNotIn) {
		this.localidadesNotIn = localidadesNotIn;
	}

	public Boolean getListaDesvinculadosDasAreas() {
		return listaDesvinculadosDasAreas;
	}

	public void setListaDesvinculadosDasAreas(Boolean listaDesvinculadosDasAreas) {
		this.listaDesvinculadosDasAreas = listaDesvinculadosDasAreas;
	}

}
