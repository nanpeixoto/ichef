package br.com.ichef.visitor;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import br.com.ichef.arquitetura.util.FilterVisitor;

public class FIchaTecnicaPratoVisitor extends FilterVisitor {

	private String descricao;

	@Override
	public void visitCriteria(Criteria criteria) {

		if (descricao != null) {
			criteria.add(Restrictions.eq("descricao", descricao));

		}

	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

}
