package br.com.ichef.service;

import java.util.List;

import br.com.ichef.dao.GenericDAO;
import br.com.ichef.model.Insumo;

public class InsumoService extends GenericDAO<Insumo> {
	private static final long serialVersionUID = 1L;

	public List<Insumo> listAll(Boolean ativo) {
		Insumo filter = new Insumo();
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
