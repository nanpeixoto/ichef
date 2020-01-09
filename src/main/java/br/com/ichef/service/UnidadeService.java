package br.com.ichef.service;

import java.util.List;

import br.com.ichef.dao.AbstractService;
import br.com.ichef.exception.AppException;
import br.com.ichef.model.Unidade;

public class UnidadeService extends AbstractService<Unidade> {
	private static final long serialVersionUID = 1L;

	public List<Unidade> listAll(Boolean ativo) {
		Unidade filter = new Unidade();
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
	protected void validaCampos(Unidade entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void validaRegras(Unidade entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void validaRegrasExcluir(Unidade entity) throws AppException {
		// TODO Auto-generated method stub
		
	}
	
	

}
