package br.com.ichef.service;

import java.util.List;

import br.com.ichef.dao.GenericDAO;
import br.com.ichef.model.Empresa;

public class EmpresaService extends GenericDAO<Empresa> {
	private static final long serialVersionUID = 1L;

	public List<Empresa> listAll(Boolean ativo) {
		Empresa filter = new Empresa();
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
