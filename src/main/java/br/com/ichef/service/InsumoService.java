package br.com.ichef.service;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;

import org.hibernate.Hibernate;

import br.com.ichef.arquitetura.util.FilterVisitor;
import br.com.ichef.dao.AbstractService;
import br.com.ichef.exception.AppException;
import br.com.ichef.model.Insumo;

@Stateless
public class InsumoService extends AbstractService<Insumo> {
	private static final long serialVersionUID = 1L;

	@Override
	public List<Insumo> findByParameters(Insumo object, FilterVisitor visitor) throws Exception {
		List<Insumo> insumo = super.findByParameters(object, visitor);
		for (Insumo insumo2 : insumo) {
			Hibernate.initialize(insumo2.getPrecos());
			Hibernate.initialize(insumo2.getUltimoPreco());
		}
		return insumo;
	}

	@Override
	public List<Insumo> findByParameters(Insumo object) throws Exception {

		List<Insumo> insumo = super.findByParameters(object);
		for (Insumo insumo2 : insumo) {
			Hibernate.initialize(insumo2.getPrecos());
			Hibernate.initialize(insumo2.getUltimoPreco());
		}
		return insumo;
	}

	public List<Insumo> listAll(Boolean ativo) {
		Insumo filter = new Insumo();
		filter.setAtivo("S");
		List<Insumo> insumo = new ArrayList<Insumo>();
		try {
			if (ativo)
				insumo = super.findByParameters(filter);
			else
				insumo = super.listAll();

			for (Insumo insumo2 : insumo) {
				Hibernate.initialize(insumo2.getPrecos());
				Hibernate.initialize(insumo2.getUltimoPreco());
			}
			return insumo;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	protected void validaCampos(Insumo entity) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void validaRegras(Insumo entity) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void validaRegrasExcluir(Insumo entity) throws AppException {
		// TODO Auto-generated method stub

	}

}
