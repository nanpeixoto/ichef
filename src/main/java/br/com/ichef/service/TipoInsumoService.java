package br.com.ichef.service;

import java.util.List;

import br.com.ichef.dao.GenericDAO;
import br.com.ichef.model.TipoInsumo;

public class TipoInsumoService extends GenericDAO<TipoInsumo> {
	private static final long serialVersionUID = 1L;

	public List<TipoInsumo> listAll(Boolean ativo) {
		TipoInsumo filter = new TipoInsumo();
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
