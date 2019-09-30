package br.com.ichef.controler;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.ichef.arquitetura.BaseEntity;
import br.com.ichef.arquitetura.controller.BaseController;
import br.com.ichef.model.Insumo;
import br.com.ichef.model.TipoInsumo;
import br.com.ichef.model.TipoPrato;
import br.com.ichef.model.TipoPratoInsumo;
import br.com.ichef.model.TipoPratoPreco;
import br.com.ichef.service.InsumoService;
import br.com.ichef.service.TipoPratoService;
import br.com.ichef.util.FacesUtil;

@Named
@ViewScoped
public class TipoPratoController extends BaseController {

	private static final long serialVersionUID = 1L;

	@Inject	private TipoPratoService service;
	@Inject private InsumoService insumoService;

	private TipoPrato entity;

	private Long id;

	private List<TipoPrato> lista = new ArrayList<TipoPrato>();
	private List<TipoPrato> listaFiltro = new ArrayList<TipoPrato>();

	private List<TipoPrato> listaSelecionadas = new ArrayList<TipoPrato>();
	
	private List<Insumo> insumos = new ArrayList<Insumo>();
	private Insumo insumo = new Insumo();
	private Long quantidade ;

	private Double preco;
	private Date dataVigencia;

	public void inicializar() {
		if (id != null) {
			setEntity(service.getById(id));
		}
	}
	
	@PostConstruct
	public void init() {
		if (id != null) {
			setEntity(service.getById(id));
		} else {
			setEntity(new TipoPrato());
			getEntity().setAtivo("S");
		}
		dataVigencia  = new Date();
		lista = service.listAll();
		setQuantidade(1l);
		obterListas();
	}

	private void obterListas() {
		try {
			Insumo filter = new Insumo();	
			TipoInsumo tipoInsumo = new TipoInsumo();
			tipoInsumo.setId(TipoInsumo.COD_INSUMO_MATERIAL);
			filter.setTipoInsumo(tipoInsumo);
			insumos = insumoService.findByParameters(filter);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public void adicionarInsumo() {
		boolean existe = false;

		if (getInsumo() == null) {
			facesMessager.error(getRequiredMessage("Insumo"));
			return;
		}

		if (getQuantidade() == null) {
			facesMessager.error(getRequiredMessage("Qtd."));
			return;
		}

		

		if (getEntity().getInsumos() != null) {
			for (TipoPratoInsumo tipoPratoInsumo : getEntity().getInsumos()) {
				if (insumo.getId().equals(tipoPratoInsumo.getInsumo().getId()))
					existe = true;
			}
		}

		if (!existe) {
			TipoPratoInsumo tipoPratoInsumo = new TipoPratoInsumo();
			tipoPratoInsumo.setAtivo(true);;
			tipoPratoInsumo.setAtivo("S");
			tipoPratoInsumo.setQuantidade(getQuantidade());
			tipoPratoInsumo.setCustoTotal((new BigDecimal(tipoPratoInsumo.getQuantidade().toString()).multiply(new BigDecimal(getInsumo().getValor()))).setScale(2, RoundingMode.HALF_EVEN));
			tipoPratoInsumo.setTipoPrato(getEntity());
			tipoPratoInsumo.setInsumo(insumo);
			

			if (getEntity().getInsumos() == null) {
				getEntity().setInsumos(new ArrayList<TipoPratoInsumo>());
			}
			getEntity().getInsumos().add(tipoPratoInsumo);

			//service.calcularPercos(entity, configuracao);
			setInsumo(null);
			setQuantidade(1l);

		} else {
			facesMessager.error("Insumo já cadastrado");
		}

	}

	public void excluirInsumoSelecionado(TipoPratoInsumo insumo) {
		List<TipoPratoInsumo> temp = new ArrayList<TipoPratoInsumo>();
		temp.addAll(entity.getInsumos());
		for (TipoPratoInsumo item : entity.getInsumos()) {
			if (insumo.getInsumo().getId().equals(item.getInsumo().getId()))
				temp.remove(item);
		}
		entity.getInsumos().clear();
		entity.getInsumos().addAll(temp);
		//service.calcularPercos(entity, configuracao);
		updateComponentes("Stable");
		FacesUtil.addInfoMessage("Itens excluídos com sucesso");
	}
	

	public void adicionarPreco() {
		TipoPratoPreco tipoPratoPrecoObj = new TipoPratoPreco();
		tipoPratoPrecoObj.setDataCadastro(new Date());
		tipoPratoPrecoObj.setDataVigencia(getDataVigencia());
		tipoPratoPrecoObj.setPreco( getPreco() );
		tipoPratoPrecoObj.setTipoPrato(getEntity());
		tipoPratoPrecoObj.setUsuarioCadastro(getUserLogado());
		if (getEntity().getPrecos() == null)
			getEntity().setPrecos(new ArrayList<TipoPratoPreco>());
		getEntity().getPrecos().add(tipoPratoPrecoObj);
		
		updateComponentes("dttel");

		setPreco(null);
		setDataVigencia(null);

	}

	public void excluirSelecionados() {
		for (BaseEntity entity : listaSelecionadas) {
			service.excluir(entity);
			lista.remove(entity);
		}
		FacesUtil.addInfoMessage("Itens excluídos com sucesso");
	}

	public String Salvar() throws Exception {

		if (entity.isEdicao()) {
			entity.setUsuarioAlteracao(getUserLogado());
			entity.setDataAlteracao(new Date());
		} else {
			entity.setUsuarioCadastro(getUserLogado());
			entity.setDataCadastro(new Date());
		}
		service.saveOrUpdade(entity);
		return "lista-tipo-prato.xhtml?faces-redirect=true";

	}

	public String excluir() {
		service.excluir(entity);
		return "lista-tipo-prato.xhtml?faces-redirect=true";
	}

	public TipoPratoService getService() {
		return service;
	}

	public void setService(TipoPratoService service) {
		this.service = service;
	}

	public TipoPrato getEntity() {
		return entity;
	}

	public void setEntity(TipoPrato entity) {
		this.entity = entity;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<TipoPrato> getLista() {
		return lista;
	}

	public void setLista(List<TipoPrato> lista) {
		this.lista = lista;
	}

	public List<TipoPrato> getListaFiltro() {
		return listaFiltro;
	}

	public void setListaFiltro(List<TipoPrato> listaFiltro) {
		this.listaFiltro = listaFiltro;
	}

	public List<TipoPrato> getListaSelecionadas() {
		return listaSelecionadas;
	}

	public void setListaSelecionadas(List<TipoPrato> listaSelecionadas) {
		this.listaSelecionadas = listaSelecionadas;
	}

	public Date getDataVigencia() {
		return dataVigencia;
	}

	public void setDataVigencia(Date dataVigencia) {
		this.dataVigencia = dataVigencia;
	}

	public Double getPreco() {
		return preco;
	}

	public void setPreco(Double preco) {
		this.preco = preco;
	}

	public List<Insumo> getInsumos() {
		return insumos;
	}

	public void setInsumos(List<Insumo> insumos) {
		this.insumos = insumos;
	}

	public Insumo getInsumo() {
		return insumo;
	}

	public void setInsumo(Insumo insumo) {
		this.insumo = insumo;
	}

	public Long getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(Long quantidade) {
		this.quantidade = quantidade;
	}
	
	

}
