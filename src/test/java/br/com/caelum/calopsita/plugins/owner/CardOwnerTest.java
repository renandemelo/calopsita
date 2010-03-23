package br.com.caelum.calopsita.plugins.owner;

import static org.junit.Assert.assertEquals;

import org.hibernate.Session;
import org.jmock.Mockery;
import org.junit.Before;
import org.junit.Test;

import br.com.caelum.calopsita.model.Card;
import br.com.caelum.calopsita.model.Iteration;
import br.com.caelum.calopsita.model.Project;
import br.com.caelum.calopsita.persistence.dao.IterationDao;
import br.com.caelum.calopsita.plugins.PluginResultTransformer;
import br.com.caelum.calopsita.plugins.prioritization.PrioritizationRepository;
import br.com.caelum.calopsita.repository.IterationRepository;
import br.com.caelum.calopsita.repository.ProjectRepository;
import br.com.caelum.vraptor.Result;

public class CardOwnerTest {
	IterationRepository repository;
	private Session session;
	private PluginResultTransformer transformer;
	private Mockery mockery;
	
	@Before
	public void setUp() throws Exception {
		mockery = new Mockery();
		//session = mockery.mock(org.hibernate.classic.Session.class);
		
	}
	
	@Test
	public void listCardsFromIteration() throws Exception {
		Result result = null;
		
		Project project = new Project();
		project.setId(1L);
		
		Iteration iteration = new Iteration(new IterationDao(session, transformer));
		
		iteration.setProject(project);
	
		Card cardInIteration = new Card();
		Card cardInIteration2 = new Card();
		
		CardOwnerController cardOwnerController = new CardOwnerController(result);
		
		iteration.addCard(cardInIteration);
		iteration.addCard(cardInIteration2);
		
		assertEquals(2, cardOwnerController.list(iteration).size());
	}
}
