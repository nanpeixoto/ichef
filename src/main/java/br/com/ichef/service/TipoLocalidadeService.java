package br.com.ichef.service;

import java.util.List;

import javax.ejb.Stateless;

import br.com.ichef.dao.AbstractService;
import br.com.ichef.exception.AppException;
import br.com.ichef.model.TipoLocalidade;

@Stateless
public class TipoLocalidadeService extends AbstractService<TipoLocalidade> {
	private static final long serialVersionUID = 1L;

	public List<TipoLocalidade> listAll(Boolean ativo) {
		TipoLocalidade filter = new TipoLocalidade();
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
	protected void validaCampos(TipoLocalidade entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void validaRegras(TipoLocalidade entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void validaRegrasExcluir(TipoLocalidade entity) throws AppException {
		// TODO Auto-generated method stub
		
	}

}
