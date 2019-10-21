package br.com.ichef.visitor;

import java.util.Date;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import br.com.ichef.arquitetura.util.FilterVisitor;
import br.com.ichef.util.Util;

public class ClienteCarteiraVisitor extends FilterVisitor {

	private Date dataSemHora;
	

	@Override
	public void visitCriteria(Criteria criteria) {
		if(dataSemHora!=null) {
			criteria.add( Restrictions.sqlRestriction(" date_format( data, '%d/%m/%Y' ) ='"+Util.dateToString( getDataSemHora() )+"'" ) );
		}
	}


	public Date getDataSemHora() {
		return dataSemHora;
	}


	public void setDataSemHora(Date dataSemHora) {
		this.dataSemHora = dataSemHora;
	}
	
	
	
	

}
