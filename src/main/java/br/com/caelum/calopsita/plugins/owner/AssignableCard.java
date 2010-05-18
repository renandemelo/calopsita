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
import br.com.caelum.calopsita.model.User;

@Entity
public class AssignableCard implements Gadget {

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
		String ownerString = (owner != null)? owner.getName():"nobody";
		String html = "";
		
		if (this.card.getIteration() != null && this.card.getIteration().isCurrent()) {
			html = "<sub class=\"assignable\" title=\"Owner\" align=\"right\"> Owned by :cardOwner" +  
							" <a href=\"/calopsita/projects/:project_id/iterations/:iteration_id/card/:card_id/cardOwner/\">Be an Owner now!</a></sub>";
			html = html.replace(":cardOwner", ownerString);
			html = html.replace(":project_id", card.getProject().getId().toString());
			html = html.replace(":iteration_id", card.getIteration() != null ? card.getIteration().getId().toString() : "0");
			html = html.replace(":card_id", card.getId().toString());
		}
		return html;
	}

	@Override
	public void processEvent(Event event) {
		// TODO Auto-generated method stub
		
	}

}
