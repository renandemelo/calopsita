package br.com.caelum.calopsita.plugins.lifeCycle;

import static org.junit.Assert.*;

import org.joda.time.LocalDate;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import br.com.caelum.calopsita.model.Card;
import br.com.caelum.calopsita.model.Event;
import br.com.caelum.calopsita.persistence.dao.AbstractDaoTest;
import br.com.caelum.calopsita.plugins.lifeCycle.LifeCycledCard;


public class LifeCycledCardTest extends AbstractDaoTest{
	
	private Card card;
	private LifeCycledCard lifeCicledCard;

	@Before
	public void setUp() throws Exception {
		super.setUp();
		this.card = new Card();
		session.save(card);		
		lifeCicledCard = new LifeCycledCard();
		lifeCicledCard.setCard(card);		
		session.flush();		
	}
	
	@Test
	public void saveALifeCicledCard() throws Exception {
		session.save(lifeCicledCard);		
		Long id = lifeCicledCard.getId();		
		LifeCycledCard savedCicledCard = (LifeCycledCard) session.load(LifeCycledCard.class, id);
		Card savedCard = savedCicledCard.getCard();
		Assert.assertEquals(card, savedCard);
	}

	
	@Test
	public void saveACardWithCreationDate() throws Exception {
		LocalDate date = new LocalDate();
		lifeCicledCard.setCreationDate(date);		
		session.save(lifeCicledCard);
		Long id = lifeCicledCard.getId();
		LifeCycledCard savedCicledCard = (LifeCycledCard) session.load(LifeCycledCard.class, id);
		Assert.assertEquals(date, savedCicledCard.getCreationDate());
	}
	
	@Test
	public void saveACardWithAFinishDate() throws Exception {
		LocalDate date = new LocalDate();
		lifeCicledCard.setFinishDate(date);		
		session.save(lifeCicledCard);
		Long id = lifeCicledCard.getId();		
		LifeCycledCard savedCicledCard = (LifeCycledCard) session.load(LifeCycledCard.class, id);
		Assert.assertEquals(date, savedCicledCard.getFinishDate());
	}
	
	@Test
	public void gettingTheFinishingDate() throws Exception {
		lifeCicledCard.processEvent(Event.END);
		session.save(lifeCicledCard);
		Assert.assertEquals(new LocalDate(), lifeCicledCard.getFinishDate());
	}
}
