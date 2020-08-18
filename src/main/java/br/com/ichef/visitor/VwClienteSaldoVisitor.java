package br.com.ichef.visitor;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import br.com.ichef.arquitetura.util.FilterVisitor;

public class VwClienteSaldoVisitor extends FilterVisitor {

	private Long codigoEmpresa;

	private boolean ativo;
	private boolean inativo;
	private boolean bloqueado;
	private boolean carteira30Dias;
	private boolean carteira120Dias;
	private boolean carteiraEmDias;
	private boolean todos;
	private boolean carteiraTodos;

	@Override
	public void visitCriteria(Criteria criteria) {
		if (codigoEmpresa != null) {
			criteria.add(Restrictions.eq("codigoEmpresa", codigoEmpresa));
		}

		if (!todos) {
			if (inativo && ativo && bloqueado) {
				criteria.add(
						Restrictions.or(Restrictions.or(Restrictions.eq("ativo", "S"), Restrictions.eq("ativo", "N")),
								Restrictions.eq("bloqueado", "S")));
			} else {
				if (inativo && ativo) {
					criteria.add(Restrictions.or(Restrictions.eq("ativo", "S"), Restrictions.eq("ativo", "N")));
				} else {
					if (inativo)
						criteria.add(Restrictions.eq("ativo", "N"));
					if (ativo)
						criteria.add(Restrictions.eq("ativo", "S"));
					if (bloqueado)
						criteria.add(Restrictions.eq("bloqueado", "'S'"));
				}
			}
		}

		if (carteiraTodos) {

			if (carteiraEmDias) {
				criteria.add(Restrictions.lt("diasDevedor", 30l));

			} else {

				if (carteira30Dias) {
					criteria.add(Restrictions.ge("diasDevedor", 30l));
				}

				if (carteira120Dias) {
					criteria.add(Restrictions.ge("diasDevedor", 120l));
				}
			}
		}

	}

	public Long getCodigoEmpresa() {
		return codigoEmpresa;
	}

	public void setCodigoEmpresa(Long codigoEmpresa) {
		this.codigoEmpresa = codigoEmpresa;
	}

	public boolean isAtivo() {
		return ativo;
	}

	public void setAtivo(boolean ativo) {
		this.ativo = ativo;
	}

	public boolean isInativo() {
		return inativo;
	}

	public void setInativo(boolean inativo) {
		this.inativo = inativo;
	}

	public boolean isCarteira30Dias() {
		return carteira30Dias;
	}

	public void setCarteira30Dias(boolean carteira30Dias) {
		this.carteira30Dias = carteira30Dias;
	}

	public boolean isCarteira120Dias() {
		return carteira120Dias;
	}

	public void setCarteira120Dias(boolean carteira120Dias) {
		this.carteira120Dias = carteira120Dias;
	}

	public boolean isCarteiraEmDias() {
		return carteiraEmDias;
	}

	public void setCarteiraEmDias(boolean carteiraEmDias) {
		this.carteiraEmDias = carteiraEmDias;
	}

	public boolean isBloqueado() {
		return bloqueado;
	}

	public void setBloqueado(boolean bloqueado) {
		this.bloqueado = bloqueado;
	}

	public boolean isTodos() {
		return todos;
	}

	public void setTodos(boolean todos) {
		this.todos = todos;
	}

	public boolean isCarteiraTodos() {
		return carteiraTodos;
	}

	public void setCarteiraTodos(boolean carteiraTodos) {
		this.carteiraTodos = carteiraTodos;
	}

}
