package br.com.caelum.calopsita.plugins.lifeCicle;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import br.com.caelum.calopsita.model.Card;
import br.com.caelum.calopsita.persistence.dao.AbstractDaoTest;


public class LifeCicledCardTest extends AbstractDaoTest{
	
	private Card card;

	@Before
	public void setUp() throws Exception {
		super.setUp();
		this.card = new Card();
		session.save(card);
		session.flush();
	}

	@Test
	public void saveALifeCicledCard() throws Exception {
		LifeCicledCard lifeCicledCard = new LifeCicledCard();
		lifeCicledCard.setCard(card);
		session.save(lifeCicledCard);
		
		Long id = lifeCicledCard.getId();		
		LifeCicledCard savedCicledCard = (LifeCicledCard) session.load(LifeCicledCard.class, id);
		Card savedCard = savedCicledCard.getCard();
		Assert.assertEquals(card, savedCard);
	}

}
