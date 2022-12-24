package br.gov.rj.fazenda.bloqueio.util;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

/**
 * The Class Mensagem.
 */
public class Mensagem {

	/**
	 * Instantiates a new mensagem.
	 */
	public Mensagem() {
	}

	/**
	 * Adds the.
	 *
	 * @param tipo
	 *            (info, warn, error) default: info
	 * @param mensagem
	 *            the mensagem
	 */
	public static void add(String tipo, String mensagem) {

		FacesMessage message = null;

		switch (tipo) {
		case "info":
			message = new FacesMessage(FacesMessage.SEVERITY_INFO, mensagem, "");
			break;
		case "warn":
			message = new FacesMessage(FacesMessage.SEVERITY_WARN, mensagem, "");
			break;
		case "error":
			message = new FacesMessage(FacesMessage.SEVERITY_ERROR, mensagem, "");
			break;
		default:
			message = new FacesMessage(FacesMessage.SEVERITY_INFO, mensagem, "");
			break;
		}

		FacesContext.getCurrentInstance().addMessage(null, message);
	}
}
