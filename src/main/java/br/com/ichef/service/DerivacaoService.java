package br.com.ichef.service;

import java.util.List;

import br.com.ichef.dao.AbstractService;
import br.com.ichef.exception.AppException;
import br.com.ichef.model.Derivacao;

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
