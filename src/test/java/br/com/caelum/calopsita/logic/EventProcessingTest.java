package br.com.caelum.calopsita.logic;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;

import br.com.caelum.calopsita.controller.IterationsController;
import br.com.caelum.calopsita.infra.vraptor.SessionUser;
import br.com.caelum.calopsita.mocks.MockHttpSession;
import br.com.caelum.calopsita.model.Card;
import br.com.caelum.calopsita.model.Event;
import br.com.caelum.calopsita.model.Gadget;
import br.com.caelum.calopsita.model.Iteration;
import br.com.caelum.calopsita.model.Project;
import br.com.caelum.calopsita.model.User;
import br.com.caelum.calopsita.model.Card.Status;
import br.com.caelum.calopsita.repository.CardRepository;
import br.com.caelum.calopsita.repository.IterationRepository;
import br.com.caelum.calopsita.repository.ProjectModificationRepository;
import br.com.caelum.calopsita.repository.ProjectRepository;
import br.com.caelum.calopsita.repository.UserRepository;
import br.com.caelum.vraptor.util.test.MockResult;
import br.com.caelum.vraptor.util.test.MockValidator;


public class EventProcessingTest {
    private Mockery mockery;
    private IterationsController logic;
    private IterationRepository iterationRepository;
    private CardRepository cardRepository;
    private Project project;
    private ProjectRepository projectRepository;
	private User currentUser;
	private UserRepository userRepository;
	private Gadget gadget;
	private ProjectModificationRepository modificationRepository;

    @Before
    public void setUp() throws Exception {
        mockery = new Mockery();
        iterationRepository = mockery.mock(IterationRepository.class);
        cardRepository = mockery.mock(CardRepository.class);
        projectRepository = mockery.mock(ProjectRepository.class);
        userRepository = mockery.mock(UserRepository.class);
        modificationRepository = mockery.mock(ProjectModificationRepository.class);
        gadget = mockery.mock(Gadget.class);
        
        
        currentUser = new User(userRepository);
        currentUser.setLogin("me");
        project = new Project(projectRepository, modificationRepository);

        SessionUser sessionUser = new SessionUser(new MockHttpSession());
        sessionUser.setUser(currentUser);
		logic = new IterationsController(new MockResult(), new MockValidator(), sessionUser);

    }

    @Test
	public void addingACardInAnIteration() throws Exception {
		Iteration iteration = givenAnIteration();
		Card card = givenACard();	

		shouldUpdateTheCard(card);
		
		whenIAddTheCardToIteration(card, iteration);
		whenISetTheCardAsDone(card,iteration);		

		assertThat(card.getIteration(), is(iteration));
		mockery.assertIsSatisfied();
	}
    
    

	private void whenISetTheCardAsDone(Card card, Iteration iteration) {
    	card.setStatus(Status.DONE);
    	logic.updateCards(iteration, Arrays.asList(card));
	}

	private void shouldUpdateTheCard(final Card card) {

		mockery.checking(new Expectations() {
			{
				one(cardRepository).update(card);
				
				exactly(2).of(cardRepository).load(card);
				will(returnValue(card));
				allowing(cardRepository).listGadgets(card);
				List<Gadget> gadgetList = Arrays.asList(new Gadget[]{gadget});
				will(returnValue(gadgetList));				
				
				exactly(1).of(gadget).processEvent(Event.END);
				
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

	private Iteration givenAnIteration() {
        Iteration iteration = new Iteration(iterationRepository);
        iteration.setProject(project);
		return iteration;
    }
}
