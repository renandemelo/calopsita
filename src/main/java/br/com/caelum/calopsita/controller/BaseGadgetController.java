package br.com.caelum.calopsita.controller;

import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

@Resource
public class BaseGadgetController {

	private Result result;


	public BaseGadgetController(Result result) {
		this.result = result;
	}
		
	@Path(value="/projects/{a}/cards/{b}/gadgets/{c}",priority=Integer.MAX_VALUE)
	public void defaultGadget() {
		result.use(Results.nothing());
	}
}
