package br.com.ichef.visitor;

import java.util.Date;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import br.com.ichef.arquitetura.util.FilterVisitor;
import br.com.ichef.util.Util;

public class PedidoVisitor extends FilterVisitor {

	 
	private Date data ;
	
 

	@Override
	public void visitCriteria(Criteria criteria) {
		if (getData() != null)
			criteria.add(Restrictions.sqlRestriction(" date_format( dt_pedido, '%d/%m/%Y' ) ='" + Util.dateToString( getData() ) + "'"));
	}



	public Date getData() {
		return data;
	}



	public void setData(Date data) {
		this.data = data;
	}

	 
	

}
