package br.com.ichef.visitor;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import br.com.ichef.arquitetura.util.FilterVisitor;
import br.com.ichef.util.Util;

public class ClienteVisitor extends FilterVisitor {
	private Long idNotInt;
	
	private String telefone;

	@Override
	public void visitCriteria(Criteria criteria) {
		if (getIdNotInt() != null) {
			criteria.add(Restrictions.ne("id", getIdNotInt()));
		}
		
		if(getTelefone()!=null) {
				criteria.add(Restrictions.sqlRestriction(
						"  replace(replace(replace(replace(\r\n" + 
						"replace(nr_telefone ,' ', '')\r\n" + 
						", '(', ''), ')', ''), '+', ''), '-', '') ='" + getTelefone() + "'"));
		}

	}

	public Long getIdNotInt() {
		return idNotInt;
	}

	public void setIdNotInt(Long idNotInt) {
		this.idNotInt = idNotInt;
	}

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}
	
	

}
