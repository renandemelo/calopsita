package br.com.caelum.calopsita.persistence.dao;

import static br.com.caelum.calopsita.CustomMatchers.hasItemsInThisOrder;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.hibernate.Session;
import org.jmock.Expectations;
import org.jmock.Sequence;
import org.junit.Before;
import org.junit.Test;

import br.com.caelum.calopsita.model.Card;
import br.com.caelum.calopsita.model.Gadget;
import br.com.caelum.calopsita.model.Gadgets;
import br.com.caelum.calopsita.model.Project;
import br.com.caelum.calopsita.plugins.PluginResultTransformer;
import br.com.caelum.calopsita.plugins.Transformer;
import br.com.caelum.calopsita.plugins.planning.PlanningCard;
import br.com.caelum.calopsita.plugins.prioritization.OrderByPriorityTransformer;
import br.com.caelum.calopsita.plugins.prioritization.PrioritizableCard;

public class CardDaoTest extends AbstractDaoTest {
	private CardDao dao;
	private Session mockSession;
	private ProjectDao projectDao;
	private ProjectModificationDao modificationDao;

	@Override
	@Before
	public void setUp() throws Exception {
		super.setUp();
		dao = new CardDao(session, new PluginResultTransformer(session, Arrays.<Transformer>asList(new OrderByPriorityTransformer())));
		projectDao = new ProjectDao(session, new PluginResultTransformer(session, new ArrayList<Transformer>()));
		modificationDao = new ProjectModificationDao(session, new PluginResultTransformer(session, new ArrayList<Transformer>()));
	}

	@Test
	public void listingSubcard() throws Exception {
		Card card = givenACard();
		Card subcard = givenASubcard(card);
		Card otherCard = givenACard();
		List<Card> list = dao.listSubcards(card);

		assertThat(list, hasItem(subcard));
		assertThat(list, not(hasItem(card)));
		assertThat(list, not(hasItem(otherCard)));
	}

	@Test
	public void orderedListings() throws Exception {
		Project project = givenAProject();
		Card card3 = givenAPlanningCard(project, withPriority(3));
		Card card1 = givenAPlanningCard(project, withPriority(1));

		assertThat(dao.listRootFrom(project), hasItemsInThisOrder(card1, card3));
	}
	@Test
	public void shouldNotListCardsWithParent() throws Exception {
		Project project = givenAProject();
		Card parent = givenACard(project);
		Card subcard = givenASubcard(parent);

		assertThat(dao.listRootFrom(project), hasItem(parent));
		assertThat(dao.listRootFrom(project), not(hasItem(subcard)));
	}

	@Test
	public void listingGadgets() throws Exception {
		Card card = givenAPlanningCard(givenAProject(), withPriority(1));

		List<Gadget> gadgets = dao.listGadgets(card);


		assertThat(gadgets.size(), is(2));
		assertThat(gadgets, hasItem(instanceOf(PrioritizableCard.class)));
		assertThat(gadgets, hasItem(instanceOf(PlanningCard.class)));
	}

	@Test
	public void updatingGadgets() throws Exception {
		Card card = givenACard(givenAProject());

		whenIAddPriorizationGadget(card);

		List<Gadget> gadgets = dao.listGadgets(card);
		assertThat(gadgets.size(), is(1));
		assertThat(gadgets, hasItem(instanceOf(PrioritizableCard.class)));

		whenIRemoveAllGadgets(card);

		assertThat(dao.listGadgets(card).size(), is(0));

	}

	@Test
	public void testDelegation() throws Exception {
		CardDao mockedDao = givenAMockedDao();
		shouldExecuteCrudInSequence();
		mockedDao.add(new Card());
		mockedDao.load(new Card());
		mockedDao.update(new Card());
		mockedDao.remove(new Card());
		mockery.assertIsSatisfied();
	}

	private void shouldExecuteCrudInSequence() {
		final Sequence crud = mockery.sequence("crud");
		mockery.checking(new Expectations() {
			{
				one(mockSession).save(with(any(Card.class))); inSequence(crud);
				one(mockSession).load(Card.class, null); will(returnValue(new Card())); inSequence(crud);
				one(mockSession).update(with(any(Card.class))); inSequence(crud);
				one(mockSession).delete(with(any(Card.class))); inSequence(crud);
			}
		});
	}

	private CardDao givenAMockedDao() {
		mockSession = mockery.mock(Session.class);
		return new CardDao(mockSession, null);
	}

	private void whenIAddPriorizationGadget(Card card) {
		dao.updateGadgets(card, Arrays.asList(Gadgets.PRIORITIZATION));
	}

	private void whenIRemoveAllGadgets(Card card) {
		dao.updateGadgets(card, new ArrayList<Gadgets>());
	}

	private Matcher<? extends Gadget> instanceOf(Class<? extends Gadget> type) {
		 Matcher matcher = Matchers.instanceOf(type);
		 return matcher;
	}

	private Card givenAPlanningCard(Project project) {
		Card card = givenACard(project);
		session.save(PlanningCard.of(card));
		session.flush();
		return card;
	}

	private Card givenAPlanningCard(Project project, int priority) {
		Card card = givenAPlanningCard(project);

		PrioritizableCard pCard = new PrioritizableCard();
		pCard.setCard(card);
		pCard.setPriority(priority);
		session.save(pCard);
		session.flush();
		return card;
	}


	private int withPriority(int i) {
		return i;
	}


	private Card givenASubcard(Card card) {
		Card sub = givenACard();
		sub.setParent(card);
		session.flush();
		return sub;
	}

	private Card givenACard() {
		Card card = new Card(dao);
		card.setName("Rumpelstitlskin");
		card.setDescription("I hope I spelld his name correctly");
		session.save(card);
		session.flush();
		return card;

	}
	private Card givenACard(Project project) {
		Card card = givenACard();
		card.setProject(project);
		session.flush();
		return card;
	}

	private Project givenAProject() {
		Project project = new Project(projectDao, modificationDao);
		project.setName("A project");
		session.save(project);
		session.flush();
		return project;
	}
}
