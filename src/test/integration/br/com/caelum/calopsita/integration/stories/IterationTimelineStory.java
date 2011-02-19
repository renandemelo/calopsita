package br.com.caelum.calopsita.integration.stories;

import org.junit.Test;

import br.com.caelum.calopsita.integration.stories.common.DefaultStory;

/**
 * <b>In order to</b> easily see how many days are gone and how many days I
 * 	still have to work on my iteration <br>
 * <b>As a</b> Fabs <br>
 * <b>I want</b> to see a timeline of my iteration, clearly indicating
 * 	today and the start and end days, when they exist <br>
 *
 * @author ceci
 */
public class IterationTimelineStory extends DefaultStory {

	@Test
	public void currentIterationWithStartAndEndDays() throws Exception {
		given.thereIsAnUserNamed("Ferreira").and()
			.thereIsAProjectNamed("Hoops").ownedBy("Ferreira")
				.withAnIterationWhichGoalIs("Allow attributes on fields")
				.starting(oneWeekAgo()).ending(nextWeek()).and()
			.iAmLoggedInAs("Ferreira");
		when.iOpenProjectPageOf("Hoops").and()
		    .iOpenThePageOfIterationWithGoal("Allow attributes on fields");
		then.theIterationTimeline()
				.showsTodayMarker().and()
				.showsItBegan(oneWeekAgo()).and()
				.showsItEnds(nextWeek());
	}

	@Test
	public void currentIterationWithoutEndDay() throws Exception {
		given.thereIsAnUserNamed("Ferreira").and()
			.thereIsAProjectNamed("Hoops").ownedBy("Ferreira")
				.withAnIterationWhichGoalIs("Allow attributes on fields")
				.starting(oneWeekAgo()).and()
			.iAmLoggedInAs("Ferreira");
		when.iOpenProjectPageOf("Hoops").and()
			.iOpenIterationsPage().and()
		    .iOpenThePageOfIterationWithGoal("Allow attributes on fields");
		then.theIterationTimeline()
				.showsTodayMarker().and()
				.showsItBegan(oneWeekAgo()).and()
				.showsItEnds(inNoSpecificDate());
	}

	@Test
	public void iterationWithNoStartButWithEndDay() throws Exception {
		given.thereIsAnUserNamed("Ferreira").and()
			.thereIsAProjectNamed("Hoops").ownedBy("Ferreira")
				.withAnIterationWhichGoalIs("Allow attributes on fields")
				.ending(nextWeek()).and()
			.iAmLoggedInAs("Ferreira");
		when.iOpenProjectPageOf("Hoops").and()
			.iOpenIterationsPage().and()
		    .iOpenThePageOfIterationWithGoal("Allow attributes on fields");
		then.theIterationTimeline()
				.showsTodayMarker().and()
				.showsItBegan(inNoSpecificDate()).and()
				.showsItEnds(nextWeek());
	}

	@Test
	public void iterationWithNoDateAtAll() throws Exception {
		given.thereIsAnUserNamed("Ferreira").and()
			.thereIsAProjectNamed("Hoops").ownedBy("Ferreira")
				.withAnIterationWhichGoalIs("Allow attributes on fields")
			.iAmLoggedInAs("Ferreira");
		when.iOpenProjectPageOf("Hoops").and()
			.iOpenIterationsPage().and()
		    .iOpenThePageOfIterationWithGoal("Allow attributes on fields");
		then.theIterationTimeline()
				.showsTodayMarker().and()
				.showsItBegan(inNoSpecificDate()).and()
				.showsItEnds(inNoSpecificDate());
	}

	@Test
	public void iterationStartingToday() throws Exception {
		given.thereIsAnUserNamed("Ferreira").and()
			.thereIsAProjectNamed("Hoops").ownedBy("Ferreira")
				.withAnIterationWhichGoalIs("Allow attributes on fields")
				.starting(today()).ending(nextWeek()).and()
			.iAmLoggedInAs("Ferreira");
		when.iOpenProjectPageOf("Hoops").and()
			.iOpenIterationsPage().and()
		    .iOpenThePageOfIterationWithGoal("Allow attributes on fields");
		then.theIterationTimeline()
				.showsTodayMarker().and()
				.showsItBegan(today());
	}

	@Test
	public void iterationEndingToday() throws Exception {
		given.thereIsAnUserNamed("Ferreira").and()
			.thereIsAProjectNamed("Hoops").ownedBy("Ferreira")
				.withAnIterationWhichGoalIs("Allow attributes on fields")
				.starting(oneWeekAgo()).ending(today()).and()
			.iAmLoggedInAs("Ferreira");
		when.iOpenProjectPageOf("Hoops").and()
			.iOpenIterationsPage().and()
		    .iOpenThePageOfIterationWithGoal("Allow attributes on fields");
		then.theIterationTimeline()
				.showsTodayMarker().and()
				.showsItEnds(today());
	}
}
