package br.com.caelum.calopsita.repository;

import br.com.caelum.calopsita.model.ProjectModification;

public interface ProjectModificationRepository extends BaseRepository<ProjectModification> {

	ProjectModification load (ProjectModification modification);
}
