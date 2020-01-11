package br.com.ichef.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;

import br.com.ichef.dao.AbstractService;
import br.com.ichef.exception.AppException;
import br.com.ichef.model.Derivacao;

@Stateless
public class DerivacaoService extends AbstractService<Derivacao> {
	private static final long serialVersionUID = 1L;

	public List<Derivacao> listAll(Boolean ativo) {
		Derivacao filter = new Derivacao();
		filter.setAtivo("S");
		try {
			if (ativo)
				return super.findByParameters(filter);
			else
				return super.listAll();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public List<Derivacao> listAll() {
		 CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		    CriteriaQuery<Derivacao> criteriaQuery = builder.createQuery(Derivacao.class);
		     
		    Root<Derivacao> veiculo = criteriaQuery.from(Derivacao.class);
		    veiculo.fetch("usuarioCadastro", JoinType.LEFT);
		    //veiculo.fetch("usuarioAlteracao", JoinType.LEFT);
		    criteriaQuery.select(veiculo);
		 
		    TypedQuery<Derivacao> query = entityManager.createQuery(criteriaQuery);
		    return query.getResultList();
	}

	@Override
	protected void validaCampos(Derivacao entity) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void validaRegras(Derivacao entity) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void validaRegrasExcluir(Derivacao entity) throws AppException {
		// TODO Auto-generated method stub

	}

}
