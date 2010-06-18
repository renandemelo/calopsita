package br.com.caelum.calopsita.controller;

import br.com.caelum.calopsita.model.Project;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;

@Resource
public class ProjectModificationsController {
    
	private Result result;
	
	public ProjectModificationsController(Result result) {
		this.result = result;
	}

	@Path("/projects/{project.id}/modifications/last/") @Get
	public void list(Project project) {
		Project loaded = project.load();
		this.result.include("project", loaded);		
		this.result.include("modifications", loaded.getLastModifications());
	}

}
