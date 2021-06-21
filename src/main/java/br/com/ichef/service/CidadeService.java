package br.com.ichef.service;

import java.util.List;

import br.com.ichef.dao.GenericDAO;
import br.com.ichef.model.Cidade;

public class CidadeService extends GenericDAO<Cidade> {
	private static final long serialVersionUID = 1L;

	public List<Cidade> listAll(Boolean ativo) {
		Cidade cidade = new Cidade();
		cidade.setAtivo("S");
		try {
			if (ativo)
				return super.findByParameters(cidade);
			else
				return super.listAll();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	

}
