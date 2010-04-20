package br.com.caelum.calopsita.integration.stories;

import org.junit.Test;

import br.com.caelum.calopsita.integration.stories.common.DefaultStory;


/**
 * 
 * @author davidcurado
 */
public class EditCardStory extends DefaultStory {

	@Test
	public void WhenICancelAnEditTheBrowserGoesBack() throws Exception {
		//TODO: fix this problem...
		given.thereIsAnUserNamed("Juju").and()
		     .thereIsAProjectNamed("Wars")
		         .ownedBy("Juju")
		         .withACardNamed("cartao").and()
		     .iAmLoggedInAs("Juju");
		when.iOpenProjectPageOf("Wars").and()
		    .iOpenCardsPage().and()
		    .iEditTheCard("cartao")
		    .iClickButton("Cancel");
		then.iAmNotEditingCard().and()
		    .iMustBeOnCardsPage();
	}
}
