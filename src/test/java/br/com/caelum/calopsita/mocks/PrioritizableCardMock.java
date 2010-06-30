package br.com.caelum.calopsita.mocks;

import br.com.caelum.calopsita.plugins.prioritization.PrioritizableCard;

public class PrioritizableCardMock extends PrioritizableCard{
		
	private int changedPriority;

	@Override
	public void changePriority(int newPriority) {
		changedPriority = newPriority;
	}

	public int getChangedPriority() {
		return changedPriority;
	}
	
}
