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
import br.com.caelum.calopsita.model.Project;
import br.com.caelum.calopsita.model.User;
import br.com.caelum.vraptor.Delete;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

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

	@Path("/projects/{card.project.id}/iterations/{card.iteration.id}/card/{card.id}/cardOwner/")
	@Get
	public void assign(Card card) {
		Card loaded = card.load();
		AssignableCard assignableCard = loaded.getGadget(AssignableCard.class);
		assignableCard.setOwner(sessionUser.getUser());
		repository.save(assignableCard);
		result.use(logic()).redirectTo(CardOwnerController.class).list(loaded.getIteration());
		
	}
	
	@Path("/projects/{card.project.id}/iterations/{card.iteration.id}/card/{card.id}/cardOwner/")
	@Post
	public void isAlreadyOwner(Card card, Project project) {
		List<Card> cardList = repository.listAllCardsFrom(project, sessionUser.getUser());
		boolean isAlreadyOwner = cardList.size() > 0; 
		System.out.println(cardList);
		result.use(Results.json()).from(isAlreadyOwner).serialize();
	}
}
