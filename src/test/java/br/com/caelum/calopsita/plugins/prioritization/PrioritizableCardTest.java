package br.com.caelum.calopsita.plugins.prioritization;

import org.junit.Assert;
import org.junit.Test;

import br.com.caelum.calopsita.mocks.ProjectMock;
import br.com.caelum.calopsita.model.Card;

public class PrioritizableCardTest {

	private Card card;
	private ProjectMock project;
	
	
	@Test
	public void testChangeInPriorityWithLog(){
		PrioritizableCard prioritazableCard = new PrioritizableCard();
		
		project = new ProjectMock();
		card = new Card();
		card.setName("DoIt");
		card.setProject(project);
		prioritazableCard.setCard(card);
		prioritazableCard.setPriority(2);
		
		prioritazableCard.changePriority(5);
		
		Assert.assertEquals("Changed priorization of card 'DoIt' from 2 to 5", project.getAddedModificationDescription());
		Assert.assertEquals(5, prioritazableCard.getPriority());
		
		
		//this.card.getProject().addModification("Changed card " + card.getId() + " priority from " + this.priority + " to " + priority);
	}
	
	@Test
	public void testChangeInPriorityWithLogWhenInitialPriorityIsNotSet(){
		PrioritizableCard prioritazableCard = new PrioritizableCard();
		
		project = new ProjectMock();
		card = new Card();
		card.setName("DoIt");
		card.setProject(project);
		prioritazableCard.setCard(card);
		
		prioritazableCard.changePriority(5);
		
		Assert.assertEquals("Changed priorization of card 'DoIt' from 0 to 5", project.getAddedModificationDescription());
		Assert.assertEquals(5, prioritazableCard.getPriority());
		
		
		//this.card.getProject().addModification("Changed card " + card.getId() + " priority from " + this.priority + " to " + priority);
	}
	
	
}
