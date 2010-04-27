package br.com.caelum.calopsita.integration.stories.common;

import java.util.Arrays;
import java.util.Calendar;

import org.hibernate.Session;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import br.com.caelum.calopsita.model.Card;
import br.com.caelum.calopsita.model.CardType;
import br.com.caelum.calopsita.model.Gadgets;
import br.com.caelum.calopsita.model.Iteration;
import br.com.caelum.calopsita.model.Project;
import br.com.caelum.calopsita.model.User;
import br.com.caelum.calopsita.persistence.dao.UserDao;
import br.com.caelum.seleniumdsl.Browser;

public class ProjectContexts<T extends ProjectContexts<T>> extends GivenContexts {

	private final Session session;
	private final Project project;
	private Iteration iteration;
	private final Browser browser;
	private CardType cardType;

	public ProjectContexts(Project project, Session session, Browser browser) {
		super(browser, session);
		this.project = project;
		this.session = session;
		this.browser = browser;
	}

	public ProjectContexts(Project project, Iteration iteration, Session session, Browser browser) {
		super(browser, session);
		this.project = project;
		this.session = session;
		this.browser = browser;
		this.iteration = iteration;
		
	}
	
	@Override
	public GivenContexts and() {
    	return new GivenContexts(browser, session);
    }

	public T ownedBy(String login) {
        User user = new UserDao(session).find(login);
        project.setOwner(user);
        session.save(user);
        session.flush();
        return (T) this;
    }

	public T withColaborator(String login) {
		User user = new UserDao(session).find(login);
        project.getColaborators().add(user);
        session.flush();
        return (T) this;
	}

	public IterationContexts withAnIterationWhichGoalIs(String goal) {
		Iteration iteration = new Iteration();
		iteration.setGoal(goal);
		iteration.setProject(project);
		session.save(iteration);
		session.flush();
		return new IterationContexts(iteration, session, browser);
	}
	
	public IterationContexts withACurrentIterationWhichGoalIs(String goal) {
		Iteration iteration = new Iteration();
		iteration.setGoal(goal);
		iteration.setProject(project);
		iteration.setStartDate(new LocalDate());
		iteration.setEndDate(new LocalDate());
		this.iteration=iteration;
		session.save(iteration);
		session.flush();
		return new IterationContexts(iteration, session, browser);
	}

	public CardContexts<T> withACardNamed(String cardName) {
		Card card = new Card();
		card.setName(cardName);
		card.setProject(project);
		session.save(card);
		session.flush();
		return new CardContexts<T>(card, session, browser, (T) this);
	}

	
	public T whichDescriptionIs(String description) {
		project.setDescription(description);
		session.flush();
		return (T) this;
	}

	public T withACardTypeNamed(String name) {
		cardType = new CardType();
		cardType.setName(name);
		cardType.setProject(project);
		session.save(cardType);
		session.flush();
		return (T) this;
	}

	public T withGadgets(Gadgets... gadgets) {
		cardType.setGadgets(Arrays.asList(gadgets));
		session.flush();
		return (T) this;
	}
}
