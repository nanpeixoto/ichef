package br.com.ichef.service;

import java.util.List;

import javax.ejb.Stateless;

import br.com.ichef.dao.AbstractService;
import br.com.ichef.exception.AppException;
import br.com.ichef.model.TipoInsumo;

@Stateless
public class TipoInsumoService extends AbstractService<TipoInsumo> {
	private static final long serialVersionUID = 1L;

	public List<TipoInsumo> listAll(Boolean ativo) {
		TipoInsumo filter = new TipoInsumo();
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
	protected void validaCampos(TipoInsumo entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void validaRegras(TipoInsumo entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void validaRegrasExcluir(TipoInsumo entity) throws AppException {
		// TODO Auto-generated method stub
		
	}
	
	

}
