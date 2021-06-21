package br.com.ichef.service;

import java.util.Date;
import java.util.List;

import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;

import br.com.ichef.dao.GenericDAO;
import br.com.ichef.model.Empresa;
import br.com.ichef.model.VwPrevisaoInsumo;
import br.com.ichef.util.Util;

public class PrevisaoInsumoService extends GenericDAO<VwPrevisaoInsumo> {
	private static final long serialVersionUID = 1L;

	public List<VwPrevisaoInsumo> findPrevisao(Empresa empresa, Date dataInicial, Date dataFinal) {
		@SuppressWarnings("unchecked")
		List<VwPrevisaoInsumo> result = getSession().createCriteria(VwPrevisaoInsumo.class)
				.add(Restrictions.eq("id.codigoEmpresa", empresa.getId()))
				.add(Restrictions.sqlRestriction(" data between  STR_TO_DATE( '"
						+ Util.dateToString(dataInicial) + "', '%d/%m/%Y' ) and  STR_TO_DATE( '" + Util.dateToString(dataFinal) + "', '%d/%m/%Y' ) "))
				.setProjection(Projections.projectionList().add(Projections.groupProperty("id.codigoInsumo"))
						.add(Projections.groupProperty("id.codigoEmpresa"))
						.add(Projections.groupProperty("descricaoInsumo"))
						.add(Projections.groupProperty("codigoTipoInsumo"))
						.add(Projections.groupProperty("descricaoTipoInsumo"))
						.add(Projections.groupProperty("siglaUnidade"))
						.add(Projections.property("descricaoInsumo"), "descricaoInsumo")
						.add(Projections.property("codigoTipoInsumo"), "codigoTipoInsumo")
						.add(Projections.property("descricaoTipoInsumo"), "descricaoTipoInsumo")
						.add(Projections.property("siglaUnidade"), "siglaUnidade")
						.add(Projections.sum("qtdTotal"), "qtdTotal"))
				.setResultTransformer(Transformers.aliasToBean(VwPrevisaoInsumo.class))
				.addOrder(Order.asc("descricaoTipoInsumo")).addOrder(Order.asc("descricaoInsumo")).list();

		return result;
	}

}
