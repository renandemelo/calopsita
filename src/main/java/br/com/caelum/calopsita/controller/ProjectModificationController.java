package br.com.caelum.calopsita.controller;

import br.com.caelum.calopsita.model.Project;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;

@Resource
public class ProjectModificationController {
    
	private Result result;
	
	public ProjectModificationController(Result result) {
		this.result = result;
	}

	public void list(Project project) {
		Project loaded = project.load();
		this.result.include("project", loaded);		
		this.result.include("modifications", loaded.getModifications());
	}


}
