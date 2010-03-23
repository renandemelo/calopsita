package br.com.caelum.calopsita.plugins.lifeCicle;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import org.hibernate.annotations.Type;
import org.joda.time.LocalDate;

import br.com.caelum.calopsita.model.Card;

@Entity
public class LifeCicledCard {

	@Id
	@GeneratedValue
	private Long id;

	@OneToOne
	private Card card;

	@Type(type = "org.joda.time.contrib.hibernate.PersistentLocalDate")
	private LocalDate date;

	public LifeCicledCard() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public void setCard(Card card) {
		this.card = card;
	}

	public Card getCard() {
		return card;
	}

}
