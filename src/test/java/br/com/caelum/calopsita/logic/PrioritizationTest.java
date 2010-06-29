package br.com.caelum.calopsita.logic;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.Before;
import org.junit.Test;

import br.com.caelum.calopsita.mocks.PrioritizableCardMock;
import br.com.caelum.calopsita.model.Card;
import br.com.caelum.calopsita.model.Project;
import br.com.caelum.calopsita.model.ProjectModification;
import br.com.caelum.calopsita.plugins.prioritization.PrioritizableCard;
import br.com.caelum.calopsita.plugins.prioritization.PrioritizationController;
import br.com.caelum.calopsita.plugins.prioritization.PrioritizationRepository;
import br.com.caelum.calopsita.repository.ProjectModificationRepository;
import br.com.caelum.calopsita.repository.ProjectRepository;
import br.com.caelum.vraptor.util.test.MockResult;

public class PrioritizationTest {
    private Mockery mockery;
    private PrioritizationController logic;
    private PrioritizableCardMock prioritizableCard;
	private PrioritizationRepository repository;
	private ProjectRepository projectRepository;
	private Project project;
	private ProjectModificationRepository modificationRepository;

    @Before
    public void setUp() throws Exception {
        mockery = new Mockery();
        repository = mockery.mock(PrioritizationRepository.class);

		projectRepository = mockery.mock(ProjectRepository.class);
		modificationRepository = mockery.mock(ProjectModificationRepository.class);
		project = new Project(projectRepository, modificationRepository);
		logic = new PrioritizationController(new MockResult(), repository);
		prioritizableCard = new PrioritizableCardMock();

		mockery.checking(new Expectations() {
			{
				allowing(projectRepository).load(project);
				will(returnValue(project));

				ignoring(repository).listCards(project);
				
				allowing(modificationRepository).add(with(any(ProjectModification.class)));
				
				allowing(projectRepository).listModificationsFrom(project);
			}
		});
    }

    @Test
	public void prioritizingCards() throws Exception {
		PrioritizableCard card = givenACard(withPriority(5));

		shouldLoadFromRepository(card);

		logic.prioritize(project, Arrays.asList(card));
		
		assertEquals(5,prioritizableCard.getChangedPriority());
		mockery.assertIsSatisfied();

	}

	private void shouldLoadFromRepository(final PrioritizableCard card) {
		mockery.checking(new Expectations() {
			{
				one(repository).load(card);
				will(returnValue(prioritizableCard));
			}
		});
	}

	private PrioritizableCard givenACard(int priority) {
		PrioritizableCard card = new PrioritizableCard();
		Card card2 = new Card();
		card2.setProject(project);
		card.setCard(card2);
		card.setPriority(priority);
		return card;
	}

	private int withPriority(int i) {
		return i;
	}
}
