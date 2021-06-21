package br.com.ichef.visitor;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import br.com.ichef.arquitetura.util.FilterVisitor;

public class ClienteVisitor extends FilterVisitor {
	private Long idNotInt;

	private String telefone;

	private String likeNomeTelefone;


	@Override
	public void visitCriteria(Criteria criteria) {
		if (getIdNotInt() != null) {
			criteria.add(Restrictions.ne("id", getIdNotInt()));
		}

		if (getTelefone() != null) {
			criteria.add(Restrictions
					.sqlRestriction("  replace(replace(replace(replace(\r\n" + "replace(nr_telefone ,' ', '')\r\n"
							+ ", '(', ''), ')', ''), '+', ''), '-', '') ='" + getTelefone() + "'"));
		}

		if (  getLikeNomeTelefone() != null) {
			criteria.add(Restrictions.or(
					Restrictions.sqlRestriction(
							"  replace(replace(replace(replace(\r\n" + "replace(ds_telefone ,' ', '')\r\n"
									+ ", '(', ''), ')', ''), '+', ''), '-', '')  like '%" + getLikeNomeTelefone() + "%'"),
					Restrictions.sqlRestriction("   upper(nm_cliente) like upper('%" + getLikeNomeTelefone() + "%')")));
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


	public String getLikeNomeTelefone() {
		return likeNomeTelefone;
	}


	public void setLikeNomeTelefone(String likeNomeTelefone) {
		this.likeNomeTelefone = likeNomeTelefone;
	}

	 

}
