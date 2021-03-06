package br.com.ichef.service;

import java.util.List;

import br.com.ichef.arquitetura.util.FilterVisitor;
import br.com.ichef.dao.GenericDAO;
import br.com.ichef.model.Insumo;


public class InsumoService extends GenericDAO<Insumo> {
	private static final long serialVersionUID = 1L;
	
	@Override
	public List<Insumo> findByParameters(Insumo object, FilterVisitor visitor) throws Exception {

		return mount(super.findByParameters(object, visitor));
	}
	
	@Override
	public List<Insumo> findByParameters(Insumo object) throws Exception {

		return mount(super.findByParameters(object));
	}

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
