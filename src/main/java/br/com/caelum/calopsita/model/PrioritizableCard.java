package br.com.caelum.calopsita.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Entity
public class PrioritizableCard implements Gadget {

	@Id
	@GeneratedValue(generator="custom")
	@GenericGenerator(name="custom", strategy="foreign",
			parameters=@Parameter(name="property", value="card"))
	private Long id;

	@OneToOne
	@PrimaryKeyJoinColumn
	private Card card;

	private int priority;


	public Card getCard() {
		return card;
	}

	public void setCard(Card card) {
		this.card = card;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}


}
