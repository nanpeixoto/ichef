package br.com.ichef.service;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityTransaction;
import javax.persistence.Query;

import org.apache.regexp.recompile;

import br.com.ichef.arquitetura.service.EntityManagerProducer;
import br.com.ichef.dao.GenericDAO;
import br.com.ichef.model.Cliente;
import br.com.ichef.model.ClienteTelefone;
import br.com.ichef.util.FacesUtil;
import br.com.ichef.visitor.ClienteVisitor;

public class ClienteService extends GenericDAO<Cliente> {
	private static final long serialVersionUID = 1L;

	@Override
	public Cliente saveOrUpdade(Cliente entity) throws Exception {
		if (validaRegras(entity)) {
			return super.saveOrUpdade(entity);
		}
		return entity;

	}

	private boolean validaRegras(Cliente entity) {
		// TODO Auto-generated method stub
		return true;
	}

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

	public String atualizarStatusBloqueio(String statusBloqueio, Long codigoCliente) {
		EntityTransaction tx = null;
		try {

			StringBuilder hql = null;
			int result = -1;

			hql = new StringBuilder();

			hql.append("UPDATE Cliente SET bloqueado = '"+statusBloqueio+"'");
			 hql.append(" where  id = "+codigoCliente);
			

			if (hql != null) {

				if (!getManager().isOpen()) {
					EntityManagerProducer producer = new EntityManagerProducer();
					setManager(producer.createEntityManager());
				} else {
					getManager().clear();
				}

				tx = getManager().getTransaction();
				tx.begin();

				Query query = getManager().createQuery(hql.toString());
				result = query.executeUpdate();
				tx.commit();

			}
			if (result == 0) {
				return "Operação Não Realizada. Contact o ADM do sistema";
			}

			return null;
		} catch (Exception e) {

			e.printStackTrace();
			return e.getMessage();
		}  
		
	}

}
