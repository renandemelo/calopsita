package br.com.caelum.calopsita.model;

import java.util.List;

import org.joda.time.LocalDateTime;
import org.junit.Assert;
import org.junit.Test;

import br.com.caelum.calopsita.util.DateTimeGenerator;

public class ProjectTest {
		
    @Test
    public void testAddModificationRecordsDescription() throws Exception {
    	Project project = new Project();
    	String description = "New Modification";
		
    	project.addModification (description);
    	List<ProjectModification> list = project.getLastModifications();
    	
    	Assert.assertEquals(list.get(0).getDescription(), description);
    }
    
    @Test
	public void testTheDateOfTheModification() throws Exception {
		Project project = new Project();
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
		Project project = new Project();
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

//    @Test
//	public void testTheDateOfTheModification() throws Exception {
//		Project project = new Project();
//		String string = "New Modification";		
//		LocalDate date = new LocalDate();
//		project.addModification(string);
//		
//		Assert.assertEquals(project.getLastModifications().get(0).getDate(), date);
//	}
    
}
