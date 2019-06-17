package br.com.ichef.service;

import java.util.List;

import br.com.ichef.dao.GenericDAO;
import br.com.ichef.model.TipoLocalidade;

public class TipoLocalidadeService extends GenericDAO<TipoLocalidade> {
	private static final long serialVersionUID = 1L;

	public List<TipoLocalidade> listAll(Boolean ativo) {
		TipoLocalidade filter = new TipoLocalidade();
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
