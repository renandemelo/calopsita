package br.com.caelum.calopsita.logic;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

import java.util.Arrays;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;

import br.com.caelum.calopsita.controller.IterationsController;
import br.com.caelum.calopsita.infra.vraptor.SessionUser;
import br.com.caelum.calopsita.mocks.MockHttpSession;
import br.com.caelum.calopsita.model.Card;
import br.com.caelum.calopsita.model.Iteration;
import br.com.caelum.calopsita.model.Project;
import br.com.caelum.calopsita.model.User;
import br.com.caelum.calopsita.repository.CardRepository;
import br.com.caelum.calopsita.repository.IterationRepository;
import br.com.caelum.calopsita.repository.ProjectModificationRepository;
import br.com.caelum.calopsita.repository.ProjectRepository;
import br.com.caelum.calopsita.repository.UserRepository;
import br.com.caelum.vraptor.util.test.MockResult;
import br.com.caelum.vraptor.util.test.MockValidator;
import br.com.caelum.vraptor.validator.ValidationException;


public class IterationTest {
    private Mockery mockery;
    private IterationsController logic;
    private IterationRepository iterationRepository;
    private CardRepository cardRepository;
    private Project project;
    private ProjectRepository projectRepository;
	private User currentUser;
	private UserRepository userRepository;
	private ProjectModificationRepository modificationRepository;

    @Before
    public void setUp() throws Exception {
        mockery = new Mockery();
        iterationRepository = mockery.mock(IterationRepository.class);
        cardRepository = mockery.mock(CardRepository.class);
        projectRepository = mockery.mock(ProjectRepository.class);
        userRepository = mockery.mock(UserRepository.class);
        modificationRepository = mockery.mock(ProjectModificationRepository.class);

        currentUser = new User(userRepository);
        currentUser.setLogin("me");
        project = new Project(projectRepository, modificationRepository);

        SessionUser sessionUser = new SessionUser(new MockHttpSession());
        sessionUser.setUser(currentUser);
		logic = new IterationsController(new MockResult(), new MockValidator(), sessionUser);

    }


    @Test
    public void savingAnIteration() throws Exception {
        Iteration iteration = givenAnIteration();

        shouldSaveOnTheRepositoryTheIteration(iteration);

        whenISaveTheIteration(iteration);

        mockery.assertIsSatisfied();
    }

    @Test
	public void addingACardInAnIteration() throws Exception {
		Iteration iteration = givenAnIteration();
		Card card = givenACard();

		shouldUpdateTheCard(card);

		whenIAddTheCardToIteration(card, iteration);

		assertThat(card.getIteration(), is(iteration));
		mockery.assertIsSatisfied();
	}
    @Test
    public void removingACardOfAnIteration() throws Exception {

    	Iteration iteration = givenAnIteration();
    	Card card = givenACard();

    	Card loaded = givenLoadedCardContainsIteration(card, iteration);

    	
    	
    	whenIRemoveTheCardOfIteration(card, iteration);

    	assertThat(loaded.getIteration(), is(nullValue()));
    	mockery.assertIsSatisfied();
    }

    @Test(expected=ValidationException.class)
    public void removeAnIterationFromOtherProject() throws Exception {
        Iteration iteration = givenAnIteration();
        givenTheProjectIsOwnedBy(anyUser());
        Iteration returned = givenTheIterationIsInThisProject(iteration);

        shouldNotRemoveTheIterationFromRepository(returned);

        whenIRemove(iteration);
        mockery.assertIsSatisfied();
    }

    @Test
    public void removeAnIterationFromMyProject() throws Exception {
        Iteration iteration = givenAnIteration();
        givenTheProjectIsOwnedBy(currentUser);

        Card card = givenACard();
        Iteration returnedIteration = givenTheIterationIsInThisProject(iteration);

        givenTheIterationHasThisCard(card, returnedIteration);

        shouldRemoveTheIterationFromRepository(returnedIteration);

        whenIRemove(iteration);

        assertThat(card.getIteration(), is(nullValue()));
        mockery.assertIsSatisfied();
    }

    @Test(expected=ValidationException.class)
    public void validatingDateOnSave() throws Exception {
        Iteration iteration = givenAnIteration();
        iteration.setStartDate(new LocalDate(2005,10,1));
        iteration.setEndDate(new LocalDate(2005,8,1));

        whenISaveTheIteration(iteration);
        //should throw exception
    }

    @Test
    public void startingAnIteration() throws Exception {
        Iteration iteration = givenAnIteration();

        Iteration loaded = givenTheIterationHaveNoStartDate(iteration);

        whenIStartTheIteration(iteration);

        assertThat(loaded.getStartDate(), is(today()));
        mockery.assertIsSatisfied();
    }
    @Test(expected=IllegalArgumentException.class)
    public void startingAnIterationAlreadyStarted() throws Exception {
        Iteration iteration = givenAnIteration();

        givenTheIterationAlreadyStarted(iteration);

        whenIStartTheIteration(iteration);
    }

    @Test
    public void endingAnIteration() throws Exception {
        Iteration iteration = givenAnIteration();

        Iteration loaded = givenTheIterationStartedYesterday(iteration);

        whenIEndTheIteration(iteration);

        assertThat(loaded.getEndDate(), is(today()));
        mockery.assertIsSatisfied();
    }


    @Test(expected=IllegalArgumentException.class)
    public void endingAnIterationAlreadyStarted() throws Exception {
        Iteration iteration = givenAnIteration();

        givenTheIterationNotStarted(iteration);

        whenIEndTheIteration(iteration);
    }

    private Iteration givenTheIterationNotStarted(final Iteration iteration) {
        final Iteration result = new Iteration(iterationRepository);
        mockery.checking(new Expectations() {
            {
                one(iterationRepository).load(iteration);
                will(returnValue(result));
            }
        });
        return result;
    }

    @Test
	public void editingAnIteration() throws Exception {
		Iteration iteration = givenAnIteration();

		Iteration loaded = shouldLoadFromRepository(iteration);

		iteration.setGoal("Altered goal");
		iteration.setStartDate(today());
		iteration.setEndDate(tomorrow());

		logic.update(iteration);


		assertThat(loaded.getGoal(), is("Altered goal"));
		assertThat(loaded.getStartDate(), is(today()));
		assertThat(loaded.getEndDate(), is(tomorrow()));

		mockery.assertIsSatisfied();

	}

    @Test
	public void editingIterationShouldLoadItsFields() throws Exception {
		Iteration iteration = givenAnIteration();
		shouldLoadIteration(iteration);

		logic.edit(iteration);

		mockery.assertIsSatisfied();
	}
    @Test
    public void formShouldIncludeLoadedProject() throws Exception {
    	Project project = givenAProject();

    	shouldLoadProject(project);

    	logic.form(project);

    	mockery.assertIsSatisfied();
    }
    @Test
    public void currentShouldLoadCurrentIteration() throws Exception {
    	Project project = givenAProject();

    	shouldLoadCurrentIteration(project);

    	logic.current(project);

    	mockery.assertIsSatisfied();
    }
    @Test
    public void shouldListIterationsOfAProject() throws Exception {
    	Project project = givenAProject();

    	shouldListIterations(project);

    	logic.list(project);

    	mockery.assertIsSatisfied();
    }
    @Test
    public void showingIterationsShouldListBacklogCards() throws Exception {
    	Iteration iteration = givenAnIteration();
    	iteration.setProject(project);

    	shouldListBacklogCards(iteration);

    	logic.show(iteration);

    	mockery.assertIsSatisfied();
    }

    private void shouldListBacklogCards(final Iteration iteration) {
    	mockery.checking(new Expectations() {
    		{
    			one(projectRepository).planningCardsWithoutIteration(iteration.getProject());
    			ignoring(anything());
    		}
    	});

	}


	private void shouldListIterations(final Project project2) {

		mockery.checking(new Expectations() {
			{
				one(projectRepository).listIterationsFrom(project2);
				ignoring(anything());
			}
		});
	}


	private void shouldLoadCurrentIteration(final Project project2) {

		mockery.checking(new Expectations() {
			{
				one(projectRepository).getCurrentIterationFromProject(project2);
				ignoring(anything());
			}
		});
	}


	private void shouldLoadProject(final Project project2) {

		mockery.checking(new Expectations() {
			{
				one(projectRepository).load(project2);
			}
		});
	}


	private Project givenAProject() {
		return new Project(projectRepository, modificationRepository);
	}


	private void shouldLoadIteration(final Iteration iteration) {

		mockery.checking(new Expectations() {
			{
				one(iterationRepository).load(iteration);

				ignoring(anything());
			}
		});
	}


	private LocalDate tomorrow() {
		return new LocalDate().plusDays(1);
	}


	private LocalDate today() {
		return new LocalDate();
	}


	private Iteration shouldLoadFromRepository(final Iteration iteration) {
    	final Iteration result = new Iteration(iterationRepository);


		mockery.checking(new Expectations() {
			{
				one(iterationRepository).load(iteration);
				will(returnValue(result));
			}
		});
    	return result;
	}


	private Iteration givenTheIterationAlreadyStarted(final Iteration iteration) {
    	final Iteration result = new Iteration(iterationRepository);
    	result.setStartDate(today().minusDays(1));
		mockery.checking(new Expectations() {
			{
				one(iterationRepository).load(iteration);
				will(returnValue(result));
			}
		});
    	return result;
	}


	private void whenIStartTheIteration(Iteration iteration) {
    	logic.start(iteration);
	}

	private void whenIEndTheIteration(Iteration iteration) {
	    logic.end(iteration);
	}

	private Iteration givenTheIterationHaveNoStartDate(final Iteration iteration) {
    	final Iteration result = new Iteration(iterationRepository);

		mockery.checking(new Expectations() {
			{
				one(iterationRepository).load(iteration);
				will(returnValue(result));
			}
		});
    	return result;
	}

	private Iteration givenTheIterationStartedYesterday(final Iteration iteration) {
	    final Iteration result = new Iteration(iterationRepository);
        result.setStartDate(today().minusDays(1));
        mockery.checking(new Expectations() {
            {
                one(iterationRepository).load(iteration);
                will(returnValue(result));
            }
        });
        return result;
	}

	private void givenTheIterationHasThisCard(final Card card, final Iteration returnedIteration) {
		mockery.checking(new Expectations() {
			{
				one(cardRepository).listSubcards(card);
				one(iterationRepository).listCards(returnedIteration);
				will(returnValue(Arrays.asList(card)));
			}
		});
		
		returnedIteration.addCard(card);
        card.setIteration(returnedIteration);		
    }

    private void givenTheProjectIsOwnedBy(User user) {
	    project.setOwner(user);
    }

    private void shouldNotRemoveTheIterationFromRepository(final Iteration returned) {
	    mockery.checking(new Expectations() {
            {
                never(iterationRepository).remove(returned);
            }
        });
    }

    private User anyUser() {
	    User user = new User(userRepository);
	    user.setName("any name");
	    return user;
    }

    private void whenIRemove(Iteration iteration) {
	    logic.delete(iteration);
    }

    private void shouldRemoveTheIterationFromRepository(final Iteration returned) {
	    mockery.checking(new Expectations() {
            {
                one(iterationRepository).remove(returned);
                allowing(iterationRepository).listCards(returned);
            }
        });
    }

    private Iteration givenTheIterationIsInThisProject(final Iteration iteration) {
	    final Iteration returned = new Iteration(iterationRepository);
        returned.setProject(this.project);

        mockery.checking(new Expectations() {
            {

                one(iterationRepository).load(iteration);
                will(returnValue(returned));
            }
        });
        return returned;
    }

    private void whenIRemoveTheCardOfIteration(Card card, Iteration iteration) {
		logic.removeCards(iteration, Arrays.asList(card));
	}

	private Card givenLoadedCardContainsIteration(final Card card, final Iteration iteration) {
		final Card loaded = new Card(cardRepository);

		mockery.checking(new Expectations() {
			{
				one(cardRepository).load(card);
				will(returnValue(loaded));

				one(cardRepository).update(loaded);

				allowing(iterationRepository).listCards(with(any(Iteration.class)));
				allowing(projectRepository).planningCardsWithoutIteration(with(any(Project.class)));
				allowing(iterationRepository).load(iteration);
				will(returnValue(iteration));

				ignoring(anything());
			}
		});
		return loaded;
	}

	private void shouldUpdateTheCard(final Card card) {

		mockery.checking(new Expectations() {
			{
				one(cardRepository).update(card);

				one(cardRepository).load(card);
				will(returnValue(card));

				allowing(iterationRepository).listCards(with(any(Iteration.class)));
				allowing(projectRepository).planningCardsWithoutIteration(with(any(Project.class)));
				allowing(iterationRepository).load(with(any(Iteration.class)));
				will(returnValue(new Iteration(iterationRepository)));
				ignoring(anything());
			}
		});
	}

	private void whenIAddTheCardToIteration(Card card, Iteration iteration) {
    	logic.updateCards(iteration, Arrays.asList(card));
	}

	private Card givenACard() {
		return new Card(cardRepository);
	}

	private void shouldSaveOnTheRepositoryTheIteration(final Iteration iteration) {

        mockery.checking(new Expectations() {
            {
                one(iterationRepository).add(iteration);
            }
        });
    }


    private void whenISaveTheIteration(Iteration iteration) {
        logic.save(iteration);
    }

    private Iteration givenAnIteration() {
        Iteration iteration = new Iteration(iterationRepository);
        iteration.setProject(project);
		return iteration;
    }
}
