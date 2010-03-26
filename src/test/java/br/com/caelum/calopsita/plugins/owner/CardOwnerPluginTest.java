package br.com.caelum.calopsita.plugins.owner;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import org.hibernate.Session;
import org.hibernate.validator.AssertFalse;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.Before;
import org.junit.Test;

import br.com.caelum.calopsita.model.Card;
import br.com.caelum.calopsita.model.Iteration;
import br.com.caelum.calopsita.model.Menu;
import br.com.caelum.calopsita.model.Parameters;
import br.com.caelum.calopsita.model.Project;
import br.com.caelum.calopsita.persistence.dao.IterationDao;
import br.com.caelum.calopsita.plugins.PluginResultTransformer;
import br.com.caelum.calopsita.plugins.prioritization.PrioritizationPlugin;
import br.com.caelum.calopsita.plugins.prioritization.PrioritizationRepository;
import br.com.caelum.calopsita.repository.IterationRepository;
import br.com.caelum.calopsita.repository.ProjectRepository;
import br.com.caelum.iogi.util.Assert;
import br.com.caelum.vraptor.Result;

public class CardOwnerPluginTest {
	IterationRepository repository;
	private Session session;
	private PluginResultTransformer transformer;
	private Mockery mockery;
	
	private Parameters parameters;
	private CardOwnerPlugin menus;
	
	@Before
	public void setUp() throws Exception {
		mockery = new Mockery();

		parameters = mockery.mock(Parameters.class);
		
		menus = new CardOwnerPlugin();
		
		
	}
	
	@Test
	public void listCardsFromIteration() throws Exception {
		org.junit.Assert.assertTrue(false);
	}
	
	@Test
	public void shouldNotIncludePluginMenusWhenThereIsNoProject() throws Exception {
		givenThereIsNoProject();

		Menu menu = givenAMenu();
		menus.includeMenus(menu, parameters);

		assertThat(menu.toString(), not(containsString("/projects")));
		mockery.assertIsSatisfied();
	}

	@Test
	public void whenThereIsAnIterationCreatePluginMenus() throws Exception {
		givenThereIsAProjectWithId(5l);
		givenThereIsAnIterationWithId(9l);
		givenAnythingElseIsIgnored();

		Menu menu = givenAMenu();

		menus.includeMenus(menu, parameters);

		assertThat(menu.toString(), containsString("/projects/5/iterations/9/cardOwner"));
		mockery.assertIsSatisfied();
	}

	private Menu givenAMenu() {
		return new Menu("path");
	}
	private void givenThereIsNoProject() {

		mockery.checking(new Expectations() {
			{
				one(parameters).contains("project");
				will(returnValue(false));
			}
		});
	}

	private void givenThereIsAnIterationWithId(final Long id) {
		mockery.checking(new Expectations() {
			{
				Iteration iteration = new Iteration();
				iteration.setId(id);

				one(parameters).contains("iteration");
				will(returnValue(true));

				one(parameters).get("iteration");
				will(returnValue(iteration));

			}
		});
	}

	private void givenThereIsAProjectWithId(final Long id) {
		mockery.checking(new Expectations() {
			{
				Project project = new Project();
				project.setId(id);

				one(parameters).contains("project");
				will(returnValue(true));

				one(parameters).get("project");
				will(returnValue(project));

			}
		});
	}
	private void givenAnythingElseIsIgnored() {
		mockery.checking(new Expectations() {
			{
				ignoring(anything());
			}
		});
	}

}
