package br.com.caelum.calopsita.logic;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jmock.Expectations;
import org.jmock.Mockery;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import br.com.caelum.calopsita.controller.ProjectModificationsController;
import br.com.caelum.calopsita.controller.ProjectsController;
import br.com.caelum.calopsita.infra.vraptor.SessionUser;
import br.com.caelum.calopsita.mocks.MockHttpSession;
import br.com.caelum.calopsita.model.Project;
import br.com.caelum.calopsita.model.ProjectModification;
import br.com.caelum.calopsita.repository.ProjectModificationRepository;
import br.com.caelum.calopsita.repository.ProjectRepository;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.util.test.MockResult;

@SuppressWarnings("unchecked")
public class ProjectModificationTest {
	
	
	private Mockery mockery;
	private ProjectRepository repository;
	private ProjectModificationRepository modificationRepository;

	@Before
	public void prepareMocks(){
		mockery = new Mockery();
        repository = mockery.mock(ProjectRepository.class);
        modificationRepository = mockery.mock(ProjectModificationRepository.class);
	}
	
	
	@Test
	public void testListModifications(){			
		final Project project = createProject(repository, 1L, null);
		
		final ArrayList<ProjectModification> lastModifications = new ArrayList<ProjectModification>();
		final Project loadedProject = new Project(){
			@Override
			public List<ProjectModification> getLastModifications() {
				return lastModifications;
			}
		};
		loadedProject.setId(1L);
		loadedProject.setName("Calopsita");
		
    	shouldLoadProject(project, loadedProject);
                
		Result result = new MockResult();
		ProjectModificationsController controller = new ProjectModificationsController(result);		
				
		controller.list(project);
		Map<String, Object> includedResult = result.included();
		Project includedProject = (Project) includedResult.get("project");
		assertEquals(project.getId(), includedProject.getId());
		assertTrue(includedProject.getName() != null);		
		List<ProjectModification> includedModifications =  (List<ProjectModification>) includedResult.get("modifications");
		assertTrue(lastModifications == includedModifications);		
	}


	private void shouldLoadProject(final Project project, final Project projectToBeReturned) {
		mockery.checking(new Expectations() {
    		{
    			one(repository).load(project);    			
				will(returnValue(projectToBeReturned));
    		}
    	});
	}

	private Project createProject(ProjectRepository repository, long id, String name) {
		final Project loadedProject = new Project(repository, modificationRepository);
		loadedProject.setId(id);
		loadedProject.setName(name);
		return loadedProject;
	}	
	
}
