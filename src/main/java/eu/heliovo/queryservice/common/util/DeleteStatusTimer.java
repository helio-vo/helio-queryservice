/* #ident	"%W%" */
package eu.heliovo.queryservice.common.util;

import java.util.TimerTask;

public abstract class DeleteStatusTimer extends TimerTask {

	public DeleteStatusTimer(  ) {
		
	}

	public final void run() {
		execute();
	}

	protected abstract void execute();
}

