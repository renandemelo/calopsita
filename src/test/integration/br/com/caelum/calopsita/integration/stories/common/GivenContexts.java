package br.com.caelum.calopsita.integration.stories.common;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.Arrays;

import org.hibernate.Session;

import br.com.caelum.calopsita.model.Project;
import br.com.caelum.calopsita.model.User;
import br.com.caelum.calopsita.persistence.dao.ProjectDao;
import br.com.caelum.calopsita.persistence.dao.ProjectModificationDao;
import br.com.caelum.calopsita.plugins.PluginResultTransformer;
import br.com.caelum.calopsita.plugins.Transformer;
import br.com.caelum.calopsita.plugins.prioritization.OrderByPriorityTransformer;
import br.com.caelum.seleniumdsl.Browser;
import br.com.caelum.seleniumdsl.ContentTag;
import br.com.caelum.seleniumdsl.Form;
import br.com.caelum.seleniumdsl.Page;

public class GivenContexts {

	private final Browser browser;
	private final Session session;

	public GivenContexts(Browser browser, Session session) {
		this.browser = browser;
		this.session = session;
	}

	public void iAmOnTheRootPage() {
		browser.open("/calopsita");
	}

	public GivenContexts thereIsAnUserNamed(String login, String name) {
		User user = new User();
		user.setLogin(login);
		user.setEmail(login + "@caelum.com.br");
		user.setName(name);
		user.setPassword(login);
		session.saveOrUpdate(user);
		session.flush();
		return this;
	}

	public GivenContexts thereIsAnUserNamed(String login) {
		thereIsAnUserNamed(login, login);
		return this;
	}

	public GivenContexts and() {
		return this;
	}

	public void iAmLoggedInAs(String login) {
		iAmOnTheRootPage();
		Page currentPage = browser.currentPage();
		Form form = currentPage.form("form");
		form.field("user.login").type(login);
		form.field("user.password").type(login);
		form.submit();

		assertThat(browser.currentPage().div("user").innerHTML(),
				containsString(login));
	}

	public void iAmNotLogged() {
		iAmOnTheRootPage();
		ContentTag div = browser.currentPage().div("user");
		if (div.exists() && div.contains("Logout")) {
			browser.currentPage().click("link=Logout");
		}
	}

	@SuppressWarnings("unchecked")
	public ProjectContexts<?> thereIsAProjectNamed(String name) {
		Project project = new Project(
				new ProjectDao(session, new PluginResultTransformer(session, new ArrayList<Transformer>())),
				new ProjectModificationDao(session, new PluginResultTransformer(session, new ArrayList<Transformer>())));
		project.setId(1L);
		project.setDescription(name);
		project.setName(name);
		session.save(project);
		session.flush();
		return new ProjectContexts(project, session, browser);
	}

	public void theUserDoesntExist(String name) {
		session.createQuery("delete from User u where u.name = :name")
				.setParameter("name", name).executeUpdate();
	}

}
