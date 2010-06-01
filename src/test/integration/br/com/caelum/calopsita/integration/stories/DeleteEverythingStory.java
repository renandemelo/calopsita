package br.com.caelum.calopsita.integration.stories;

import org.junit.Test;

import br.com.caelum.calopsita.integration.stories.common.DefaultStory;

/**
 * <b>In order to</b> be able to make a mistake with no fear <br>
 * <b>As</b> Fabs<br>
 * <b>I want to</b> delete cards, iterations and projects that I have created<br>
 *
 */
public class DeleteEverythingStory extends DefaultStory {
    @Test
    public void deleteAnIterationAndConfirm() throws Exception {
        given.thereIsAnUserNamed("kung").and()
        .thereIsAProjectNamed("Vraptor 3").ownedBy("kung")
            .withAnIterationWhichGoalIs("make it work")
	            .withACardNamed("support Vraptor 2")
	            	.planningCard()
	                .whichDescriptionIs("some stuff should be backward compatible").and()
            .withAnIterationWhichGoalIs("i18n").and()
        .iAmLoggedInAs("kung");
        when.iOpenProjectPageOf("Vraptor 3").and()
            .iOpenIterationsPage().and()
            .iDeleteTheIterationWithGoal("make it work").andConfirm("deletion");
        then.theIteration("make it work").notAppearsOnList();
        when.iOpenThePageOfIterationWithGoal("i18n");
        then.theCard("support Vraptor 2")
            .appearsOnBacklog();
    }

    @Test
    public void deleteAnIterationAndDontConfirm() throws Exception {
        given.thereIsAnUserNamed("kung").and()
        .thereIsAProjectNamed("Vraptor 3").ownedBy("kung")
            .withAnIterationWhichGoalIs("make it work")
	            .withACardNamed("support Vraptor 2")
	                .whichDescriptionIs("some stuff should be backward compatible").and()
            .withAnIterationWhichGoalIs("i18n").and()
        .iAmLoggedInAs("kung");
        when.iOpenProjectPageOf("Vraptor 3").and()
            .iOpenIterationsPage().and()
            .iDeleteTheIterationWithGoal("make it work").andDontConfirm("deletion");
        then.theIteration("make it work").appearsOnList();
    }

	@Test
	public void deleteACardAndConfirm() {
		given.thereIsAnUserNamed("fabs").and()
			.thereIsAProjectNamed("method-finder").ownedBy("fabs")
				.withACardNamed("Support everything").whichDescriptionIs("That is a mistake").and()
			.iAmLoggedInAs("fabs");
		when.iOpenProjectPageOf("method-finder").and()
		    .iOpenCardsPage().and()
			.iDeleteTheCard("Support everything").andConfirm("deletion");
		then.theCard("Support everything").shouldNotAppearOnCardList();
	}
	@Test
	public void deleteACardAndDontConfirm() {
		given.thereIsAnUserNamed("fabs").and()
			.thereIsAProjectNamed("method-finder").ownedBy("fabs")
				.withACardNamed("Support everything").whichDescriptionIs("That is a mistake").and()
			.iAmLoggedInAs("fabs");
		when.iOpenProjectPageOf("method-finder").and()
		    .iOpenCardsPage().and()
			.iDeleteTheCard("Support everything").andDontConfirm("deletion");
		then.theCard("Support everything").appearsOnList();
	}
	@Test
	public void deleteACardAndSubcards() {
		given.thereIsAnUserNamed("fabs").and()
			.thereIsAProjectNamed("method-finder").ownedBy("fabs")
				.withACardNamed("Support everything").whichDescriptionIs("That is a mistake")
					.withASubcardNamed("support continuations").whichDescriptionIs("continuations is good").and()
			.iAmLoggedInAs("fabs");
		when.iOpenProjectPageOf("method-finder").and()
		    .iOpenCardsPage().and()
			.iDeleteTheCard("Support everything").andConfirm("deletion").andConfirm("subcards");
		then.theCard("Support everything").shouldNotAppearOnCardList().and()
			.theCard("support continuations").shouldNotAppearOnCardList();
	}
	@Test
	public void deleteACardButNotSubcards() {
		given.thereIsAnUserNamed("fabs").and()
			.thereIsAProjectNamed("method-finder").ownedBy("fabs")
				.withACardNamed("Support everything").whichDescriptionIs("That is a mistake")
					.withASubcardNamed("support continuations").whichDescriptionIs("continuations is good").and()
			.iAmLoggedInAs("fabs");
		when.iOpenProjectPageOf("method-finder").and()
		    .iOpenCardsPage().and()
			.iDeleteTheCard("Support everything").andConfirm("deletion").andDontConfirm("subcards");
		then.theCard("Support everything").shouldNotAppearOnCardList().and()
			.theCard("support continuations").appearsOnList();
	}


	@Test
	public void deleteMyProject() {
		given.thereIsAnUserNamed("fabs").and()
			.thereIsAProjectNamed("goat").ownedBy("fabs").and()
			.iAmLoggedInAs("fabs");
		when.iDeleteTheProject("goat").andConfirm("deletion");
		then.project("goat").notAppearsOnList();
	}
	@Test
	public void deleteMyProjectButNotConfirm() {
		given.thereIsAnUserNamed("fabs").and()
			.thereIsAProjectNamed("goat").ownedBy("fabs").and()
			.iAmLoggedInAs("fabs");
		when.iDeleteTheProject("goat").andDontConfirm("deletion");
		then.project("goat").appearsOnList();
	}
	@Test
	public void cantDeleteProjectOwnedByOthers() {
		given.thereIsAnUserNamed("fabs").and()
			.thereIsAnUserNamed("hugo")
			.thereIsAProjectNamed("archimedes").ownedBy("hugo").withColaborator("fabs").and()
			.iAmLoggedInAs("fabs");
		when.iListProjects();
		then.project("archimedes").appearsOnList().and()
			.deletionLinkDoesnotAppearForProject("archimedes");
	}
	
	@Test
	public void deleteAROICard() {
		given.thereIsAnUserNamed("fabs").and()
			.thereIsAProjectNamed("method-finder").ownedBy("fabs")
				.withACardNamed("Support everything").whichDescriptionIs("That is a mistake")
				.withROI(5).and()
			.iAmLoggedInAs("fabs");
		when.iOpenProjectPageOf("method-finder").and()
		    .iOpenCardsPage().and()
			.iDeleteTheCard("Support everything").andConfirm("deletion");
		then.theCard("Support everything").shouldNotAppearOnCardList();
	}
	
	@Test
	public void deleteAROICardAndSubCards() {
		given.thereIsAnUserNamed("fabs").and()
			.thereIsAProjectNamed("method-finder").ownedBy("fabs")
				.withACardNamed("Support everything").withROI(5).whichDescriptionIs("That is a mistake")
				.withASubcardNamed("support continuations").whichDescriptionIs("continuations is good").and()
			.iAmLoggedInAs("fabs");
		when.iOpenProjectPageOf("method-finder").and()
		    .iOpenCardsPage().and()
			.iDeleteTheCard("Support everything").andConfirm("deletion").andConfirm("subcards");
		then.theCard("Support everything").shouldNotAppearOnCardList()
		    .theCard("support continuations").shouldNotAppearOnCardList();
	}
	
	@Test
	public void deleteAROICardButNotSubcards() {
		given.thereIsAnUserNamed("fabs").and()
			.thereIsAProjectNamed("method-finder").ownedBy("fabs")
				.withACardNamed("Support everything").withROI(5).whichDescriptionIs("That is a mistake")
				.withASubcardNamed("support continuations").whichDescriptionIs("continuations is good").and()
			.iAmLoggedInAs("fabs");
		when.iOpenProjectPageOf("method-finder").and()
		    .iOpenCardsPage().and()
			.iDeleteTheCard("Support everything").andConfirm("deletion").andDontConfirm("subcards");
		then.theCard("Support everything").shouldNotAppearOnCardList()
		    .theCard("support continuations").appearsOnList();
	}
}
