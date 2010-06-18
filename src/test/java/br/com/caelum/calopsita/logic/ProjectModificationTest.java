package br.com.caelum.calopsita.logic;

import static org.junit.Assert.*;

import java.util.List;
import java.util.Map;

import org.jmock.Expectations;
import org.jmock.Mockery;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import br.com.caelum.calopsita.controller.ProjectModificationController;
import br.com.caelum.calopsita.controller.ProjectsController;
import br.com.caelum.calopsita.infra.vraptor.SessionUser;
import br.com.caelum.calopsita.mocks.MockHttpSession;
import br.com.caelum.calopsita.model.Project;
import br.com.caelum.calopsita.model.ProjectModification;
import br.com.caelum.calopsita.repository.ProjectRepository;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.util.test.MockResult;

@SuppressWarnings("unchecked")
public class ProjectModificationTest {
	
	
	private Mockery mockery;
	private ProjectRepository repository;

	@Before
	public void preparaMocks(){
		mockery = new Mockery();
        repository = mockery.mock(ProjectRepository.class);		
	}
	
	
	@Test
	public void testaAlgo(){			
		final Project project = createProject(repository, 1L, null);		
		final Project loadedProject = createProject(null, 1L, "Calopsita");
		
		loadedProject.addModification("Creating card BAGACA");
		loadedProject.addModification("Creating card BAGACA 2");
		
    	shouldLoadProject(project, loadedProject);
                
		Result result = new MockResult();
		ProjectModificationController controller = new ProjectModificationController(result);		
				
		controller.list(project);
		Map<String, Object> includedResult = result.included();
		Project includedProject = (Project) includedResult.get("project");
		assertEquals(project.getId(), includedProject.getId());
		assertTrue(includedProject.getName() != null);		
		List<ProjectModification> includedModifications =  (List<ProjectModification>) includedResult.get("modifications");
		assertEquals(includedModifications.get(0).getDescription(),"Creating card BAGACA");
		assertEquals(includedModifications.get(1).getDescription(),"Creating card BAGACA 2");
		
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
		final Project loadedProject = new Project(repository);
		loadedProject.setId(id);
		loadedProject.setName(name);
		return loadedProject;
	}	
	
}
