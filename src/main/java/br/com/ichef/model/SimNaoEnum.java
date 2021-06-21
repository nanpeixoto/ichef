package br.com.ichef.model;

public enum SimNaoEnum {

	S("Sim"), N("N�o");

	private String descricao;

	private SimNaoEnum(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}

}
