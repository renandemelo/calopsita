package br.com.caelum.calopsita.persistence.dao;

import java.util.List;

import org.hibernate.Session;

import br.com.caelum.calopsita.model.Project;
import br.com.caelum.calopsita.model.User;
import br.com.caelum.calopsita.model.Card.Status;
import br.com.caelum.calopsita.plugins.owner.AssignableCard;
import br.com.caelum.calopsita.repository.UserRepository;
import br.com.caelum.vraptor.ioc.Component;

@Component
public class UserDao implements UserRepository {

    private final Session session;

    public UserDao(Session session) {
        this.session = session;
    }

    public void add(User user) {
        this.session.save(user);
    }

    public User find(String login) {
        return (User) this.session.createQuery("from User where login = :login").setParameter("login", login).uniqueResult();
    }

    public List<Project> listAllFrom(User user) {
        return this.session.createQuery("from Project p where p.owner = :user or " +
        		":user in elements(p.colaborators)")
                .setParameter("user", user).list();
    }
    
    public boolean isCardOwner(User user){
    	List<AssignableCard> cards = this.session.createQuery("from AssignableCard ac where ac.owner = :user")
    			.setParameter("user", user).list();
		for(AssignableCard card : cards){
			if(card.getCard().getIteration().isCurrent() && card.getCard().getStatus() != Status.DONE)
				return true;
		}
		return false;
    }

}
