package br.com.caelum.calopsita.repository;

import java.util.List;

import br.com.caelum.calopsita.model.Project;
import br.com.caelum.calopsita.model.User;
import br.com.caelum.calopsita.plugins.owner.AssignableCard;

public interface UserRepository {

	void add(User user);

    User find(String login);

	List<Project> listAllFrom(User user);
	
	boolean isCardOwner(User user);
}
