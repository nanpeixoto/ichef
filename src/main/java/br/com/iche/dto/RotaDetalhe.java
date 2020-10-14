package br.com.iche.dto;

import br.com.ichef.util.Util;

public class RotaDetalhe {

	private Integer codigoPedido;

	private Integer codigoLocalidade;
	private Integer codigoEntregador;
	private Integer codigoCliente;

	private String cliente;

	private String texto;

	private String localidade;

	private String endereco;

	private String observacaoEntrega;

	private String telefone;

	private String entregue;

	public Integer getCodigoPedido() {
		return codigoPedido;
	}

	public void setCodigoPedido(Integer codigoPedido) {
		this.codigoPedido = codigoPedido;
	}

	public String getTexto() {
		return texto;
	}

	public void setTexto(String texto) {
		this.texto = texto;
	}

	public String getCliente() {
		return Util.removeAcentos(cliente);
	}

	public void setCliente(String cliente) {
		this.cliente = cliente;
	}

	public String getLocalidade() {
		return localidade;
	}

	public void setLocalidade(String localidade) {
		this.localidade = localidade;
	}

	public String getEndereco() {
		return Util.removeAcentos(endereco);
	}

	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}

	public String getObservacaoEntrega() {
		return observacaoEntrega;
	}

	public void setObservacaoEntrega(String observacaoEntrega) {
		this.observacaoEntrega = observacaoEntrega;
	}

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	public Integer getCodigoLocalidade() {
		return codigoLocalidade;
	}

	public void setCodigoLocalidade(Integer codigoLocalidade) {
		this.codigoLocalidade = codigoLocalidade;
	}

	public Integer getCodigoEntregador() {
		return codigoEntregador;
	}

	public void setCodigoEntregador(Integer codigoEntregador) {
		this.codigoEntregador = codigoEntregador;
	}

	public Integer getCodigoCliente() {
		return codigoCliente;
	}

	public void setCodigoCliente(Integer codigoCliente) {
		this.codigoCliente = codigoCliente;
	}

 

	public Boolean getLocalidadeEntregue() {
		if (getEntregue() == null || getEntregue().equalsIgnoreCase("N"))
			return false;
		if (getEntregue().equalsIgnoreCase("S"))
			return true;
		return false;
	}

	public String getEntregue() {
		return entregue;
	}

	public void setEntregue(String entregue) {
		this.entregue = entregue;
	}
	
	

}
