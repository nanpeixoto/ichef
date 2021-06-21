package br.com.ichef.service;

import java.util.List;

import br.com.ichef.dao.GenericDAO;
import br.com.ichef.model.Area;

public class AreaService extends GenericDAO<Area> {
	private static final long serialVersionUID = 1L;

	@Override
	public Area saveOrUpdade(Area entity) throws Exception {
		if (validaRegras(entity)) {
			return super.saveOrUpdade(entity);
		}
		return entity;

	}

	
	private boolean validaRegras(Area entity) {
		return true;
	}


	public List<Area> listAll(Boolean ativo) {
		Area filter = new Area();
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
