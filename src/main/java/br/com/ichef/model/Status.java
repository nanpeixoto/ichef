package br.com.ichef.model;

public enum Status {

	ANDAMENTO("Em Andamento"), FINALIZADO("Finalizado");

	private String descricao;

	private Status(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}

}
