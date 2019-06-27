package br.com.ichef.visitor;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import br.com.ichef.arquitetura.util.FilterVisitor;

public class ClienteVisitor extends FilterVisitor {
	private Long idNotInt;

	@Override
	public void visitCriteria(Criteria criteria) {
		if (getIdNotInt() != null) {
			criteria.add(Restrictions.ne("id", getIdNotInt()));
		}

	}

	public Long getIdNotInt() {
		return idNotInt;
	}

	public void setIdNotInt(Long idNotInt) {
		this.idNotInt = idNotInt;
	}

}
