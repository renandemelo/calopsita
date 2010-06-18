package br.com.caelum.calopsita.plugins.owner;

import java.util.List;

import br.com.caelum.calopsita.model.Card;
import br.com.caelum.calopsita.model.Project;
import br.com.caelum.calopsita.model.User;

public interface AssignableCardRepository {
	AssignableCard save(AssignableCard card);
	List<Card> listAllPendingCardsFrom(Project project, User owner);
}
