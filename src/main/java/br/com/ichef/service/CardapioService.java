package br.com.ichef.service;

import java.util.List;

import br.com.ichef.dao.GenericDAO;
import br.com.ichef.model.Cardapio;

public class CardapioService extends GenericDAO<Cardapio> {
	private static final long serialVersionUID = 1L;


	public List<Cardapio> listAll(Boolean ativo) {
		Cardapio filter = new Cardapio();
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
