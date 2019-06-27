package br.com.ichef.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.regexp.recompile;

import br.com.ichef.dao.GenericDAO;
import br.com.ichef.model.Cliente;
import br.com.ichef.model.ClienteTelefone;
import br.com.ichef.util.FacesUtil;
import br.com.ichef.visitor.ClienteVisitor;

public class ClienteService extends GenericDAO<Cliente> {
	private static final long serialVersionUID = 1L;

	@Override
	public Cliente saveOrUpdade(Cliente entity) throws Exception {
		if (validaRegras(entity)) {
			return super.saveOrUpdade(entity);
		}
		return entity;

	}

	
	private boolean validaRegras(Cliente entity) {
		// TODO Auto-generated method stub
		return true;
	}


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
