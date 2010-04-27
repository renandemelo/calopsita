package br.com.caelum.calopsita.plugins.owner;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.transform.ResultTransformer;

import br.com.caelum.calopsita.model.Card;
import br.com.caelum.calopsita.model.Project;
import br.com.caelum.calopsita.model.User;
import br.com.caelum.vraptor.ioc.Component;

@Component
public class AssignableCardDao implements AssignableCardRepository {
	private final Session session;
	private ResultTransformer transformer;

	public AssignableCardDao(Session session, ResultTransformer transformer) {
		this.session = session;
		this.transformer = transformer;
	}

	@Override
	public AssignableCard save(AssignableCard card) {
		session.saveOrUpdate(card);
		return (AssignableCard) session.load(AssignableCard.class, card.getId());
	}
	
	@Override
	public List<Card> listAllCardsFrom(Project project, User owner) {
    	return this.session.createQuery("from AssignableCard ac where ac.project = :project and ac.status != 'DONE' and ac.owner = :owner")
    	.setResultTransformer(transformer)
    	.setParameter("project", project)
    	.setParameter("owner", owner).list();
    }
}
