package br.com.caelum.calopsita.integration.stories;

import org.hibernate.type.YesNoType;
import org.junit.Test;

import br.com.caelum.calopsita.integration.stories.common.DefaultStory;

/**
 * <b>In order to</b> know how much time a card stays undone <br>
 * <b>As</b> Morelli <br>
 * <b>I want to </b> record the creation and finish dates in a plugin <br>
 * @author josedavid
 *
 */
public class CardLifeCycleStory extends DefaultStory {

	@Test
	public void creatingCardWithLifeCicleRecordsCreationDate() throws Exception {
		given.thereIsAnUserNamed("Hugo").and()
			 .thereIsAProjectNamed("Archimedes").ownedBy("Hugo")
			 	.withACardNamed("Make trim work").thatHasALifeCicleStarting(today()).and()
			 .iAmLoggedInAs("Hugo");
		when.iOpenProjectPageOf("Archimedes")
			.iOpenAllCardsPage();
		then.theCard("Make trim work").hasCreationDate(today());
	}
	
	@Test
	public void markingACardAsDoneRecordsFinishDate() throws Exception {
		given.thereIsAnUserNamed("Erich").and()
			.thereIsAProjectNamed("Sysgraph").ownedBy("Erich")
				.withAnIterationWhichGoalIs("refactor it all").startingYesterday()
					.withACardNamed("finalize the main refactor")
						.planningCard().thatHasALifeCicleStarting(oneWeekAgo())
					.also()
			.iAmLoggedInAs("Erich");
		when.iOpenProjectPageOf("Sysgraph").and()
			.iOpenThePageOfCurrentIteration().and()
			.iFlagTheCard("finalize the main refactor").asDone();
		then.theCard("finalize the main refactor").hasFinishDate(today());
		
	}
}
