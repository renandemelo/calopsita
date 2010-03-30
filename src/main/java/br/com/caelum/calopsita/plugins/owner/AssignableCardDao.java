package br.com.caelum.calopsita.plugins.owner;

import org.hibernate.Session;

import br.com.caelum.vraptor.ioc.Component;

@Component
public class AssignableCardDao implements AssignableCardRepository {
	private final Session session;

	public AssignableCardDao(Session session) {
		this.session = session;
	}

	@Override
	public AssignableCard save(AssignableCard card) {
		session.saveOrUpdate(card);
		return (AssignableCard) session.load(AssignableCard.class, card.getId());
	}

}
