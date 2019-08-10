package br.com.ichef.service;

import java.util.List;

import br.com.ichef.dao.GenericDAO;
import br.com.ichef.model.Derivacao;

public class DerivacaoService extends GenericDAO<Derivacao> {
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
	
	

}
