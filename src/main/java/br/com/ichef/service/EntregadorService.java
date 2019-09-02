package br.com.ichef.service;

import java.util.List;

import br.com.ichef.dao.GenericDAO;
import br.com.ichef.model.Entregador;

public class EntregadorService extends GenericDAO<Entregador> {
	private static final long serialVersionUID = 1L;

	@Override
	public Entregador saveOrUpdade(Entregador entity) throws Exception {
		if (validaRegras(entity)) {
			return super.saveOrUpdade(entity);
		}
		return entity;

	}

	
	private boolean validaRegras(Entregador entity) {
		return true;
	}


	public List<Entregador> listAll(Boolean ativo) {
		Entregador filter = new Entregador();
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
