package br.com.ichef.visitor;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import br.com.ichef.arquitetura.util.FilterVisitor;

public class VwClienteCarteiraSaldoVisitor extends FilterVisitor {
	private Long codigoCarteira;

	@Override
	public void visitCriteria(Criteria criteria) {
		if (codigoCarteira != null) {
			criteria.add(Restrictions.ge("id", codigoCarteira ));
		}

	}

	public Long getCodigoCarteira() {
		return codigoCarteira;
	}

	public void setCodigoCarteira(Long codigoCarteira) {
		this.codigoCarteira = codigoCarteira;
	}

}
