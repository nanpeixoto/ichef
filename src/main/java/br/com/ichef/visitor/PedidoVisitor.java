package br.com.ichef.visitor;

import java.util.Date;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import br.com.ichef.arquitetura.util.FilterVisitor;
import br.com.ichef.util.Util;

public class PedidoVisitor extends FilterVisitor {

	private Date data;

	private Date dataCardapio;

	// relatorio
	private Date dataInicial;
	private Date dataFinal;

	@Override
	public void visitCriteria(Criteria criteria) {
		if (getData() != null && getDataCardapio() == null)
			criteria.add(Restrictions
					.sqlRestriction(" date_format( dt_pedido, '%d/%m/%Y' ) ='" + Util.dateToString(getData()) + "'"));

		if (getDataCardapio() != null && getData() != null)
			criteria.add(Restrictions.or(
					Restrictions.sqlRestriction(
							" date_format( dt_pedido, '%d/%m/%Y' ) ='" + Util.dateToString(getData()) + "'"),
					Restrictions.sqlRestriction(" date_format(cardapio1_.data, '%d/%m/%Y' ) ='"
							+ Util.dateToString(getDataCardapio()) + "'")));

		if (getDataCardapio() != null && getData() == null)
			criteria.add(Restrictions.sqlRestriction(
					" date_format(cardapio1_.data, '%d/%m/%Y' ) ='" + Util.dateToString(getDataCardapio()) + "'"));

		if (getDataInicial() != null && getDataFinal() != null)
			criteria.add(Restrictions.sqlRestriction(" cardapio1_.data between STR_TO_DATE( '" + Util.dateToString(getDataInicial())
					+ "', '%d/%m/%Y' ) and  STR_TO_DATE('" + Util.dateToString(getDataFinal()) + "', '%d/%m/%Y' ) "));
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public Date getDataCardapio() {
		return dataCardapio;
	}

	public void setDataCardapio(Date dataCardapio) {
		this.dataCardapio = dataCardapio;
	}

	public Date getDataInicial() {
		return dataInicial;
	}

	public void setDataInicial(Date dataInicial) {
		this.dataInicial = dataInicial;
	}

	public Date getDataFinal() {
		return dataFinal;
	}

	public void setDataFinal(Date dataFinal) {
		this.dataFinal = dataFinal;
	}

}
