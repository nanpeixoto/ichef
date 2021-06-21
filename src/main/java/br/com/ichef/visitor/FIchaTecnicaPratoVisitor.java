package br.com.ichef.visitor;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

import br.com.ichef.arquitetura.util.FilterVisitor;

public class FIchaTecnicaPratoVisitor extends FilterVisitor {

	
	private List<Long> idPreparacoes;
	private String descricao;

	@Override
	public void visitCriteria(Criteria criteria) {

		if (descricao != null) {
			criteria.add(Restrictions.eq("descricao", descricao));

		}
		
		if (idPreparacoes != null) {
			criteria.createCriteria( "fichaTecnicaPratoPreparos", "pe", JoinType.INNER_JOIN );
			criteria.add(Restrictions.in("pe.fichaTecnicaPreparo.id", idPreparacoes));
			

		}

	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public List<Long> getIdPreparacoes() {
		return idPreparacoes;
	}

	public void setIdPreparacoes(List<Long> idPreparacoes) {
		this.idPreparacoes = idPreparacoes;
	}
	
	

}
