package br.com.caelum.calopsita.integration.stories;

import org.junit.Test;

import br.com.caelum.calopsita.integration.stories.common.DefaultStory;
import br.com.caelum.calopsita.model.Gadgets;

/**
 * <b>In order to</b> spend less time adding cards <br>
 * <b>As a</b> Olivia and Morelli <br>
 * <b>I want</b> to create predefined card types on admin page of the project,
 * 	choosing a name and a list of gadgets for this type. <br>
 * 	At card creation, I should choose the card type and respective gadgets will
 * 	be checked <br>
 * @author lucascs
 *
 */
public class CardTypeStory extends DefaultStory {


	@Test
	public void createACardType() {
		given.thereIsAnUserNamed("kung").and()
			.thereIsAProjectNamed("CuKung'er").ownedBy("kung").and()
			.iAmLoggedInAs("kung");
		when.iOpenProjectPageOf("CuKung'er").and()
			.iOpenAdminPage().and()
			.iAddTheCardType("Story").and()
			.iOpenCardTypesPage();
		then.theCardType("Story").appearsOnList();
	}

	@Test
	public void createACardOfAGivenType() {
		given.thereIsAnUserNamed("kung").and()
			.thereIsAProjectNamed("CuKung'er").ownedBy("kung")
				.withACardTypeNamed("Story")
					.withGadgets(Gadgets.PLANNING).and()
			.iAmLoggedInAs("kung");
		when.iOpenProjectPageOf("CuKung'er").and()
			.iOpenCardsPage()
			.iAddTheCard("Describe something")
				.withType("Story")
				.withDescription("I want to describe stuff");
		then.theCard("Describe something")
				.isNotPrioritizable().and()
				.isPlannable();

	}

	@Test
	public void editACardTypeName(){
		given.thereIsAnUserNamed("kung").and()
		.thereIsAProjectNamed("Whatever Project").ownedBy("kung")
			.withACardTypeNamed("OriginalType")
				.withGadgets(Gadgets.PRIORITIZATION).and()
				.iAmLoggedInAs("kung");
		when.iOpenProjectPageOf("Whatever Project").and()
		.iOpenAdminPage().and()
		.iEditTheCardTypeName("OriginalType", "NewType").and()
		.iRemoveCardTypeGadget(Gadgets.PRIORITIZATION).and()
		.iAddCardTypeGadget(Gadgets.PLANNING).and()
		.saveEditedCardType().and()
		.iOpenEditCardTypePage("NewType");
		then.theListedCardType("NewType").existsAsCardTypeInList().and()
		.cardTypeGadgetIsSet(Gadgets.PLANNING).and()
		.cardTypeGadgetIsNotSet(Gadgets.PRIORITIZATION); 
	}
	
	@Test
	public void editACardTypeShowsSuccessMessage(){
		given.thereIsAnUserNamed("kung").and()
		.thereIsAProjectNamed("Whatever Project").ownedBy("kung")
			.withACardTypeNamed("OriginalType")
				.withGadgets(Gadgets.PRIORITIZATION).and()
				.iAmLoggedInAs("kung");
		when.iOpenProjectPageOf("Whatever Project").and()
		.iOpenAdminPage().and()
		.iEditTheCardTypeName("OriginalType", "NewType").and()
		.iRemoveCardTypeGadget(Gadgets.PRIORITIZATION).and()
		.iAddCardTypeGadget(Gadgets.PLANNING).and()
		.saveEditedCardType();
		then.thereIsSuccessMessage("Card type was succesfully edited");
	}
	
	@Test
	public void listDetailsOnACardType() {
		given.thereIsAnUserNamed("kung").and()
			.thereIsAProjectNamed("CuKung'er").ownedBy("kung")
				.withACardTypeNamed("Story")
					.withGadgets(Gadgets.PLANNING).and()
			.iAmLoggedInAs("kung");
		when.iOpenProjectPageOf("CuKung'er").and()
		.iOpenAdminPage().and()
		.iOpenCardTypesPage();
		then.theCardTypeGadgetIsListed("Story", Gadgets.PLANNING);
		

	}

}
