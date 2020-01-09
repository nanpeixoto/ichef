package br.com.ichef.service;

import java.util.ArrayList;
import java.util.List;

import br.com.ichef.dao.AbstractService;
import br.com.ichef.exception.AppException;
import br.com.ichef.model.Empresa;
import br.com.ichef.model.Usuario;
import br.com.ichef.model.UsuarioEmpresa;

public class EmpresaService extends AbstractService<Empresa> {
	private static final long serialVersionUID = 1L;

	public List<Empresa> listAll(Boolean ativo) {
		Empresa filter = new Empresa();
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

	public List<Empresa> empresasUsuario(Usuario usuario) {
		Empresa filterEmpresa = new Empresa();
		List<Empresa> empresas = new ArrayList<Empresa>();
		filterEmpresa.setUsuarioEmpresas(usuario.getUsuarioEmpresas());
		
		for (UsuarioEmpresa usuarioEmpresa : usuario.getUsuarioEmpresas()) {
			empresas.add(usuarioEmpresa.getEmpresa());
		}
		return empresas;
	}

	@Override
	protected void validaCampos(Empresa entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void validaRegras(Empresa entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void validaRegrasExcluir(Empresa entity) throws AppException {
		// TODO Auto-generated method stub
		
	}

}
