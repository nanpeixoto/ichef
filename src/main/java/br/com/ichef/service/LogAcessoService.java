package br.com.ichef.service;

import javax.ejb.Stateless;

import br.com.ichef.dao.AbstractService;
import br.com.ichef.exception.AppException;
import br.com.ichef.model.LogAcesso;


@Stateless
public class LogAcessoService extends AbstractService<LogAcesso> {
	private static final long serialVersionUID = 1L;

	@Override
	protected void validaCampos(LogAcesso entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void validaRegras(LogAcesso entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void validaRegrasExcluir(LogAcesso entity) throws AppException {
		// TODO Auto-generated method stub
		
	}

}
