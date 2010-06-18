package br.com.caelum.calopsita.persistence.dao;

import org.hibernate.Session;

import br.com.caelum.calopsita.model.ProjectModification;
import br.com.caelum.calopsita.plugins.PluginResultTransformer;
import br.com.caelum.calopsita.repository.ProjectModificationRepository;
import br.com.caelum.vraptor.ioc.Component;

@Component
public class ProjectModificationDao implements ProjectModificationRepository {

    private final Session session;
	private final PluginResultTransformer transformer;

    public ProjectModificationDao(Session session, PluginResultTransformer transformer) {
        this.session = session;
		this.transformer = transformer;
    }
    
	@Override
	public ProjectModification load(ProjectModification modification) {
		return (ProjectModification) session.load(ProjectModification.class, modification.getId());
	}

	@Override
	public void add(ProjectModification t) {
		session.save(t);
		
	}

	@Override
	public void remove(ProjectModification t) {
		session.delete(t);
		
	}

	@Override
	public void update(ProjectModification t) {
		session.update(t);
	}

}
