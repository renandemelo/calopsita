package br.com.caelum.calopsita.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import org.hibernate.validator.NotNull;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;

import br.com.caelum.calopsita.persistence.dao.ProjectModificationDao;
import br.com.caelum.calopsita.repository.ProjectModificationRepository;
import br.com.caelum.calopsita.repository.ProjectRepository;
import br.com.caelum.calopsita.util.DateTimeGenerator;
import br.com.caelum.calopsita.util.DateTimeGeneratorImpl;

@Entity
public class Project implements Identifiable {
    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    private String name;
    private String description;

    @ManyToOne
    private User owner;

    @ManyToMany
    private List<User> colaborators;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    private List<Iteration> iterations;
    
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    private List<ProjectModification> modifications;
    
    public Project(ProjectRepository repository, ProjectModificationRepository modificationRepository) {
    	this.repository = repository;
    	this.modificationRepository = modificationRepository;
    }
    
//    public Project(ProjectRepository repository) {
//		this.repository = repository;
//	}

    public Project() {
	}
    @Transient
    private ProjectRepository repository;
    @Transient
	private DateTimeGenerator dateTimeGenerator;
    @Transient
    private ProjectModificationRepository modificationRepository;

    public void setDateTimeGenerator(DateTimeGenerator dateTimeGenerator) {
		this.dateTimeGenerator = dateTimeGenerator;
	}

	private ProjectRepository getRepository() {
    	if (repository == null) {
    		throw new IllegalStateException("Repository was not set. You should inject it first");
    	}
    	return repository;
    }

    public List<Card> getLastAddedCards() {
    	return repository.listLastAddedCards(this);
    }
    public List<Iteration> getIterations() {
    	if (iterations == null) {
    		iterations = getRepository().listIterationsFrom(this);
    	}
        return iterations;
    }

    public List<Card> getCardsWithoutIteration() {
    	return getRepository().planningCardsWithoutIteration(this);
    }

    public Iteration getCurrentIteration() {
    	return repository.getCurrentIterationFromProject(this);
    }
    public Project load() {
    	return getRepository().load(this);
    }

    public Project refresh() {
    	return getRepository().refresh(this);
    }
    public void delete() {
    	getRepository().remove(this);
    }

    public List<User> getUnrelatedUsers() {
    	return getRepository().listUnrelatedUsers(this);
    }

    public List<Card> getToDoCards() {
    	return getRepository().listTodoCardsFrom(this);
    }
    public List<Card> getAllToDoCards() {
    	return getRepository().listAllTodoCardsFrom(this);
    }

    public List<Card> getAllRootCards() {
    	return getRepository().listRootCardsFrom(this);
    }

    public List<CardType> getCardTypes() {
    	return getRepository().listCardTypesFrom(this);
    }

    public void save() {
    	getRepository().add(this);
    }
    
    public void update() {
    	getRepository().update(this);
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public List<User> getColaborators() {
    	if (colaborators == null) {
			colaborators = new ArrayList<User>();
		}

        return colaborators;
    }

    public void setColaborators(List<User> colaborators) {
        this.colaborators = colaborators;
    }

	public void addModification(String description) {
		ProjectModification projectModification = new ProjectModification(this.modificationRepository, this);
		projectModification.setDescription(description);
		LocalDateTime now = getNow();
		projectModification.setDateTime(now);
		projectModification.save();
		this.getModifications().add(projectModification);
	}

	private LocalDateTime getNow() {
		if(dateTimeGenerator == null)
			dateTimeGenerator = new DateTimeGeneratorImpl();
		return dateTimeGenerator.getNow();
	}

	public List<ProjectModification> getLastModifications() {
		List<ProjectModification> modificationsList = new ArrayList<ProjectModification>(getModifications());
		Collections.sort(modificationsList,new Comparator<ProjectModification>() {
		 	public int compare(ProjectModification o1, ProjectModification o2) {
				return o2.getDateTime().compareTo(o1.getDateTime());
			};
		});		
		if(modificationsList.size()>30) {
			return modificationsList.subList(0, 30);	
		}
		return modificationsList;
		
	}

	public void setModifications(List<ProjectModification> modifications) {
		this.modifications = modifications;
	}

	public List<ProjectModification> getModifications() {
		if (modifications == null) {
			modifications = getRepository().listModificationsFrom(this);
    	}
		return modifications;
	}

	public void setModificationRepository(ProjectModificationRepository modificationRepository) {
		this.modificationRepository = modificationRepository;
	}

	public ProjectModificationRepository getModificationRepository() {
		return modificationRepository;
	}

}
