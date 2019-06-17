package br.com.ichef.service;

import java.util.List;

import br.com.ichef.dao.GenericDAO;
import br.com.ichef.model.Cliente;

public class ClienteService extends GenericDAO<Cliente> {
	private static final long serialVersionUID = 1L;

	public List<Cliente> listAll(Boolean ativo) {
		Cliente filter = new Cliente();
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
