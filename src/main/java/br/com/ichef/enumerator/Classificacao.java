package br.com.ichef.enumerator;

public enum Classificacao {

	A("A", "Acompanhamento"), P("P", "Prato Principal");

	private String id;
	private String descricao;

	private Classificacao(String id, String descricao) {
		this.id = id;
		this.descricao = descricao;
	}

	public static Classificacao getEnumByValor(String valor) {
		for (Classificacao status : Classificacao.values()) {
			if (status.getDescricao().equals(valor))
				return status;
		}
		return null;
	}

	public static Classificacao getEnumById(Long id) {
		for (Classificacao status : Classificacao.values()) {
			if (status.getId().equals(id))
				return status;
		}
		return null;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String valor) {
		this.descricao = valor;
	}

}
