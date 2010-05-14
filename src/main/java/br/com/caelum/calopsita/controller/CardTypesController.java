package br.com.caelum.calopsita.controller;

import static br.com.caelum.vraptor.view.Results.logic;

import java.util.List;

import br.com.caelum.calopsita.model.CardType;
import br.com.caelum.calopsita.model.Gadget;
import br.com.caelum.calopsita.model.Gadgets;
import br.com.caelum.calopsita.model.Project;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
@Resource
public class CardTypesController {

	private final Result result;

	public CardTypesController(Result result) {
		this.result = result;
	}

	@Path("/projects/{project.id}/cardTypes/") @Get
	public void list(Project project) {
		result.include("project", project);
		result.include("cardTypeList", project.getCardTypes());
		result.include("gadgets", Gadgets.values());
	}

	@Path("/projects/{cardType.project.id}/cardTypes/") @Post
	public void save(CardType cardType) {
		cardType.save();
		result.use(logic()).redirectTo(CardTypesController.class).list(cardType.getProject());
	}
	
	
	@Path("/projects/{cardType.project.id}/cardTypes/{cardType.id}/") @Get
	public void edit (CardType cardType) {
	 	CardType load = cardType.load();
		List<Gadgets> gadgets = load.getGadgets();
		Project project = load.getProject().load();
		result.include("cardType", load);
		result.include("cardTypeGadgets", gadgets);
		result.forwardTo(CardTypesController.class).list(project);
	}
	
	@Path("/projects/{cardType.project.id}/cardTypes/{cardType.id}/") @Post
	public void update (CardType cardType, List<Gadget> gadgets) {
		CardType load = cardType.load();
		load.setName(cardType.getName());
		load.setGadgets(cardType.getGadgets());
		load.setProject(cardType.getProject());
		load.update();
		result.forwardTo(CardTypesController.class).list(cardType.getProject());
	}
}
