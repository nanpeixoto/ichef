package br.com.ichef.visitor;

import java.util.Date;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import br.com.ichef.arquitetura.util.FilterVisitor;
import br.com.ichef.util.Util;

public class PedidoVisitor extends FilterVisitor {

	 
	private Date data ;
	
	private Date dataCardapio ;
	
 

	@Override
	public void visitCriteria(Criteria criteria) {
		if (getData() != null)
			criteria.add(Restrictions.sqlRestriction(" date_format( dt_pedido, '%d/%m/%Y' ) ='" + Util.dateToString( getData() ) + "'"));
		
		if (getDataCardapio() != null)
			criteria.add(Restrictions.sqlRestriction(" date_format( DT_CARDAPIO, '%d/%m/%Y' ) ='" + Util.dateToString( getDataCardapio() ) + "'"));
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

	 
	

}
