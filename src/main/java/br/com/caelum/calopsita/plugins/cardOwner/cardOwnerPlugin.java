package br.com.caelum.calopsita.plugins.cardOwner;

import br.com.caelum.calopsita.model.Iteration;
import br.com.caelum.calopsita.model.Menu;
import br.com.caelum.calopsita.model.Parameters;
import br.com.caelum.calopsita.model.PluginConfig;
import br.com.caelum.calopsita.model.Project;
import br.com.caelum.calopsita.model.SubmenuItem;
import br.com.caelum.vraptor.ioc.Component;

@Component
public class cardOwnerPlugin implements PluginConfig{

	//Feito no dia 16/03 pela dupla Toshi e José David.
	@Override
	public String getDescription() {
		return null;
	}

	@Override
	public String getName() {
		return "Card Owner";
	}

	@Override
	public void includeMenus(Menu menu, Parameters parameters) {
		if (parameters.contains("project") && parameters.contains("iteration")) {
			Project project = parameters.get("project");
			Iteration iteration = parameters.get("iteration");
			menu.getOrCreate("iterations")
				.add(new SubmenuItem("owner", "/projects/" + project.getId() + "/iterations/" + iteration.getId() + "/"));
		}
	}
}
