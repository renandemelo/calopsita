package br.com.caelum.calopsita.plugins.owner;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import br.com.caelum.calopsita.model.Card;
import br.com.caelum.calopsita.model.Event;
import br.com.caelum.calopsita.model.Gadget;
import br.com.caelum.calopsita.model.Identifiable;
import br.com.caelum.calopsita.model.User;

@Entity
public class AssignableCard implements Gadget{

	@Id
	@GeneratedValue(generator = "custom")
	@GenericGenerator(name = "custom", strategy = "foreign", parameters = @Parameter(name = "property", value = "card"))
	private Long id;

	@OneToOne
	@PrimaryKeyJoinColumn
	private Card card;

	@ManyToOne
	private User owner;

	public static AssignableCard of(Card card) {
		AssignableCard assignableCard = new AssignableCard();
		assignableCard.setCard(card);
		return assignableCard;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public User getOwner() {
		return owner;
	}

	public void setOwner(User owner) {
		this.owner = owner;
	}

	public void setCard(Card card) {
		this.card = card;
	}

	@Override
	public Card getCard() {
		return card;
	}

	@Override
	public String getHtml() {		
		return null;
	}

	@Override
	public void processEvent(Event event) {
		// TODO Auto-generated method stub		
	}

	@Override
	public String getUrlHtml() {
		return "/WEB-INF/jsp/cardOwner/gadget.jsp";
	}

}
