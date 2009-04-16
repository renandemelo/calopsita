package br.com.caelum.calopsita.integration.stories;

import org.junit.Test;

public class StoriesMustBeOrderedByPriorityStory extends DefaultStory {
	@Test
	public void storiesMustBeOrderedOutOfIterations() throws Exception {
		given.thereIsAnUserNamed("caue").and()
			.thereIsAProjectNamed("htmlunit")
				.ownedBy("caue")
				.withAStoryNamed("step1")
					.whichDescriptionIs("this is just step 1")
					.withPriority(3)
				.withAStoryNamed("step2")
					.whichDescriptionIs("step 2 duh")
					.withPriority(1).and()
			.iAmLoggedInAs("caue");
		when.iOpenProjectPageOf("htmlunit");
		then.theStory("step1")
				.appearsOnStoriesListAtPosition(1)
			.theStory("step2")
				.appearsOnStoriesListAtPosition(0);
	}
	
	@Test
	public void storiesMustBeOrderedInIterations() throws Exception {
		given.thereIsAnUserNamed("caue").and()
			.thereIsAProjectNamed("htmlunit")
				.ownedBy("caue")
				.withAnIterationWhichGoalIs("make it works")
				.withAStoryNamed("step1")
					.whichDescriptionIs("this is just step 1")
					.withPriority(3)
					.insideIterationWithGoal("make it works")
				.withAStoryNamed("step2")
					.whichDescriptionIs("step 2 duh")
					.withPriority(1)
					.insideIterationWithGoal("make it works")
				.withAStoryNamed("step3")
					.whichDescriptionIs("step 3 duh")
					.withPriority(5)
				.withAStoryNamed("step4")
					.whichDescriptionIs("step 4 duh")
					.withPriority(2).and()
			.iAmLoggedInAs("caue");
		when.iOpenProjectPageOf("htmlunit").and()
			.iOpenThePageOfIterationWithGoal("make it works");
		then.theStory("step1")
				.appearsOnStoriesListAtPosition(1)
			.theStory("step2")
				.appearsOnStoriesListAtPosition(0)
			.theStory("step3")
				.appearsOnOtherStoriesListAtPosition(1)
			.theStory("step4")
				.appearsOnOtherStoriesListAtPosition(0);
		
	}
}
