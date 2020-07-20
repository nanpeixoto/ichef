package br.com.ichef.service;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import br.com.ichef.dao.GenericDAO;
import br.com.ichef.model.Empresa;
import br.com.ichef.model.Usuario;
import br.com.ichef.model.UsuarioEmpresa;

public class EmpresaService extends GenericDAO<Empresa> {
	private static final long serialVersionUID = 1L;
	
	@Inject
	private UsuarioEmpresaoService usuarioEmpresaService;

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
		List<Empresa> empresas = new ArrayList<Empresa>();
		// Empresa filterEmpresa = new Empresa();
		// List<Empresa> empresas = new ArrayList<Empresa>();
		// filterEmpresa.setUsuarioEmpresas(usuario.getUsuarioEmpresas());
		List<UsuarioEmpresa> usuariosEmpresa = new ArrayList<UsuarioEmpresa>();

		if (usuario.getUsuarioEmpresas() == null || usuario.getUsuarioEmpresas().size() == 0) {
			usuariosEmpresa = getEmpresasUsuario(usuario);
			usuario.setUsuarioEmpresas(usuariosEmpresa);
		} else {
			usuariosEmpresa = usuario.getUsuarioEmpresas();
		}

		for (UsuarioEmpresa usuarioEmpresa : usuariosEmpresa) {
			empresas.add(usuarioEmpresa.getEmpresa());
		}
		return empresas;
	}

	private List<UsuarioEmpresa> getEmpresasUsuario(Usuario usuario) {
		UsuarioEmpresa filter = new UsuarioEmpresa();
		filter.setUsuario(usuario);
		List<UsuarioEmpresa> usuarioEmpresa = new ArrayList<UsuarioEmpresa>();
		try {
			usuarioEmpresa = usuarioEmpresaService.findByParameters(filter);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return usuarioEmpresa;

	}

}
