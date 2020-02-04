package br.com.ichef.visitor;

import java.util.Date;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import br.com.ichef.arquitetura.util.FilterVisitor;
import br.com.ichef.util.Util;

public class VwResumoDiarioPedidosVisitor extends FilterVisitor {

	private Date dataInicio;
	private Date dataFim;

	private Long codigoEmpresa;

	@Override
	public void visitCriteria(Criteria criteria) {

		if (getDataInicio() != null && getDataFim() != null) {
			//criteria.createAlias("id", "pk");
			criteria.add(Restrictions.sqlRestriction(" id.data between STR_TO_DATE( '"
					+ Util.dateToString(getDataInicio()) + "', '%d/%m/%Y' ) and  STR_TO_DATE('"
					+ Util.dateToString(getDataFim()) + "', '%d/%m/%Y' ) "));
		}

		if (getCodigoEmpresa()!=null) {
			criteria.add(Restrictions.eq(" id.codigoEmpresa", getCodigoEmpresa()));
		}

	}

	public Date getDataInicio() {
		return dataInicio;
	}

	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}

	public Date getDataFim() {
		return dataFim;
	}

	public void setDataFim(Date dataFim) {
		this.dataFim = dataFim;
	}

	public Long getCodigoEmpresa() {
		return codigoEmpresa;
	}

	public void setCodigoEmpresa(Long codigoEmpresa) {
		this.codigoEmpresa = codigoEmpresa;
	}

}
