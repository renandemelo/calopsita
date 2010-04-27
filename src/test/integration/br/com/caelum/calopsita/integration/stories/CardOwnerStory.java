package br.com.caelum.calopsita.integration.stories;

import static org.junit.Assert.*;

import org.junit.Test;

import br.com.caelum.calopsita.integration.stories.common.DefaultStory;


/**
 * <b>In order to</b> avoid rework <br>
 * <b>As a</b> Collaborator <br>
 * <b>I want to </b> notify that I am working on that card <br>
 * @author santiago
 *
 */
public class CardOwnerStory extends DefaultStory {

	@Test
	public void addingAnAssignableCard() {
		given.thereIsAnUserNamed("adriano").and()
			.thereIsAProjectNamed("Marriage").ownedBy("adriano")
			.withAnIterationWhichGoalIs("Postpone").and()
			.iAmLoggedInAs("adriano");
		when.iOpenProjectPageOf("Marriage").and()
			.iOpenCardsPage().and()
			.iAddTheCard("schedule date")
				.assignableCard().planningCard()
				.withDescription("we need a date for marriage").and()
			.iOpenCardsPage().and()
			.iOpenAllCardsPage();
		then.theCard("schedule date").hasOwnerGadget();
	}
	
	@Test
	public void addingOwnerToAssignableCard() throws Exception {
		given.thereIsAnUserNamed("adriano").and()
			.thereIsAProjectNamed("Marriage").ownedBy("adriano")
			.withACurrentIterationWhichGoalIs("Postpone")
			.withACardNamed("schedule date").planningCard().assignable().and()
			.iAmLoggedInAs("adriano");
		when.iOpenProjectPageOf("Marriage").and()
		.iOpenIterationsPage().iOpenThePageOfIterationWithGoal("Postpone").iClickOn("Be an Owner now!");
		then.currentIterationCardList().isOwnedBy("adriano");
	}
}
