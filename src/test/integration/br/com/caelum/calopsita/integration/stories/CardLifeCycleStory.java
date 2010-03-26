package br.com.caelum.calopsita.integration.stories;

import org.junit.Ignore;
import org.junit.Test;

import br.com.caelum.calopsita.integration.stories.common.DefaultStory;

/**
 * <b>In order to</b> know how much time a card stays undone <br>
 * <b>As a</b> Morelli <br>
 * <b>I want to </b> record the creation and finish dates in a plugin <br>
 * @author josedavid
 *
 */
public class CardLifeCycleStory extends DefaultStory {

	@Test
	public void creatingCardWithLifeCicleRecordsCreationDate() throws Exception {
		given.thereIsAnUserNamed("Hugo").and()
			 .thereIsAProjectNamed("Archimedes").ownedBy("Hugo")
			 	.withACardNamed("Make trim work").thatHasALifeCicle(today()).and()
			 .iAmLoggedInAs("Hugo");
		when.iOpenProjectPageOf("Archimedes")
			.iOpenAllCardsPage();
		then.theCard("Make trim work").hasCreationDate(today());
	}
}
