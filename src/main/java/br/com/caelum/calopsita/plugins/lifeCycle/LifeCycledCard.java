package br.com.caelum.calopsita.plugins.lifeCycle;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import br.com.caelum.calopsita.model.Card;
import br.com.caelum.calopsita.model.Gadget;

@Entity
public class LifeCycledCard implements Gadget{

	@Id
	@GeneratedValue(generator="custom")
	@GenericGenerator(name="custom", strategy="foreign",
			parameters=@Parameter(name="property", value="card"))
	private Long id;

	@OneToOne
	@PrimaryKeyJoinColumn
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
		DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd");
		return "<div>" + formatter.print(creationDate) + "</div>";
	}

	public static LifeCycledCard of(Card card) {
		LifeCycledCard lifeCycledCard = new LifeCycledCard();
		lifeCycledCard.setCard(card);
		lifeCycledCard.setCreationDate(new LocalDate());
		return lifeCycledCard;
	}

}
