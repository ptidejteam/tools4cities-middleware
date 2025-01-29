package ca.concordia.encs.citydata.core;

/**
 *
 * This implements features common to all Runners, such as state control
 * (possibility to mark Runner as done after work is finished)
 */
public abstract class AbstractRunner extends MiddlewareEntity implements IRunner {

	private boolean isDone = false;

	public AbstractRunner() {
		this.setMetadata("role", "runner");
	}

	@Override
	public boolean isDone() {
		return this.isDone;
	}

	@Override
	public void setAsDone() {
		this.isDone = true;
	}

}
