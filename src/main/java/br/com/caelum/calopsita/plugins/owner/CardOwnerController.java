package br.com.caelum.calopsita.plugins.owner;

import static br.com.caelum.vraptor.view.Results.nothing;

import java.util.List;

import org.joda.time.LocalDate;

import br.com.caelum.calopsita.model.Iteration;
import br.com.caelum.calopsita.model.Project;
import br.com.caelum.calopsita.plugins.prioritization.PrioritizableCard;
import br.com.caelum.calopsita.plugins.prioritization.PrioritizationRepository;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;

@Resource
public class CardOwnerController {
	private final PrioritizationRepository repository;
	private final Result result;

	public CardOwnerController(Result result, PrioritizationRepository repository) {
		this.result = result;
		this.repository = repository;
	}

	@Path("/projects/{iteration.project.id}/iterations/{iteration.id}/assignation/") @Get
    public void assignation(Iteration iteration) {
    	result.include("iteration", iteration.load());
    	result.include("project", iteration.getProject().load());
    	result.include("otherCards", iteration.getProject().getCardsWithoutIteration());
    	result.include("today", new LocalDate());
    }

	@Path("/projects/{iteration.project.id}/iterations/{iteration.id}/assign/") @Post
	public void assign(Iteration iteration) {
	}
}
