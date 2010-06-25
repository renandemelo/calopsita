package br.com.caelum.calopsita.logic;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.Before;
import org.junit.Test;

import br.com.caelum.calopsita.controller.ProjectsController;
import br.com.caelum.calopsita.infra.vraptor.SessionUser;
import br.com.caelum.calopsita.mocks.MockHttpSession;
import br.com.caelum.calopsita.model.Project;
import br.com.caelum.calopsita.model.ProjectModification;
import br.com.caelum.calopsita.model.User;
import br.com.caelum.calopsita.persistence.dao.ProjectModificationDao;
import br.com.caelum.calopsita.repository.ProjectModificationRepository;
import br.com.caelum.calopsita.repository.ProjectRepository;
import br.com.caelum.calopsita.repository.UserRepository;
import br.com.caelum.vraptor.util.test.MockResult;
import br.com.caelum.vraptor.util.test.MockValidator;
import br.com.caelum.vraptor.validator.ValidationException;

public class ProjectTest {
    private Mockery mockery;
    private ProjectsController logic;
    private ProjectRepository repository;
	private User currentUser;
	private UserRepository userRepository;
	private ProjectModificationRepository projectModificationRepository;

    @Before
    public void setUp() throws Exception {
        mockery = new Mockery();
        repository = mockery.mock(ProjectRepository.class);
        userRepository = mockery.mock(UserRepository.class);
        projectModificationRepository = mockery.mock(ProjectModificationRepository.class);
        currentUser = currentUser();

		SessionUser sessionUser = new SessionUser(new MockHttpSession());
		sessionUser.setUser(currentUser);

		logic = new ProjectsController(new MockValidator(), new MockResult(), sessionUser);
    }


    @Test
    public void listingAProject() throws Exception {
        Project project = givenThatOnlyExistsOneProjectForCurrentUser();
        List<Project> projects = whenIListProjects();
        thenTheLogicShouldExposeOnlyTheProject(projects, project);
        mockery.assertIsSatisfied();
    }
    @Test
    public void saveAProject() throws Exception {
    	Project project = givenAProject();

    	shouldSaveTheProjectOnTheRepository(project);

    	whenISaveTheProject(project);

    	assertThat(project.getOwner(), is(currentUser));
    	mockery.assertIsSatisfied();
    }
    @Test
    public void addingColaboratorsToAProject() throws Exception {
    	Project project = givenAProject();

    	User user = givenAUser();

    	shouldReloadAndUpdateTheProject(project);
    	shouldLoadColaborator(user);
    	whenIAddTheUserToTheProject(user, project);

    	thenTheProjectWillContainTheUserAsColaborator(project, user);
    	mockery.assertIsSatisfied();
    }

    private void shouldLoadColaborator(final User user) {
    	mockery.checking(new Expectations() {
			{
				one(userRepository).find(user.getLogin());
				will(returnValue(user));
				
				allowing(projectModificationRepository).add(with(any(ProjectModification.class)));
				
				allowing(repository).listModificationsFrom(with(any(Project.class)));
				will(returnValue(new ArrayList<ProjectModification>()));
			}
		});
	}


	@Test
	public void removingAProjectOwnedByMe() throws Exception {
		Project project = givenAProject();

		Project loaded = givenProjectIsOwnedBy(project, currentUser);

		shouldRemoveFromRepository(loaded);

		whenIRemoveTheProject(project);

		mockery.assertIsSatisfied();
	}
    @Test(expected=ValidationException.class)
    public void removingAProjectOwnedByOthers() throws Exception {
    	Project project = givenAProject();

    	Project loaded = givenProjectIsOwnedBy(project, givenAUser());

    	shouldNotRemoveFromRepository(loaded);

    	whenIRemoveTheProject(project);

    	mockery.assertIsSatisfied();
    }

    @Test
    public void editingAProjectOwnedByMe() throws Exception {
    	Project project = givenAProject();
    	project.setDescription("New description");

    	Project loaded = givenProjectIsOwnedBy(project, currentUser);

    	whenIEditTheProject(project);

    	assertThat(loaded.getDescription(), is("New description"));
    	mockery.assertIsSatisfied();
    }

	@Test(expected=ValidationException.class)
    public void editingAProjectOwnedByOthers() throws Exception {
    	Project project = givenAProject();
    	project.setDescription("Anything");
    	Project loaded = givenProjectIsOwnedBy(project, givenAUser());

    	whenIEditTheProject(project);

    	assertThat(loaded.getDescription(), is(not("Anything")));
    	mockery.assertIsSatisfied();
    }

	private void whenIEditTheProject(Project project) {
		logic.update(project);
	}

	private void shouldNotRemoveFromRepository(final Project project) {
		mockery.checking(new Expectations() {
			{
				never(repository).remove(project);
			}
		});

	}

	private Project givenProjectIsOwnedBy(final Project project, final User user) {

		final Project result = new Project(repository, projectModificationRepository);
		result.setOwner(user);
		mockery.checking(new Expectations() {
			{
				one(repository).load(project);
				will(returnValue(result));
			}
		});
		return result;
	}

	private void shouldRemoveFromRepository(final Project project) {

		mockery.checking(new Expectations() {
			{
				one(repository).remove(project);
			}
		});
	}

	private void whenIRemoveTheProject(Project project) {
		logic.delete(project);
	}

	private void shouldReloadAndUpdateTheProject(final Project project) {

		mockery.checking(new Expectations() {
			{
				one(repository).load(project);
				will(returnValue(project));

			}
		});
	}

	private void thenTheProjectWillContainTheUserAsColaborator(Project project, User user) {
		assertThat(project.getColaborators(), hasItem(user));
	}

	private void whenIAddTheUserToTheProject(User user, Project project) {
		logic.addColaborator(project, user);
	}


	private User givenAUser() {
		return new User(userRepository);
	}
    private void whenISaveTheProject(Project project) {
    	logic.save(project);
	}

	private void shouldSaveTheProjectOnTheRepository(final Project project) {
    	mockery.checking(new Expectations() {
    		{
    			one(repository).add(project);
    		}
    	});
	}

	private Project givenAProject() {
		Project project = new Project(repository, projectModificationRepository);
		project.setName("A project");
		return project;
	}

	private void thenTheLogicShouldExposeOnlyTheProject(List<Project> projects, Project project) {
        assertThat(projects, is(notNullValue()));
        assertThat(projects.size(), is(1));
        assertThat(projects, hasItem(project));
    }

	private List<Project> whenIListProjects() {
        return logic.list();
    }

    private Project givenThatOnlyExistsOneProjectForCurrentUser() {
        final Project project = new Project(repository, projectModificationRepository);
        mockery.checking(new Expectations() {
            {
                one(userRepository).listAllFrom(currentUser);
                will(returnValue(Collections.singletonList(project)));
            }
        });
        return project;
    }

    private User currentUser() {
        final User user = new User(userRepository);
        String login = "caue";
        user.setLogin(login);
        user.setEmail(login + "@caelum.com.br");
        user.setName(login);
        user.setPassword(login);

        return user;
    }
}
