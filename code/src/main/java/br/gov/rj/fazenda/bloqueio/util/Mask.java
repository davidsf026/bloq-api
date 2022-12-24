package br.gov.rj.fazenda.bloqueio.util;

// TODO: Auto-generated Javadoc
/**
 * The Enum Mask.
 */
public enum Mask {

	/** The cpf. */
	CPF("###.###.###-##"),

	/** The cnpj. */
	CNPJ("##.###.###/####-##"),

	/** The cpf cnpj. */
	CPF_CNPJ("##############"),

	/** The inscricao estadual. */
	INSCRICAO_ESTADUAL("##.###.##-#");

	/** The mask. */
	private String mask;

	/**
	 * Instantiates a new mask.
	 *
	 * @param mask
	 *            the mask
	 */
	Mask(String mask) {
		this.mask = mask;
	}

	/**
	 * Gets the mask.
	 *
	 * @return the mask
	 */
	public String getMask() {
		return mask;
	}
}
