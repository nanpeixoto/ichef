package br.com.ichef.visitor;

import java.util.Date;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import br.com.ichef.arquitetura.util.FilterVisitor;
import br.com.ichef.util.Util;

public class CardapioVisitor extends FilterVisitor {

	private Long idDiferenteDe;

	private Date dataCardapio;
	
	private Date dataInicio;
	private Date dataFim;


	@Override
	public void visitCriteria(Criteria criteria) {
		if (getDataCardapio() != null)
			criteria.add(Restrictions.sqlRestriction(
					" date_format( data, '%d/%m/%Y' ) ='" + Util.dateToString(getDataCardapio()) + "'"));

		if (getIdDiferenteDe() != null)
			criteria.add(Restrictions.ne("id", getIdDiferenteDe()));
		
		if (getDataInicio() != null && getDataFim() !=null)
			criteria.add(Restrictions.sqlRestriction(
					" data between STR_TO_DATE( '" + Util.dateToString(getDataInicio()) + "', '%d/%m/%Y' ) and  STR_TO_DATE('" + Util.dateToString(getDataFim()) + "', '%d/%m/%Y' ) "));

	}

	public Long getIdDiferenteDe() {
		return idDiferenteDe;
	}

	public void setIdDiferenteDe(Long idDiferenteDe) {
		this.idDiferenteDe = idDiferenteDe;
	}

	public Date getDataCardapio() {
		return dataCardapio;
	}

	public void setDataCardapio(Date dataCardapio) {
		this.dataCardapio = dataCardapio;
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
	

}
