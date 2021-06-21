package br.com.ichef.service;

import java.util.List;

import br.com.ichef.dao.GenericDAO;
import br.com.ichef.model.TipoPrato;

public class TipoPratoService extends GenericDAO<TipoPrato> {
	private static final long serialVersionUID = 1L;

	@Override
	public TipoPrato saveOrUpdade(TipoPrato entity) throws Exception {
		if (validaRegras(entity)) {
			return super.saveOrUpdade(entity);
		}
		return entity;

	}

	
	private boolean validaRegras(TipoPrato entity) {
		return true;
	}


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

}
