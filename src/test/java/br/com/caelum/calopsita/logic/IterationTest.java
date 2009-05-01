package br.com.caelum.calopsita.logic;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

import java.util.Arrays;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import br.com.caelum.calopsita.logic.IterationLogic;
import br.com.caelum.calopsita.model.Iteration;
import br.com.caelum.calopsita.model.Project;
import br.com.caelum.calopsita.model.Story;
import br.com.caelum.calopsita.repository.IterationRepository;
import br.com.caelum.calopsita.repository.StoryRepository;

public class IterationTest {
    private Mockery mockery;
    private IterationLogic logic;
    private IterationRepository iterationRepository;
    private StoryRepository storyRepository;

    @Before
    public void setUp() throws Exception {
        mockery = new Mockery();
        iterationRepository = mockery.mock(IterationRepository.class);
        storyRepository = mockery.mock(StoryRepository.class);
        logic = new IterationLogic(iterationRepository, storyRepository);
    }

    @After
    public void tearDown() {
        mockery.assertIsSatisfied();
    }

    @Test
    public void savingAnIteration() throws Exception {
        Iteration iteration = givenAnIteration();
        Project project = givenAProject();
    
        shouldSaveOnTheRepositoryTheIteration(iteration);
        
        whenISaveTheIteration(iteration, onThe(project));
        
        assertThat(iteration.getProject(), is(project));
    }

    @Test
	public void addingAStoryInAnIteration() throws Exception {
		Iteration iteration = givenAnIteration();
		Story story = givenAStory();
		
		shouldUpdateTheStory(story);
		
		whenIAddTheStoryToIteration(story, iteration);
		
		assertThat(story.getIteration(), is(iteration));
	}
    @Test
    public void removingAStoryOfAnIteration() throws Exception {
    	Iteration iteration = givenAnIteration();
    	Story story = givenAStory();
    	
    	Story loaded = givenLoadedStoryContainsIteration(story, iteration);
    	
    	whenIRemoveTheStoryOfIteration(story, iteration);

    	assertThat(loaded.getIteration(), is(nullValue()));
    }

	private void whenIRemoveTheStoryOfIteration(Story story, Iteration iteration) {
		logic.removeStories(iteration, Arrays.asList(story));
	}

	private Story givenLoadedStoryContainsIteration(final Story story, final Iteration iteration) {
		final Story loaded = new Story();
		
		mockery.checking(new Expectations() {
			{
				loaded.setIteration(iteration);
				
				one(storyRepository).load(story);
				will(returnValue(loaded));
				
				one(storyRepository).update(loaded);
			}
		});
		return loaded;
	}

	private void shouldUpdateTheStory(final Story story) {
    	
		mockery.checking(new Expectations() {
			{
				one(storyRepository).update(story);
				
				one(storyRepository).load(story);
				will(returnValue(story));
			}
		});
	}

	private void whenIAddTheStoryToIteration(Story story, Iteration iteration) {
    	logic.updateStories(iteration, Arrays.asList(story));
	}

	private Story givenAStory() {
		return new Story();
	}

	private void shouldSaveOnTheRepositoryTheIteration(final Iteration iteration) {
        
        mockery.checking(new Expectations() {
            {
                one(iterationRepository).add(iteration);
            }
        });
    }

    private Project onThe(Project project) {
        return project;
    }

    private void whenISaveTheIteration(Iteration iteration, Project project) {
        logic.save(iteration, project);
    }

    private Project givenAProject() {
        return new Project();
    }

    private Iteration givenAnIteration() {
        return new Iteration();
    }
}