package br.com.iche.dto;

public class Entregador {
	private Long id;
	private String entregador;
	private String ativo;
	private String senha;

	// Getter Methods

	public Long getId() {
		return id;
	}

	public String getAtivo() {
		return ativo;
	}

	public String getSenha() {
		return senha;
	}

	// Setter Methods

	public void setId(Long id) {
		this.id = id;
	}

	public void setAtivo(String ativo) {
		this.ativo = ativo;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public String getEntregador() {
		return entregador;
	}

	public void setEntregador(String entregador) {
		this.entregador = entregador;
	}

}