package br.com.caelum.calopsita.plugins.owner;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import br.com.caelum.calopsita.model.Card;
import br.com.caelum.calopsita.model.User;
import br.com.caelum.calopsita.persistence.dao.AbstractDaoTest;

public class AssignableCardTest extends AbstractDaoTest {

	private Card card;
	private User owner;
	private AssignableCard assignableCard;

	@Before
	public void setUp() throws Exception {
		super.setUp();
		this.card = new Card();
		this.owner = new User();
		session.save(card);
		assignableCard = new AssignableCard();
		assignableCard.setCard(card);
		assignableCard.setOwner(owner);
				
		session.flush();
	}

	
	@Test
	public void saveAnAssignableCard() throws Exception {
		session.save(assignableCard);		
		Long id = assignableCard.getId();		
		AssignableCard savedAssignableCard = (AssignableCard) session.load(AssignableCard.class, id);
		Card savedCard = savedAssignableCard.getCard();
		Assert.assertEquals(card, savedCard);
	}

	@Test
	public void saveAnAssignableCardOwner() throws Exception {
		session.save(assignableCard);		
		Long id = assignableCard.getId();		
		AssignableCard savedAssignableCard = (AssignableCard) session.load(AssignableCard.class, id);
		User savedOwner = savedAssignableCard.getOwner();
		Assert.assertEquals(owner, savedOwner);
	}
	
}
