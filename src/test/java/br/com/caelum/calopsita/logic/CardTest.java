package br.com.caelum.calopsita.logic;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.api.Action;
import org.jmock.internal.ExpectationBuilder;
import org.jmock.internal.ExpectationCollector;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import br.com.caelum.calopsita.controller.CardsController;
import br.com.caelum.calopsita.infra.vraptor.SessionUser;
import br.com.caelum.calopsita.mocks.MockHttpSession;
import br.com.caelum.calopsita.model.Card;
import br.com.caelum.calopsita.model.Gadgets;
import br.com.caelum.calopsita.model.Iteration;
import br.com.caelum.calopsita.model.Project;
import br.com.caelum.calopsita.model.ProjectModification;
import br.com.caelum.calopsita.model.User;
import br.com.caelum.calopsita.repository.CardRepository;
import br.com.caelum.calopsita.repository.IterationRepository;
import br.com.caelum.calopsita.repository.ProjectModificationRepository;
import br.com.caelum.calopsita.repository.ProjectRepository;
import br.com.caelum.vraptor.util.test.MockResult;
import br.com.caelum.vraptor.util.test.MockValidator;
import br.com.caelum.vraptor.validator.ValidationException;

public class CardTest {
    private Mockery mockery;
    private CardsController logic;
	private CardRepository repository;
	private Card currentCard;
	private ProjectRepository projectRepository;
	private IterationRepository iterationRepository;
    private Project project;
	private User currentUser;
	private MockValidator validator;
	private ProjectModificationRepository projectModificationsRepository;

    @Before
    public void setUp() throws Exception {
        mockery = new Mockery();
        repository = mockery.mock(CardRepository.class);
        projectModificationsRepository = mockery.mock(ProjectModificationRepository.class);
		SessionUser sessionUser = new SessionUser(new MockHttpSession());
        currentUser = new User();
        currentUser.setLogin("me");
        sessionUser.setUser(currentUser);

		projectRepository = mockery.mock(ProjectRepository.class);
		project = new Project(projectRepository);

		iterationRepository = mockery.mock(IterationRepository.class);
		
		validator = new MockValidator();
		logic = new CardsController(new MockResult(), validator, sessionUser);
    }


    @Test
	public void savingACard() throws Exception {
    	Project project = givenAProject();
    	shouldEventuallyLoadTheProject(project);
		Card card = givenACard();
		card.setName("Log development");

		shouldSaveOnTheRepositoryTheCard(card);
		shouldSaveModificationOnRepository("Created card 'Log development'");

		whenISaveTheCard(card, onThe(project));

		assertThat(card.getProject(), is(project));
		assertThat(card.getCreator(), is(currentUser));
		ProjectModification projectModification = card.getProject().getLastModifications().get(0);
		assertThat(projectModification.getDescription(), is("Created card 'Log development'"));
		mockery.assertIsSatisfied();
	}

    private void shouldSaveModificationOnRepository(String description) {
    	this.mockery.checking(new Expectations() {
    		{
				exactly(1).of(projectModificationsRepository).add(with(any(ProjectModification.class)));
			}});
	}


	private void shouldEventuallyLoadTheProject(final Project project) {
		this.mockery.checking(new Expectations() {
			{
				exactly(1).of(projectRepository).load(with(any(Project.class)));
				will(returnValue(project));
				
				exactly(1).of(projectModificationsRepository).add(with(any(ProjectModification.class)));
				
				exactly(1).of(projectRepository).listModificationsFrom(project);
				will(returnValue(new ArrayList()));
				
			}
		});
	}


	@Test
	public void savingACardWithParentWithIteration() throws Exception {
    	Project project = givenAProject();
		final Card card = givenACard();
		card.setName("Child");
		final Card parent = givenACard();
		parent.setName("Parent");
		Iteration iteration = givenAnIteration();
		
		mockery.checking(new Expectations() {
			{
				one(repository).listSubcards(parent);
				will(returnValue(Arrays.asList()));
			}
		});
		
		parent.setIteration(iteration);
		
		mockery.checking(new Expectations() {
			{
				one(repository).listSubcards(card);
				will(returnValue(Arrays.asList()));
			}
		});
		card.setParent(parent);	
		
		assertThat(parent.getIteration(), is(iteration));
		assertThat(card.getIteration(), is(iteration));
		
		mockery.assertIsSatisfied();
	}
    
    @Test
	public void settingNewIterationToAParentCard() throws Exception {
    	Project project = givenAProject();
		final Card card = givenACard();
		card.setName("Child");
		final Card parent = givenACard();
		parent.setName("Parent");
		Iteration iteration = givenAnIteration();
		
		mockery.checking(new Expectations() {
			{
				one(repository).listSubcards(card);
				will(returnValue(Arrays.asList()));
			}
		});
		card.setParent(parent);	
		
		mockery.checking(new Expectations() {
			{
				one(repository).listSubcards(parent);
				will(returnValue(Arrays.asList(card)));
			}
		});

		parent.setIteration(iteration);
				
		assertThat(parent.getIteration(), is(iteration));
		assertThat(card.getIteration(), is(iteration));
		
		mockery.assertIsSatisfied();
	}
    
    @Test
	public void editingACardsDescription() throws Exception {
    	Card card = givenACard();
    	givenTheCard(card).withName("Huck Finn")
    						.withDescription("He is Tom Sawyer's best mate.");

    	Card loadedCard = shouldLoadTheCard(card);
    	shouldUpdateOnTheRepositoryTheCard(loadedCard);

    	whenIEditTheCard(card, changingNameTo("Huckleberry Finn"), changingDescriptionTo("He has a drunk father."));

		assertThat(loadedCard.getName(), is("Huckleberry Finn"));
		assertThat(loadedCard.getDescription(), is("He has a drunk father."));
		mockery.assertIsSatisfied();
	}

    @Test
	public void removeACardFromMyProject() throws Exception {
		Card card = givenACard();
		givenTheProjectIsOwnedBy(currentUser);

		Card returned = givenTheCardIsInThisProject(card);

		shouldRemoveTheCardFromRepository(returned);

		whenIRemove(card);

        mockery.assertIsSatisfied();
	}

    @Test
    public void removeACardFromOtherProjectThanMine() throws Exception {
        Card card = givenACard();
        givenTheProjectIsOwnedBy(anyUser());

        Card returned = givenTheCardIsInThisProject(card);

    	shouldNotRemoveTheCardFromRepository(returned);

    	try {
			whenIRemove(card);
			fail("Expected ValidationException");
		} catch (ValidationException e) {
			mockery.assertIsSatisfied();
		}

    }


	@Test
    public void removeACardAndSubcards() throws Exception {
    	Card card = givenACard();
    	givenTheProjectIsOwnedBy(currentUser);

    	Card subcard = givenACard();
    	subcard.setParent(card);

    	Card subsubcard = givenACard();
    	subsubcard.setParent(subcard);
    	
    	Card returned = givenTheCardIsInThisProject(card);
    	givenTheCardHasSubCard(returned, subcard);

    	givenTheCardHasSubCard(subcard, subsubcard);
   	
    	shouldRemoveTheCardFromRepository(returned);
    	shouldRemoveTheCardFromRepository(subcard);
    	shouldRemoveTheCardFromRepository(subsubcard);

    	logic.delete(card, true);

        mockery.assertIsSatisfied();
    }
    private void givenTheCardHasSubCard(final Card returned, final Card subcard) {

		mockery.checking(new Expectations() {
			{
				one(repository).listSubcards(returned);
				will(returnValue(Arrays.asList(subcard)));
			}
		});
	}

    

	@Test
    public void removeACardButNotSubcards() throws Exception {
    	Card card = givenACard();
    	givenTheProjectIsOwnedBy(currentUser);

    	Card subCard = givenACard();
    	subCard.setParent(card);

    	Card returned = givenTheCardIsInThisProject(card);
    	givenTheCardHasSubCard(returned, subCard);

    	shouldRemoveTheCardFromRepository(returned);
    	shouldUpdateTheCardFromRepository(subCard);

    	logic.delete(card, false);

    	assertThat(subCard.getParent(), is(nullValue()));

        mockery.assertIsSatisfied();
    }

    @Test
	public void savingACardWithGadgets() throws Exception {
    	Project project = givenAProject();
    	shouldEventuallyLoadTheProject(project);
		Card card = givenACard();
		card.setName("Log development");

		Gadgets prioritization = Gadgets.PRIORITIZATION;

		shouldSaveOnTheRepositoryTheCard(card);
		shouldSaveAGadgetOfType(prioritization);
		shouldSaveModificationOnRepository("Created card 'Log development'");

		whenISaveTheCard(card, onThe(project), withGadgets(prioritization));

		assertThat(card.getProject(), is(project));
		mockery.assertIsSatisfied();
	}

    @Test
	public void listingCardsShouldIncludeOnlyTodoCards() throws Exception {
		shouldIncludeTodoCards();

		logic.list(project);

		mockery.assertIsSatisfied();
	}
    @Test
    public void listingAllCardsShouldIncludeAllCards() throws Exception {
    	shouldIncludeAllCards();

    	logic.all(project);

    	mockery.assertIsSatisfied();
    }
    @Test
    public void formShouldIncludeAllCardTypes() throws Exception {
    	shouldIncludeAllCardTypes();

    	logic.form(project);

    	mockery.assertIsSatisfied();
    }

    @Test
    public void subcardFormShouldIncludeAllCardTypes() throws Exception {
    	Card card = givenACard();
    	card.setProject(project);

    	shouldIncludeAllCardTypes();

    	logic.subcardForm(card);

    	mockery.assertIsSatisfied();
    }
    @Test
    public void editFormShouldIncludeAllCardTypes() throws Exception {
    	Card card = givenACard();
    	card.setProject(project);

    	shouldIncludeAllCardTypes();

    	logic.edit(card);

    	mockery.assertIsSatisfied();
    }

    @Test
	public void listingSubcardsShouldIncludeSubcardsList() throws Exception {
    	Card card = givenACard();

		shouldIncludeSubcardsList(card);

		logic.listSubcards(card);

		mockery.assertIsSatisfied();
	}

    private void shouldIncludeSubcardsList(final Card card) {

		mockery.checking(new Expectations() {
			{
				one(repository).listSubcards(card);

				ignoring(anything());
			}
		});
	}

	private void shouldIncludeAllCardTypes() {

		mockery.checking(new Expectations() {
			{
				one(projectRepository).listCardTypesFrom(project);
				ignoring(anything());
			}
		});
	}


	private void shouldIncludeAllCards() {

		mockery.checking(new Expectations() {
			{
				one(projectRepository).listRootCardsFrom(project);

				ignoring(anything());
			}
		});
	}


	private void whenISaveTheCard(Card card, Project project,
			List<Gadgets> gadgets) {
    	card.setProject(project);
    	logic.save(card, gadgets);
	}


	private List<Gadgets> withGadgets(Gadgets... gadgets) {
		return Arrays.asList(gadgets);
	}


	private void shouldSaveAGadgetOfType(final Gadgets prioritization) {

		mockery.checking(new Expectations() {
			{
				one(repository).add(with(any(prioritization.gadgetClass())));
			}
		});
	}

	private void shouldIncludeTodoCards() {
		mockery.checking(new Expectations() {
			{
				one(projectRepository).listTodoCardsFrom(project);

				ignoring(anything());
			}
		});
	}

	private Card givenTheCardIsInThisProject(final Card card) {
        mockery.checking(new Expectations() {
            {
            	one(repository).load(card);
            	will(returnValue(card));

                one(projectRepository).load(project);
                will(returnValue(project));
            }
        });
        return card;
    }

    private void givenTheProjectIsOwnedBy(User user) {
        project.setOwner(user);
    }

	private void shouldUpdateTheCardFromRepository(final Card subcard) {
		mockery.checking(new Expectations() {
			{
				one(repository).update(subcard);
			}
		});
	}

	private void shouldNotRemoveTheCardFromRepository(final Card returned) {
		mockery.checking(new Expectations() {
			{
				never(repository).remove(returned);
			}
		});

	}

	private User anyUser() {
		User user = new User();
		user.setLogin("any");
		return user;
	}

	private void shouldRemoveTheCardFromRepository(final Card returned) {

		mockery.checking(new Expectations() {
			{
				one(repository).remove(returned);

				allowing(projectRepository).listTodoCardsFrom(project);

				allowing(repository).listSubcards(returned);
			}
		});
	}

	private void whenIRemove(Card card) {
		logic.delete(card, false);
	}

	private CardTest givenTheCard(Card card) {
		currentCard = card;
		return this;
	}

	private CardTest withName(String cardName) {
		currentCard.setName(cardName);
		return this;
	}

	private CardTest withDescription(String cardDescription) {
		currentCard.setDescription(cardDescription);
		return this;
	}

	private Card shouldLoadTheCard(final Card card) {
		final Card newcard = new Card(repository);

		mockery.checking(new Expectations() {
			{
				allowing(projectRepository);

				one(repository).load(card);
				will(returnValue(newcard));
			}
		});
		return newcard;
	}

	private void shouldUpdateOnTheRepositoryTheCard(final Card card) {
		mockery.checking(new Expectations() {
			{
				allowing(projectRepository);
				one(repository).update(card);
				one(repository).updateGadgets(with(any(Card.class)), with(any(List.class)));
			}
		});
	}

	private String changingNameTo(String cardName) {
		return cardName;
	}

	private String changingDescriptionTo(String newDescription) {
		return newDescription;
	}

	private void whenIEditTheCard(Card card, String newName, String newDescription) {
		card.setName(newName);
		card.setDescription(newDescription);
		logic.update(card, new ArrayList<Gadgets>());
	}

	private void shouldSaveOnTheRepositoryTheCard(final Card card) {
		mockery.checking(new Expectations() {
			{
				allowing(projectRepository);

				allowing(repository).add(card);

			}
		});

	}
	
	private void shouldLoadOnTheRepositoryTheCard(final Card card) {
		mockery.checking(new Expectations() {
			{
				allowing(projectRepository);
				one(repository).load(card);
				will(returnValue(card));

			}
		});

	}
	
	
	private Project onThe(Project project) {
		return project;
	}

	private void whenISaveTheCard(Card card, Project project) {
		card.setProject(project);
		logic.save(card, new ArrayList<Gadgets>());
	}

	private Project givenAProject() {
		Project project2 = new Project(projectRepository, projectModificationsRepository);
		return project2;
	}
	
	private Iteration givenAnIteration() {
		Iteration iteration = new Iteration(iterationRepository);
		return iteration;
	}

	private Card givenACard() {
		Card card = new Card(repository);
		card.setProject(project);
		return card;
	}
}
