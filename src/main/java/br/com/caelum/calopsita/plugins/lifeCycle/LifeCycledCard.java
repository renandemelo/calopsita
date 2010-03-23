package br.com.caelum.calopsita.plugins.lifeCycle;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import org.hibernate.annotations.Type;
import org.joda.time.LocalDate;

import br.com.caelum.calopsita.model.Card;
import br.com.caelum.calopsita.model.Gadget;

@Entity
public class LifeCycledCard implements Gadget{

	@Id
	@GeneratedValue
	private Long id;

	@OneToOne
	private Card card;

	@Type(type = "org.joda.time.contrib.hibernate.PersistentLocalDate")
	private LocalDate finishDate;
	
	@Type(type = "org.joda.time.contrib.hibernate.PersistentLocalDate")
	private LocalDate creationDate;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setCard(Card card) {
		this.card = card;
	}

	public Card getCard() {
		return card;
	}

	public void setCreationDate(LocalDate localDate) {
		this.creationDate = localDate;	
	}

	public LocalDate getCreationDate() {
		return this.creationDate;
	}

	public void setFinishDate(LocalDate date) {
		this.finishDate = date;
	}

	public LocalDate getFinishDate() { 
		return this.finishDate;
	}

	@Override
	public String getHtml() {
		return null;
	}

	public static LifeCycledCard of(Card card) {
		LifeCycledCard lifeCycledCard = new LifeCycledCard();
		lifeCycledCard.setCard(card);
		lifeCycledCard.setCreationDate(new LocalDate());
		return lifeCycledCard;
	}

}
