package br.com.caelum.calopsita.persistence.dao;

import static br.com.caelum.calopsita.CustomMatchers.hasSameId;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.*;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;

import br.com.caelum.calopsita.model.Card;
import br.com.caelum.calopsita.model.Iteration;
import br.com.caelum.calopsita.model.Project;
import br.com.caelum.calopsita.model.User;
import br.com.caelum.calopsita.plugins.PluginResultTransformer;
import br.com.caelum.calopsita.plugins.Transformer;
import br.com.caelum.calopsita.plugins.owner.AssignableCard;

public class UserDaoTest extends AbstractDaoTest {

	private UserDao dao;

	@Override
	@Before
	public void setUp() throws Exception {
		super.setUp();
		dao = new UserDao(session);
	}

	@Test
	public void listProjectIfUserIsTheOwner() throws Exception {
		User user = givenAUser();
		Project project = givenAProjectOwnedBy(user);

		List<Project> list = dao.listAllFrom(user);

		assertThat(list, hasItem(hasSameId(project)));
	}

	@Test
	public void listProjectIfUserIsAColaborator() throws Exception {
		User user = givenAUser();
		Project project = givenAProjectWithColaborator(user);

		List<Project> list = dao.listAllFrom(user);

		assertThat(list, hasItem(hasSameId(project)));
	}

	@Test
	public void dontListProjectIfUserIsUnrelated() throws Exception {
		User user = givenAUser();
		Project project = givenAProject();

		List<Project> list = dao.listAllFrom(user);

		assertThat(list, not(hasItem(hasSameId(project))));
	}
	
	@Test
	public void userOwnsACardInCurrentIteration() throws Exception {
		User juju = givenAUserNamed("Juju");
		givenAnAssignableCardWithOwnerInCurrentIteration(juju);
		
		assertTrue(dao.isCardOwner(juju));
	}
	
	@Test
	public void userOwnsACardNotInCurrentIteration() throws Exception {
		User juju = givenAUserNamed("Juju");
		givenAnAssignableCardWithOwnerNotInCurrentIteration(juju);
		
		assertFalse(dao.isCardOwner(juju));
	}
	
	@Test
	public void userDoesntOwnACard() throws Exception {
		User juju = givenAUserNamed("Juju");
		
		assertFalse(dao.isCardOwner(juju));
	}

	private Project givenAProjectWithColaborator(User user) {
		Project project = givenAProject();
		project.getColaborators().add(user);
		session.update(project);
		session.flush();
		return project;
	}

	private User givenAUser() {
		User user = new User(dao);
		user.setLogin("test");
		user.setPassword("test");
		user.setName("User test");
		user.setEmail("test@caelum.com.br");
		session.save(user);
		session.flush();
		return user;
	}

	private User givenAUserNamed(String name) {
		User user = new User(dao);
		user.setLogin(name);
		user.setPassword(name);
		user.setName(name);
		user.setEmail(name + "@caelum.com.br");
		session.save(user);
		session.flush();
		return user;
	}
	
	private Project givenAProjectOwnedBy(User user) {
		Project project = givenAProject();
		project.setOwner(user);
		session.update(project);
		session.flush();
		return project;
	}

	private Project givenAProject() {
		Project project = new Project(new ProjectDao(session, new PluginResultTransformer(session, new ArrayList<Transformer>())));
		project.setName("Tuba");
		session.save(project);
		session.flush();
		return project;
	}
	
	private AssignableCard givenAnAssignableCardWithOwnerInCurrentIteration(User owner){
		AssignableCard ac = new AssignableCard();
		Card card = new Card(new CardDao(session, new PluginResultTransformer(session, new ArrayList<Transformer>())));
		session.save(card);
		Iteration currentIteration = new Iteration(new IterationDao(session, new PluginResultTransformer(session, new ArrayList<Transformer>())));
		currentIteration.setStartDate(new LocalDate().minusDays(1));
		currentIteration.setEndDate(new LocalDate().plusDays(1));
		session.save(currentIteration);
		card.setIteration(currentIteration);
		ac.setCard(card);
		ac.setOwner(owner);
		session.save(ac);
		session.flush();
		return ac;
	}

	private AssignableCard givenAnAssignableCardWithOwnerNotInCurrentIteration(User owner){
		AssignableCard ac = new AssignableCard();
		Card card = new Card(new CardDao(session, new PluginResultTransformer(session, new ArrayList<Transformer>())));
		session.save(card);
		Iteration currentIteration = new Iteration(new IterationDao(session, new PluginResultTransformer(session, new ArrayList<Transformer>())));
		currentIteration.setStartDate(new LocalDate().minusDays(3));
		currentIteration.setEndDate(new LocalDate().minusDays(2));
		session.save(currentIteration);
		card.setIteration(currentIteration);
		ac.setCard(card);
		ac.setOwner(owner);
		session.save(ac);
		session.flush();
		return ac;
	}
	
}
