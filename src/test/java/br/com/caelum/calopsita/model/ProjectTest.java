package br.com.caelum.calopsita.model;

import java.util.ArrayList;
import java.util.List;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.joda.time.LocalDateTime;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import br.com.caelum.calopsita.repository.ProjectModificationRepository;
import br.com.caelum.calopsita.repository.ProjectRepository;
import br.com.caelum.calopsita.util.DateTimeGenerator;

public class ProjectTest {
	
	private Mockery mockery;
	private ProjectRepository repository;
	private Project project;
	private ProjectModificationRepository modificationRepository;

	@Before
    public void setUp() throws Exception {
        mockery = new Mockery();
        repository = mockery.mock(ProjectRepository.class);
        modificationRepository = mockery.mock(ProjectModificationRepository.class);
        project = new Project(repository, modificationRepository);
	}

	private void shouldModifyAProjectManyTimes(final int count) {
		mockery.checking(new Expectations() {
			{
				exactly(count).of(modificationRepository).add(with(any(ProjectModification.class)));
			}
		});
	}
	
	private void shouldListModifications() {
		mockery.checking(new Expectations() {
			{
				exactly(1).of(repository).listModificationsFrom(project);
				will(returnValue(new ArrayList()));
			}
		});
	}
	
	private void shouldModifyAProject() {
		shouldModifyAProjectManyTimes(1);
	}
	
    @Test
    public void testAddModificationRecordsDescription() throws Exception {
    	shouldModifyAProject();
    	shouldListModifications();
    	String description = "New Modification";
    	project.addModification (description);
    	List<ProjectModification> list = project.getLastModifications();
    	
    	Assert.assertEquals(list.get(0).getDescription(), description);
    }

    @Test
	public void testTheDateOfTheModification() throws Exception {
    	shouldModifyAProject();
    	shouldListModifications();
    	
		String description = "New Modification";
		
		LocalDateTime date1 = new LocalDateTime();
		project.addModification(description);
		LocalDateTime date2 = new LocalDateTime();
		
		ProjectModification modification = project.getLastModifications().get(0);
		Assert.assertTrue(modification.getDateTime().compareTo(date2) <= 0);
		Assert.assertTrue(modification.getDateTime().compareTo(date1) >= 0);
		
	}

    @Test
	public void testGetLastModificationsReturns30Elements() throws Exception {
    	shouldModifyAProjectManyTimes(31);
    	shouldListModifications();
		String firstDescription = "First Modification";
		String description = "Generic Modification";
		project.addModification(firstDescription);
		
		final LocalDateTime newToday = new LocalDateTime().plusDays(1);
		project.setDateTimeGenerator(new DateTimeGenerator() {
			@Override
			public LocalDateTime getNow() {
				return newToday;
			}
		});
		
		for (int i = 0; i < 30; i++) {
			project.addModification(description);	
		}	
		
		List<ProjectModification> lastModifications = project.getLastModifications();
		Assert.assertEquals(30, lastModifications.size());
		for (ProjectModification projectModification : lastModifications) {
			Assert.assertEquals(description,projectModification.getDescription());
		}
	}
    
    
}
