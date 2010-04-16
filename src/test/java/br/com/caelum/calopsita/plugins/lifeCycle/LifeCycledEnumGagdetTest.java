package br.com.caelum.calopsita.plugins.lifeCycle;

import java.util.Arrays;
import java.util.List;

import org.joda.time.LocalDate;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import br.com.caelum.calopsita.model.Card;
import br.com.caelum.calopsita.model.Gadgets;
import br.com.caelum.calopsita.persistence.dao.AbstractDaoTest;
import br.com.caelum.calopsita.persistence.dao.CardDao;
import br.com.caelum.calopsita.plugins.PluginResultTransformer;
import br.com.caelum.calopsita.plugins.Transformer;
import br.com.caelum.calopsita.plugins.prioritization.OrderByPriorityTransformer;

public class LifeCycledEnumGagdetTest extends AbstractDaoTest{

	private Card card;	
	
	@Before
	public void setUp() throws Exception {
		super.setUp();
		this.card = new Card(new CardDao(session, new PluginResultTransformer(session, Arrays.<Transformer>asList(new OrderByPriorityTransformer()))));
		this.card.save();
	}
	
	@Test
	public void testCreationOfLifeCycledCard() throws Exception {
		List<Gadgets> gadgets = Arrays.asList(Gadgets.LIFE_CYCLE);
		card.addGadgets(gadgets);
		session.flush();
		LifeCycledCard lifeCycledCard = (LifeCycledCard) card.getGadgets().get(0);
		Assert.assertNotNull(lifeCycledCard.getCreationDate());
		Assert.assertEquals(new LocalDate(), lifeCycledCard.getCreationDate());
	}
	
	
	
}
