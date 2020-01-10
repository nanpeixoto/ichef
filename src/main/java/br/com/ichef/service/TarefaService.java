package br.com.ichef.service;

import javax.ejb.Stateless;

import br.com.ichef.dao.AbstractService;
import br.com.ichef.exception.AppException;
import br.com.ichef.model.Tarefa;

@Stateless
public class TarefaService extends AbstractService<Tarefa> {
	private static final long serialVersionUID = 1L;

	@Override
	protected void validaCampos(Tarefa entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void validaRegras(Tarefa entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void validaRegrasExcluir(Tarefa entity) throws AppException {
		// TODO Auto-generated method stub
		
	}

	

}
