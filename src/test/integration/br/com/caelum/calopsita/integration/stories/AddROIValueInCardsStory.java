package br.com.caelum.calopsita.integration.stories;

import static org.junit.Assert.*;

import org.junit.Test;

import br.com.caelum.calopsita.integration.stories.common.DefaultStory;

/**
 * <b>In order to</b> tell how much ROI we're bringing to our clients <br />
 * <b>As a</b> scrum product owner <br />
 * <b>I want to</b> add a ROI value to cards using an integer number <br />
 *
 * @author ceci
 */
public class AddROIValueInCardsStory extends DefaultStory{

	@Test
	public void addROIValueToCard() throws Exception {
		given.thereIsAnUserNamed("Adriano").and()
			.thereIsAProjectNamed("VRaptor3").ownedBy("Adriano")
				.withACardNamed("Controller validation")
					.whichDescriptionIs("make the validation logics")
					.withROI(5).and()
			.iAmLoggedInAs("Adriano");
		when.iOpenProjectPageOf("VRaptor3").and()
			.iOpenCardsPage().and()
			.iOpenAllCardsPage();
		then.theCard("Controller validation").hasROI(5);
	}

	@Test
	public void editROIValueInCard() throws Exception {
		given.thereIsAnUserNamed("Caue").and()
			.thereIsAProjectNamed("RestfulieRails").ownedBy("Caue")
			.withACardNamed("Hypermedia support")
				.whichDescriptionIs("use http codes for transitions")
				.withROI(12).and()
			.iAmLoggedInAs("Caue");
		when.iOpenProjectPageOf("RestfulieRails").and()
			.iOpenCardsPage().and()
				.onCard("Hypermedia support")
				.changeRoiTo("15");
		then.theCard("Hypermedia support").hasROI(15);
	}
}
