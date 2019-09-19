package br.com.ichef.enumerator;

public enum ClassificacaoEnum {

	A("A", "Acompanhamento"), P("P", "Prato Principal");

	private String id;
	private String descricao;

	private ClassificacaoEnum(String id, String descricao) {
		this.id = id;
		this.descricao = descricao;
	}

	public static ClassificacaoEnum getEnumByValor(String valor) {
		for (ClassificacaoEnum status : ClassificacaoEnum.values()) {
			if (status.getDescricao().equals(valor))
				return status;
		}
		return null;
	}

	public static ClassificacaoEnum getEnumById(Long id) {
		for (ClassificacaoEnum status : ClassificacaoEnum.values()) {
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
