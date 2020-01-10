package br.com.ichef.service;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;

import org.apache.regexp.recompile;

import br.com.ichef.dao.AbstractService;
import br.com.ichef.exception.AppException;
import br.com.ichef.model.Cliente;
import br.com.ichef.model.ClienteTelefone;
import br.com.ichef.util.FacesUtil;
import br.com.ichef.visitor.ClienteVisitor;

@Stateless
public class ClienteService extends AbstractService<Cliente> {
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




	@Override
	protected void validaCampos(Cliente entity) {
		// TODO Auto-generated method stub
		
	}




	@Override
	protected void validaRegras(Cliente entity) {
		// TODO Auto-generated method stub
		
	}




	@Override
	protected void validaRegrasExcluir(Cliente entity) throws AppException {
		// TODO Auto-generated method stub
		
	}

}
