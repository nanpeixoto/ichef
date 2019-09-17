package br.com.ichef.service;

import java.util.List;

import br.com.ichef.dao.GenericDAO;
import br.com.ichef.model.FichaTecnicaPreparo;

public class FichaTecnicaPreparoService extends GenericDAO<FichaTecnicaPreparo> {
	private static final long serialVersionUID = 1L;

	@Override
	public FichaTecnicaPreparo saveOrUpdade(FichaTecnicaPreparo entity) throws Exception {
		if (validaRegras(entity)) {
			return super.saveOrUpdade(entity);
		}
		return entity;

	}

	
	private boolean validaRegras(FichaTecnicaPreparo entity) {
		return true;
	}


	public List<FichaTecnicaPreparo> listAll(Boolean ativo) {
		FichaTecnicaPreparo filter = new FichaTecnicaPreparo();
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
