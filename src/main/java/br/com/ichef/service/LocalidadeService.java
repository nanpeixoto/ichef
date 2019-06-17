package br.com.ichef.service;

import java.util.List;

import br.com.ichef.dao.GenericDAO;
import br.com.ichef.model.Localidade;
import br.com.ichef.model.TipoLocalidade;

public class LocalidadeService extends GenericDAO<Localidade> {
	private static final long serialVersionUID = 1L;
	
	public List<Localidade> listAll(Boolean ativo) {
		Localidade filter = new Localidade();
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
	public List<Localidade> listAllResidencial() {
		Localidade filter = new Localidade();
		filter.setAtivo("S");
		TipoLocalidade tipo = new TipoLocalidade();
		tipo.setId(2l);
		filter.setTipoLocalidade(tipo);
		
		try {
			return super.findByParameters(filter);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	

}
