package br.com.ichef.visitor;

import java.util.ArrayList;

import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;

import br.com.ichef.arquitetura.util.FilterVisitor;
import br.com.ichef.model.Area;
import br.com.ichef.model.EntregadorLocalidade;


public class AreaVisitor extends FilterVisitor {

	private ArrayList<Area> listaNotIn;

	private Boolean listaDesvinculados ;

	@Override
	public void visitCriteria(Criteria criteria) {
		if (listaNotIn != null) {
			for (Area localidade : listaNotIn) {
				criteria.add(Restrictions.ne("id", localidade.getId()));
			}

		}
		if (listaDesvinculados) {
			DetachedCriteria areaArea = DetachedCriteria.forClass(EntregadorLocalidade.class, "rc")
					.setProjection(Projections.projectionList().add(Projections.groupProperty("rc.localidade.id")));

			criteria.add(Subqueries.propertiesNotIn(new String[] { "id" }, areaArea));

		}

	}

	public ArrayList<Area> getListaNotIn() {
		return listaNotIn;
	}

	public void setListaNotIn(ArrayList<Area> listaNotIn) {
		this.listaNotIn = listaNotIn;
	}

	public Boolean getListaDesvinculados() {
		return listaDesvinculados;
	}

	public void setListaDesvinculados(Boolean listaDesvinculados) {
		this.listaDesvinculados = listaDesvinculados;
	}

	

}
