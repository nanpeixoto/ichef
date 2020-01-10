package br.com.ichef.service;

import java.util.List;

import javax.ejb.Stateless;

import br.com.ichef.dao.AbstractService;
import br.com.ichef.exception.AppException;
import br.com.ichef.model.Localidade;
import br.com.ichef.model.TipoLocalidade;
@Stateless
public class LocalidadeService extends AbstractService<Localidade> {
	private static final long serialVersionUID = 1L;
	
	public List<Localidade> listAll(Boolean ativo) {
		Localidade filter = new Localidade();
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
	public List<Localidade> listAllResidencial() {
		Localidade filter = new Localidade();
		filter.setAtivo("S");
		TipoLocalidade tipo = new TipoLocalidade();
		tipo.setId(2l);
		filter.setTipoLocalidade(tipo);
		
		try {
			return super.findByParameters(filter);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	@Override
	protected void validaCampos(Localidade entity) {
		// TODO Auto-generated method stub
		
	}
	@Override
	protected void validaRegras(Localidade entity) {
		// TODO Auto-generated method stub
		
	}
	@Override
	protected void validaRegrasExcluir(Localidade entity) throws AppException {
		// TODO Auto-generated method stub
		
	}
	

}
