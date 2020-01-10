package br.com.ichef.service;

import javax.ejb.Stateless;

import br.com.ichef.dao.AbstractService;
import br.com.ichef.exception.AppException;
import br.com.ichef.model.VwClienteSaldo;

@Stateless
public class ClienteSaldoService extends AbstractService<VwClienteSaldo> {
	private static final long serialVersionUID = 1L;

	@Override
	protected void validaCampos(VwClienteSaldo entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void validaRegras(VwClienteSaldo entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void validaRegrasExcluir(VwClienteSaldo entity) throws AppException {
		// TODO Auto-generated method stub
		
	}

}
