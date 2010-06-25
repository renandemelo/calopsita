package br.com.caelum.calopsita.persistence.dao;

import static br.com.caelum.calopsita.CustomMatchers.hasItemsInThisOrder;
import static br.com.caelum.calopsita.CustomMatchers.hasSameId;
import static br.com.caelum.calopsita.CustomMatchers.isEmpty;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;

import java.text.ParseException;
import java.util.Arrays;
import java.util.List;

import org.hibernate.Session;
import org.jmock.Expectations;
import org.jmock.Sequence;
import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;

import br.com.caelum.calopsita.model.Card;
import br.com.caelum.calopsita.model.Iteration;
import br.com.caelum.calopsita.model.Project;
import br.com.caelum.calopsita.model.User;
import br.com.caelum.calopsita.model.Card.Status;
import br.com.caelum.calopsita.plugins.PluginResultTransformer;
import br.com.caelum.calopsita.plugins.Transformer;
import br.com.caelum.calopsita.plugins.planning.PlanningCard;
import br.com.caelum.calopsita.plugins.prioritization.OrderByPriorityTransformer;
import br.com.caelum.calopsita.plugins.prioritization.PrioritizableCard;
import br.com.caelum.calopsita.repository.ProjectRepository;

public class ProjectDaoTest extends AbstractDaoTest {

	private ProjectRepository dao;
	private IterationDao iterationDao;
	private CardDao cardDao;
	private UserDao userDao;
	private Session mockSession;
	private ProjectModificationDao modificationDao;

	@Override
	@Before
	public void setUp() throws Exception {
		super.setUp();
		List<Transformer> transformers = Arrays.<Transformer>asList(new OrderByPriorityTransformer());
		PluginResultTransformer transformer = new PluginResultTransformer(session, transformers);
		dao = new ProjectDao(session, transformer);
		modificationDao = new ProjectModificationDao(session, transformer);
		iterationDao = new IterationDao(session, transformer);
		cardDao = new CardDao(session,transformer);
		userDao = new UserDao(session);
	}

	@Test
    public void gettingCurrentIterationWithNoDates() throws Exception {
        Iteration iteration = givenAnIteration(null, null);
        Iteration current = dao.getCurrentIterationFromProject(iteration.getProject());

        assertThat(current, not(is(iteration)));
    }

    @Test
    public void gettingCurrentIterationAlreadyStartedButNotFinished() throws Exception {
        Iteration iteration = givenAnIteration(withStartDate(yesterday()), withEndDate(tomorrow()));
        Iteration current = dao.getCurrentIterationFromProject(iteration.getProject());

        assertThat(current, is(iteration));
    }

    @Test
    public void gettingCurrentIterationAlreadyStartedAndFinished() throws Exception {
        Iteration iteration = givenAnIteration(withStartDate(yesterday()), withEndDate(yesterday()));
        Iteration current = dao.getCurrentIterationFromProject(iteration.getProject());

        assertThat(current, not(is(iteration)));
    }

    @Test
    public void gettingCurrentIterationAlreadyStartedWithNoEndDate() throws Exception {
        Iteration iteration = givenAnIteration(withStartDate(yesterday()));
        Iteration current = dao.getCurrentIterationFromProject(iteration.getProject());

        assertThat(current, is(iteration));
    }

    @Test
    public void gettingCurrentIterationNotStartedYet() throws Exception {
        Iteration iteration = givenAnIteration(withStartDate(tomorrow()));
        Iteration current = dao.getCurrentIterationFromProject(iteration.getProject());

        assertThat(current, not(is(iteration)));
    }

	@Test
	public void orderedListings() throws Exception {
		Project project = givenAProject();
		Card card3 = givenAPlanningCard(project, withPriority(3));
		Card card1 = givenAPlanningCard(project, withPriority(1));

		assertThat(dao.planningCardsWithoutIteration(project), hasItemsInThisOrder(card1, card3));
	}

	@Test
	public void cardsWithoutIteration() throws Exception {
		Iteration iteration = givenAnIteration();
		Card card = givenAPlanningCard(iteration.getProject());
		Card cardOfIteration = givenAPrioritizableCardOfTheIteration(iteration);
		Card cardOfOtherProject = givenAPlanningCard(givenAProject());
		Card notAPlanningCard = givenACard(givenAProject());

		List<Card> list = dao.planningCardsWithoutIteration(iteration.getProject());

		assertThat(list, hasItem(card));
		assertThat(list, not(hasItem(cardOfIteration)));
		assertThat(list, not(hasItem(cardOfOtherProject)));
		assertThat(list, not(hasItem(notAPlanningCard)));
	}

	@Test
	public void refreshingAProject() throws Exception {
		Project project = givenAProject();

		session.evict(project);

		Project project2 = new Project(dao, modificationDao);
		project2.setId(project.getId());
		project2.refresh();

		assertThat(project2.getName(), is(project.getName()));
	}

	@Test
    public void onlyListCardsFromTheGivenProject() throws Exception {
        Project project = givenAProject();
        Card cardFromOtherProject = givenACard();
        Card cardFromThisProject = givenACardOfProject(project);

        List<Card> list = dao.listRootCardsFrom(project);

        assertThat(list, not(hasItem(hasSameId(cardFromOtherProject))));
        assertThat(list, hasItem(hasSameId(cardFromThisProject)));
    }
	@Test
	public void doNotListSubCardsWhenListingCards() throws Exception {
		Project project = givenAProject();
		Card parent = givenACardOfProject(project);
		Card subcard = givenASubcardOf(parent);
		List<Card> list = dao.listRootCardsFrom(project);

		assertThat(list, not(hasItem(hasSameId(subcard))));
		assertThat(list, hasItem(hasSameId(parent)));
	}
	@Test
	public void onlyListToDoCardsFromTheGivenProject() throws Exception {
		Project project = givenAProject();
		Card cardFromOtherProject = givenACard();
		Card cardFromThisProject = givenACardOfProject(project);
		Card donecardFromThisProject = givenACardOfProject(project);
		donecardFromThisProject.setStatus(Status.DONE);

		List<Card> list = dao.listTodoCardsFrom(project);

		assertThat(list, not(hasItems(hasSameId(cardFromOtherProject), hasSameId(donecardFromThisProject))));
		assertThat(list, hasItem(hasSameId(cardFromThisProject)));
	}

	@Test
	public void doNotListSubCardsWhenListingTodoCards() throws Exception {
		Project project = givenAProject();
		Card parent = givenACardOfProject(project);
		Card subcard = givenASubcardOf(parent);
		List<Card> list = dao.listTodoCardsFrom(project);

		assertThat(list, not(hasItem(hasSameId(subcard))));
		assertThat(list, hasItem(hasSameId(parent)));
	}
	@Test
    public void onlyListIterationsFromTheGivenProject() throws Exception {
        Project project = givenAProject();
        Iteration iterationFromOtherProject = givenAnIteration();
        Iteration iterationFromThisProject = givenAnIterationOfProject(project);

        List<Iteration> list = dao.listIterationsFrom(project);

        assertThat(list, not(hasItem(hasSameId(iterationFromOtherProject))));
        assertThat(list, hasItem(hasSameId(iterationFromThisProject)));
    }

	@Test
	public void removeAProjectAlsoRemoveRelatedCardsAndIterations() throws Exception {
		Project project = givenAProject();
		givenAnIterationOfProject(project);
		givenACardOfProject(project);

		dao.remove(project);

		assertThat(dao.listIterationsFrom(project), isEmpty());
		assertThat(dao.listRootCardsFrom(project), isEmpty());
	}

	@Test
	public void aProjectNotOwnedByAUserIsAnInconsistentValue() throws Exception {
		Project project = givenAProject();
		User user = givenAUser();

		assertThat(dao.hasInconsistentValues(new Object[] {project}, user), is(true));
	}
	@Test
	public void aProjectNotOwnedByAUserIsAConsistentValue() throws Exception {
		User user = givenAUser();
		Project project1 = givenAProjectOwnedBy(user);
		Project project2 = givenAProjectWithColaborator(user);

		assertThat(dao.hasInconsistentValues(new Object[] {project1, project2}, user), is(false));
	}
	@Test
	public void checkingForInconsistentValuesInIterations() throws Exception {
		User user = givenAUser();

		Iteration unrelatedProject = givenAnIterationOfProject(givenAProject());
		assertThat(dao.hasInconsistentValues(new Object[] {unrelatedProject}, user), is(true));

		Iteration projectOwned = givenAnIterationOfProject(givenAProjectOwnedBy(user));
		Iteration projectWithColaborator = givenAnIterationOfProject(givenAProjectWithColaborator(user));
		assertThat(dao.hasInconsistentValues(new Object[] {projectOwned, projectWithColaborator}, user), is(false));
	}
	@Test
	public void iterationWithWrongProjectId() throws Exception {
		User user = givenAUser();
		Iteration iteration = givenAnIterationOfProject(givenAProjectOwnedBy(user));
		session.evict(iteration);
		iteration.setProject(givenAProjectOwnedBy(user));

		assertThat(dao.hasInconsistentValues(new Object[] {iteration}, user), is(true));
	}
	@Test
	public void cardWithWrongProjectId() throws Exception {
		User user = givenAUser();
		Card card = givenACardOfProject(givenAProjectOwnedBy(user));
		session.evict(card);
		card.setProject(givenAProjectOwnedBy(user));

		assertThat(dao.hasInconsistentValues(new Object[] {card}, user), is(true));
	}
	@Test
	public void cardWithoutIdWithAProjectIdIsConsistent() throws Exception {
		User user = givenAUser();
		Card card = givenACardOfProject(givenAProjectOwnedBy(user));
		session.evict(card);
		card.setId(null);

		assertThat(dao.hasInconsistentValues(new Object[] {card}, user), is(false));
	}

	@Test(expected=IllegalArgumentException.class)
	public void iterationWithoutProjectIdThrowsException() throws Exception {
		Iteration projectWithoutId = new Iteration(iterationDao);
		projectWithoutId.setProject(new Project(dao, modificationDao));

		dao.hasInconsistentValues(new Object[] {projectWithoutId}, givenAUser());
	}

	@Test(expected=IllegalArgumentException.class)
	public void iterationWithoutProjectThrowsException() throws Exception {
		Iteration outOfProject = new Iteration(iterationDao);
		dao.hasInconsistentValues(new Object[] {outOfProject}, givenAUser());
	}

	@Test
	public void findUnrelatedUsers() throws Exception {
		Project project = givenAProject();
		User owner = givenAnUserOwnerOf(project);
		User colaborator = givenAnUserColaboratorOf(project);
		User user = givenAnUnrelatedUser("pedro");

		List<User> users = dao.listUnrelatedUsers(project);

		assertThat(users, hasItem(user));
		assertThat(users, not(hasItem(owner)));
		assertThat(users, not(hasItem(colaborator)));

	}

	@Test
	public void findUnreleatedUsersWhenThereIsNoColaborator() throws Exception {
		Project project = givenAProject();
		User owner = givenAnUserOwnerOf(project);
		User user = givenAnUnrelatedUser("pedro");

		List<User> users = dao.listUnrelatedUsers(project);

		assertThat(users, hasItem(user));
		assertThat(users, not(hasItem(owner)));
	}

	@Test
	public void shouldShowOnlyLastFiveAddedCards() throws Exception {
		Project project = givenAProject();
		Card first = givenACard(project);
		Card second = givenACard(project);
		Card third = givenACard(project);
		Card forth = givenACard(project);
		Card fifth = givenACard(project);
		Card sixth = givenACard(project);

		List<Card> list = dao.listLastAddedCards(project);

		assertThat(list.size(), is(5));
		assertThat(list, not(hasItem(first)));
		assertThat(list, hasItems(second, third, forth, fifth, sixth));
	}
	@Test
	public void testDelegation() throws Exception {
		ProjectRepository mockedDao = givenAMockedDao();
		shouldExecuteCrudInSequence();
		mockedDao.add(new Project());
		mockedDao.load(new Project());
		mockedDao.update(new Project());
		mockery.assertIsSatisfied();
	}

	@Test
	public void cardsWithoutIterationDontIncludeCardsFromOtherProject() throws Exception {
		Project project = givenAProject();
		Card cardFromProject = givenAPlanningCard(project);
		Card cardFromOtherProject = givenAPlanningCard(givenAProject());
		List<Card> cards = dao.planningCardsWithoutIteration(project);
		assertThat(cards, hasItem(cardFromProject));
		assertThat(cards, not(hasItem(cardFromOtherProject)));
		mockery.assertIsSatisfied();
	}
	@Test
	public void cardsWithoutIterationDontIncludeCardsInAnIteration() throws Exception {
		Project project = givenAProject();
		Iteration iteration = givenAnIteration(project);

		Card cardFromOtherIteration = givenAPlanningCard(project);
		Card cardWithoutIteration = givenAPlanningCardOfIteration(iteration);

		List<Card> cards = dao.planningCardsWithoutIteration(project);
		assertThat(cards, hasItem(cardFromOtherIteration));
		assertThat(cards, not(hasItem(cardWithoutIteration)));
		mockery.assertIsSatisfied();
	}
	@Test
	public void cardsWithoutIterationIncludeTodoCardsOfAnPastIteration() throws Exception {
		Project project = givenAProject();
		Iteration pastIteration = givenAnIteration(yesterday(), yesterday(), project);
		Iteration futureIteration = givenAnIteration(tomorrow(), tomorrow(), project);

		Card donePastCard = givenAPlanningCardOfIteration(pastIteration);
		donePastCard.setStatus(Status.DONE);

		Card todoPastCard = givenAPlanningCardOfIteration(pastIteration);
		todoPastCard.setStatus(Status.TODO);

		Card doneFutureCard = givenAPlanningCardOfIteration(futureIteration);
		doneFutureCard.setStatus(Status.DONE);

		Card todoFutureCard = givenAPlanningCardOfIteration(futureIteration);
		todoFutureCard.setStatus(Status.TODO);

		List<Card> cards = dao.planningCardsWithoutIteration(project);
		assertThat(cards, not(hasItem(doneFutureCard)));
		assertThat(cards, not(hasItem(todoFutureCard)));
		assertThat(cards, not(hasItem(donePastCard)));
		assertThat(cards, hasItem(todoPastCard));
		mockery.assertIsSatisfied();
	}

    private Iteration givenAnIteration(LocalDate startDate, LocalDate endDate, Project project) {
    	Iteration iteration = givenAnIteration(startDate, endDate);
    	iteration.setProject(project);
    	session.flush();
		return iteration;
	}

	private Iteration givenAnIteration(Project project) throws ParseException {
    	Iteration iteration = givenAnIteration();
    	iteration.setProject(project);
		return iteration;
	}

	private Card givenAPlanningCardOfIteration(Iteration iteration) {
    	Card card = givenAPlanningCard(iteration.getProject());
    	card.setIteration(iteration);
    	return card;
	}

	private ProjectRepository givenAMockedDao() {
		mockSession = mockery.mock(Session.class);
		return new ProjectDao(mockSession, null);
	}

	private void shouldExecuteCrudInSequence() {
		final Sequence crud = mockery.sequence("crud");
		mockery.checking(new Expectations() {
			{
				one(mockSession).save(with(any(Project.class))); inSequence(crud);
				one(mockSession).load(Project.class, null); will(returnValue(new Project())); inSequence(crud);
				one(mockSession).update(with(any(Project.class))); inSequence(crud);
			}
		});
	}


	private User givenAnUserColaboratorOf(Project project) {
		User user = givenAnUnrelatedUser("caue");
		project.setColaborators(Arrays.asList(user));
		this.session.flush();
		return user;
	}

	private User givenAnUserOwnerOf(Project project) {
		User user = givenAnUnrelatedUser("lucas");
		project.setOwner(user);
		this.session.flush();
		return user;
	}

	private Iteration givenAnIteration(LocalDate startDate, LocalDate endDate) {
        Iteration iteration = new Iteration(iterationDao);
        iteration.setGoal("An iteration");
        iteration.setProject(givenAProject());
        iteration.setStartDate(startDate);
        iteration.setEndDate(endDate);
        session.save(iteration);
        session.flush();
        return iteration;
    }

	private User givenAnUnrelatedUser(String name) {
		User user = new User(userDao);
		user.setName(name);
		user.setEmail(name + "@caelum.com.br");
		user.setLogin(name);
		user.setPassword(name);
		this.session.save(user);
		this.session.flush();
		return user;
	}

	private Iteration givenAnIteration(LocalDate startDate) {
        Iteration iteration = new Iteration(iterationDao);
        iteration.setGoal("An iteration");
        iteration.setProject(givenAProject());
        iteration.setStartDate(startDate);
        session.save(iteration);
        session.flush();
        return iteration;
    }

	private Iteration givenAnIterationOfProject(Project project) throws ParseException {
        Iteration iteration = givenAnIteration();
        iteration.setProject(project);
        session.update(iteration);
        session.flush();
        return iteration;
    }


    private Iteration givenAnIteration() throws ParseException {
	    Iteration iteration = new Iteration(iterationDao);
	    iteration.setGoal("Be ready");
	    iteration.setStartDate(yesterday());
	    iteration.setEndDate(tomorrow());
	    iteration.setProject(givenAProject());
	    session.save(iteration);
	    session.flush();
	    return iteration;
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

	private Card givenACardOfProject(Project project) {
		Card card = givenACard();
		card.setProject(project);
		session.update(card);
		session.flush();
		return card;
	}

	private Card givenASubcardOf(Card card) {
		Card subcard = givenACardOfProject(card.getProject());
		subcard.setParent(card);
		session.update(subcard);
		session.flush();
		return subcard;
	}

	private Card givenACard() {
		Card card = new Card(cardDao);
		card.setName("Snow White");
		card.setDescription("She hangs out with the dwarves");
		session.save(card);
		session.flush();
		return card;
	}

	private LocalDate withEndDate(LocalDate date) {
        return date;
    }

    private LocalDate tomorrow() {
        return new LocalDate().plusDays(1);
    }

    private LocalDate withStartDate(LocalDate date) {
        return date;
    }

    private LocalDate yesterday() {
        return new LocalDate().minusDays(1);
    }


	private Card givenAPrioritizableCardOfTheIteration(Iteration iteration) {
		Card card = givenAPlanningCard(iteration.getProject());
		card.setIteration(iteration);
		session.update(card);
		session.flush();
		return card;
	}

	private Card givenAPlanningCard(Project project) {
		Card card = givenACard(project);
		session.save(PlanningCard.of(card));
		session.flush();
		return card;
	}


	private Card givenACard(Project project) {
		Card card = givenACard();
		card.setProject(project);
		session.flush();
		return card;
	}

	private Project givenAProjectWithColaborator(User user) {
		Project project = givenAProject();
		project.getColaborators().add(user);
		session.update(project);
		session.flush();
		return project;
	}
	private Project givenAProjectOwnedBy(User user) {
		Project project = givenAProject();
		project.setOwner(user);
		session.update(project);
		session.flush();
		return project;
	}

	private Project givenAProject() {
		Project project = new Project(dao, modificationDao);
		project.setName("Tuba");
		session.save(project);
		session.flush();
		return project;
	}

	private User givenAUser() {
		User user = new User(userDao);
		user.setLogin("test");
		user.setPassword("test");
		user.setName("User test");
		user.setEmail("test@caelum.com.br");
		session.save(user);
		session.flush();
		return user;
	}
}
