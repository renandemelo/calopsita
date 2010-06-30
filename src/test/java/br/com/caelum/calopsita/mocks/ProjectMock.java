package br.com.caelum.calopsita.mocks;

import br.com.caelum.calopsita.model.Project;

public class ProjectMock extends Project{
	
	private String addedModificationDescription;

	@Override
	public void addModification(String description) {
		addedModificationDescription = description;
	}

	public String getAddedModificationDescription() {
		return addedModificationDescription;
	}

}
