package br.com.caelum.calopsita.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;

import br.com.caelum.calopsita.repository.ProjectModificationRepository;

@Entity
public class ProjectModification {
	@Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private Project project;
    
    private String description;

    @Type(type = "org.joda.time.contrib.hibernate.PersistentLocalDateTime")
	private LocalDateTime dateTime;
    
    @Transient
    private ProjectModificationRepository repository;

    public ProjectModification() {
	}
    
    public ProjectModification (ProjectModificationRepository repository) {
    	this.repository = repository;
    }
    
	public ProjectModification(ProjectModificationRepository modificationRepository, Project project) {
		this.repository = modificationRepository;
		this.project = project;
	}

	public String getDescription() {
		return this.description;
	}


	public void setDescription(String description) {
		this.description = description;
	}


	public void setProject(Project project) {
		this.project = project;
	}


	public Project getProject() {
		return project;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public Long getId() {
		return id;
	}


	public LocalDateTime getDateTime() {
		return this.dateTime;
	}


	public void setDateTime(LocalDateTime date) {
		this.dateTime = date;
	}


	public void setRepository(ProjectModificationRepository repository) {
		this.repository = repository;
	}


	public ProjectModificationRepository getRepository() {
		return repository;
	}
	
	public void save() {
		this.repository.add(this);
	}
	
	public void load() {
		this.repository.load(this);
	}
}
