package br.com.ichef.controler;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.ichef.arquitetura.controller.BaseController;
import br.com.ichef.dto.PainelDto;
import br.com.ichef.model.PainelEmpresa1;
import br.com.ichef.model.PainelEmpresa2;
import br.com.ichef.service.PainelEmpresa1Service;
import br.com.ichef.service.PainelEmpresa2Service;

@Named
@ViewScoped
public class PainelController extends BaseController {

	private static final long serialVersionUID = 1L;

	@Inject
	private PainelEmpresa1Service painelEmpresa1Service;

	@Inject
	private PainelEmpresa2Service painelEmpresa2Service;

	private PainelEmpresa1 painelEmpresa1;

	private PainelEmpresa2 painelEmpresa2;

	private PainelDto painel;

	private String codigoEmpresa;

	@PostConstruct
	public void init() {

	}

	public String getObterDadosEmpresa2() {
		painelEmpresa2 = ((List<PainelEmpresa2>) painelEmpresa2Service.listAll()).get(0);
		return (String) painelEmpresa2.getId();
	}

	public String getObterDadosEmpresa1() {
		painelEmpresa1 = ((List<PainelEmpresa1>) painelEmpresa1Service.listAll()).get(0);
		return (String) painelEmpresa1.getId();
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public PainelDto getPainel() {
		return painel;
	}

	public void setPainel(PainelDto painel) {
		this.painel = painel;
	}

	public String getCodigoEmpresa() {
		return codigoEmpresa;
	}

	public void setCodigoEmpresa(String codigoEmpresa) {
		this.codigoEmpresa = codigoEmpresa;
	}

}
