package br.com.ichef.service;

import java.util.List;

import br.com.ichef.dao.GenericDAO;
import br.com.ichef.model.TipoLocalidade;

public class TipoLocalidadeService extends GenericDAO<TipoLocalidade> {
	private static final long serialVersionUID = 1L;

	
	@Override
	public List<TipoLocalidade> listAll() {
		TipoLocalidade filter = new TipoLocalidade();
		filter.setAtivo("S");
		try {
			return super.findByParameters(filter);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}




	

}
