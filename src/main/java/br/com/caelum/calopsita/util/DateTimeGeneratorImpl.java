package br.com.caelum.calopsita.util;

import org.joda.time.LocalDateTime;

import br.com.caelum.vraptor.ioc.Component;

@Component
public class DateTimeGeneratorImpl implements DateTimeGenerator{

	@Override
	public LocalDateTime getNow() {
		return new LocalDateTime();
	}
	
}
