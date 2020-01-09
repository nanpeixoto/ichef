package br.com.ichef.service;

import java.util.List;

import br.com.ichef.dao.AbstractService;
import br.com.ichef.exception.AppException;
import br.com.ichef.model.Area;

public class AreaService extends AbstractService<Area> {
	private static final long serialVersionUID = 1L;

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

	@Override
	protected void validaCampos(Area entity) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void validaRegras(Area entity) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void validaRegrasExcluir(Area entity) throws AppException {
		// TODO Auto-generated method stub

	}

}
