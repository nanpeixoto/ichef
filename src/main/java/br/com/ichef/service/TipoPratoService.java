package br.com.ichef.service;

import java.util.List;

import br.com.ichef.dao.AbstractService;
import br.com.ichef.exception.AppException;
import br.com.ichef.model.TipoPrato;

public class TipoPratoService extends AbstractService<TipoPrato> {
	private static final long serialVersionUID = 1L;




	public List<TipoPrato> listAll(Boolean ativo) {
		TipoPrato filter = new TipoPrato();
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
	protected void validaCampos(TipoPrato entity) {
		// TODO Auto-generated method stub
		
	}




	@Override
	protected void validaRegras(TipoPrato entity) {
		// TODO Auto-generated method stub
		
	}




	@Override
	protected void validaRegrasExcluir(TipoPrato entity) throws AppException {
		// TODO Auto-generated method stub
		
	}

}
