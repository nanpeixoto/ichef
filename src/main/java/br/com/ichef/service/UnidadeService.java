package br.com.ichef.service;

import java.util.List;

import br.com.ichef.dao.GenericDAO;
import br.com.ichef.model.Unidade;

public class UnidadeService extends GenericDAO<Unidade> {
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
	
	

}
