package br.com.caelum.calopsita.integration.stories;

import org.junit.Test;

import br.com.caelum.calopsita.integration.stories.common.DefaultStory;
import br.com.caelum.calopsita.model.Gadgets;

/**
 * <b>In order to</b>prevent the user from watching unuseful Interface elements<br>
 *  * <b>I want</b> to remove the x button for cardtype deleting, feature not supported by the system<br>
 * @author santiago
 *
 */
public class EditCardTypeStory extends DefaultStory{

	
	@Test
	public void dontShowDeleteButtonOnCardTypeList() {
		given.thereIsAnUserNamed("kung").and()
		.thereIsAProjectNamed("CuKung'er").ownedBy("kung").and()
		.iAmLoggedInAs("kung");
		when.iOpenProjectPageOf("CuKung'er").and()
			.iOpenAdminPage().and()
			.iAddTheCardType("Story").and()
			.iOpenCardTypesPage();
		then.theCardType("Story").doesntAppearsOnCardType("delete Story");
	}
	
	
	@Test
	public void clearFormOnCancelButtonClickOnEditCardType(){
		given.thereIsAnUserNamed("kung").and()
		.thereIsAProjectNamed("Whatever Project").ownedBy("kung")
			.withACardTypeNamed("OriginalType")
				.withGadgets(Gadgets.PRIORITIZATION).and()
				.iAmLoggedInAs("kung");
		when.iOpenProjectPageOf("Whatever Project").and()
		.iOpenAdminPage().and()
		.iEditTheCardTypeName("OriginalType", "NewType").and()
		.cancelEditedCardType();
		then.isCardTypeNameClear(); 
	}
	
}
