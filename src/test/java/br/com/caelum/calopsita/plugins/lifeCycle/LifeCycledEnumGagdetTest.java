package br.com.caelum.calopsita.plugins.lifeCycle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jmock.Expectations;
import org.joda.time.LocalDate;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import br.com.caelum.calopsita.model.Card;
import br.com.caelum.calopsita.model.CardType;
import br.com.caelum.calopsita.model.Gadgets;
import br.com.caelum.calopsita.model.Iteration;
import br.com.caelum.calopsita.model.Project;
import br.com.caelum.calopsita.model.ProjectModification;
import br.com.caelum.calopsita.model.User;
import br.com.caelum.calopsita.persistence.dao.AbstractDaoTest;
import br.com.caelum.calopsita.persistence.dao.CardDao;
import br.com.caelum.calopsita.persistence.dao.ProjectDao;
import br.com.caelum.calopsita.persistence.dao.ProjectModificationDao;
import br.com.caelum.calopsita.plugins.PluginResultTransformer;
import br.com.caelum.calopsita.plugins.Transformer;
import br.com.caelum.calopsita.plugins.prioritization.OrderByPriorityTransformer;
import br.com.caelum.calopsita.repository.ProjectModificationRepository;
import br.com.caelum.calopsita.repository.ProjectRepository;

public class LifeCycledEnumGagdetTest extends AbstractDaoTest{

	private Card card;
	private ProjectModificationRepository modificationsRepository;
	private ProjectRepository projectRepository;
	private Project project;	
	
	@Before
	public void setUp() throws Exception {
		super.setUp();

		this.modificationsRepository = new ProjectModificationDao(session, PluginResultTransformer());
		this.projectRepository = new ProjectDao(session, PluginResultTransformer());
		
		this.project = new Project(this.projectRepository, this.modificationsRepository);
		this.card = new Card(new CardDao(session, PluginResultTransformer()));
				
		this.project.setName("Nominho");
		this.project.save();
		
		this.card.setProject(project);
		this.card.save();
		session.flush();
	}

	private PluginResultTransformer PluginResultTransformer() {
		return new PluginResultTransformer(session, Arrays.<Transformer>asList(new OrderByPriorityTransformer()));
	}
	
	@Test
	public void testCreationOfLifeCycledCard() throws Exception {
		List<Gadgets> gadgets = Arrays.asList(Gadgets.LIFE_CYCLE);
		card.addGadgets(gadgets);
		LifeCycledCard lifeCycledCard = (LifeCycledCard) card.getGadgets().get(0);
		Assert.assertNotNull(lifeCycledCard.getCreationDate());
		Assert.assertEquals(new LocalDate(), lifeCycledCard.getCreationDate());
	}
	
	
	
}
