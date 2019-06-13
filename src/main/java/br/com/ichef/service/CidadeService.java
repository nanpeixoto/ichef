package br.com.ichef.service;

import java.util.List;

import br.com.ichef.dao.GenericDAO;
import br.com.ichef.model.Cidade;

public class CidadeService extends GenericDAO<Cidade> {
	private static final long serialVersionUID = 1L;

	@Override
	public List<Cidade> listAll() {
		Cidade cidade = new Cidade();
		cidade.setAtivo("S");
		try {
			return super.findByParameters(cidade);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
