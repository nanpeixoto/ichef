package br.com.ichef.visitor;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import br.com.ichef.arquitetura.util.FilterVisitor;

public class VwClienteSaldoVisitor extends FilterVisitor {

	private Long codigoEmpresa;

	@Override
	public void visitCriteria(Criteria criteria) {
		if(codigoEmpresa!=null) {
			criteria.add(Restrictions.eq("codigoEmpresa", codigoEmpresa));
		}
	}

	public Long getCodigoEmpresa() {
		return codigoEmpresa;
	}

	public void setCodigoEmpresa(Long codigoEmpresa) {
		this.codigoEmpresa = codigoEmpresa;
	}
 

}
