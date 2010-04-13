package br.com.caelum.calopsita.plugins.owner;

import static br.com.caelum.vraptor.view.Results.nothing;
import static br.com.caelum.vraptor.view.Results.page;
import static br.com.caelum.vraptor.view.Results.logic;

import java.util.List;

import org.joda.time.LocalDate;

import br.com.caelum.calopsita.controller.CardsController;
import br.com.caelum.calopsita.infra.vraptor.SessionUser;
import br.com.caelum.calopsita.model.Card;
import br.com.caelum.calopsita.model.Iteration;
import br.com.caelum.calopsita.model.User;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;

@Resource
public class CardOwnerController {
	private final Result result;
	private final SessionUser sessionUser;
	private final AssignableCardRepository repository;

	public CardOwnerController(Result result, SessionUser user,
			AssignableCardRepository repository) {
		this.result = result;
		this.sessionUser = user;
		this.repository = repository;
	}

	@Path("/projects/{iteration.project.id}/iterations/{iteration.id}/cardOwner/")
	@Get
	public List<Card> list(Iteration iteration) {
		result.include("iteration", iteration.load());
		result.include("project", iteration.getProject().load());
		result.include("iterationCards", iteration.getCards());
		result.include("today", new LocalDate());
		return iteration.getCards();
	}

	@Path("/projects/{iteration.project.id}/iterations/{card.iteration.id}/card/{card.id}/cardOwner/")
	@Get
	public void assign(Iteration iteration, Card card) {
		AssignableCard assignableCard = card.getGadget(AssignableCard.class);
		assignableCard.setOwner(sessionUser.getUser());
		System.out.println(sessionUser.getUser().getName());
		System.out.println(iteration.getId());
		repository.save(assignableCard);
		this.result.use(logic()).redirectTo(CardOwnerController.class).list(iteration);
		
	}
}
