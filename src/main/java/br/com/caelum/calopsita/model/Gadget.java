package br.com.caelum.calopsita.model;

/**
 * @author lucascs
 *
 */
public interface Gadget {

	Card getCard();
	
	void processEvent(Event event);

	String getHtml();
}
