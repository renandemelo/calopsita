package br.com.caelum.calopsita.plugins.owner;

import java.util.List;

import org.joda.time.LocalDate;

import br.com.caelum.calopsita.model.Card;
import br.com.caelum.calopsita.model.Iteration;
import br.com.caelum.calopsita.plugins.prioritization.PrioritizationRepository;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;

@Resource
public class CardOwnerController {
	private final Result result;

	public CardOwnerController(Result result) {
		this.result = result;
	}

	@Path("/projects/{iteration.project.id}/iterations/{iteration.id}/cardOwner/") @Get
    public List<Card> list(Iteration iteration) {
    	result.include("iteration", iteration.load());
    	result.include("project", iteration.getProject().load());
    	result.include("iterationCards", iteration.getCards());
    	result.include("today", new LocalDate());
    	return iteration.getCards();
    }
	
	@Path("/projects/{iteration.project.id}/iterations/{iteration.id}/cardOwner/") @Post
	public void assign(Iteration iteration) {
	}
}
