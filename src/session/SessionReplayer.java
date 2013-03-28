/******************************************************************************
 * Copyright (C) 2007  Institute for Visualization and Perception Research,
 *                     University of Massachusetts Lowell
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *******************************************************************************/
package session;

import java.util.List;

import jyVis.JyVis;
import jyVis.widgets.JLPeriodicUpdateThread;
import scripting.ScriptBottleneck;

/**
 * A utility class for replaying sessions
 * 
 * @author Curran Kelleher
 * 
 */
public class SessionReplayer {
	List<SessionEntry> session;

	/**
	 * The current index of the replaying session in the list of scripts.
	 */
	int i = 0;

	/**
	 * Construct a session replayer which will replay the
	 * 
	 * @param session
	 */
	public SessionReplayer(List<SessionEntry> session) {
		this.session = session;
	}

	public boolean executeNextEvent() {
		boolean sessionIsFinished = i >= session.size();
		if (!sessionIsFinished)
			ScriptBottleneck.exec(session.get(i).language,
					session.get(i).script);
		i++;
		return sessionIsFinished;
	}

	public void executeAllEvents() {
		JLPeriodicUpdateThread.performUpdates = false;
		while (i < session.size())
			executeNextEvent();
		JLPeriodicUpdateThread.performUpdates = true;
	}

	/**
	 * Initiates the session replay. This entails resetting the system state.
	 * 
	 */
	public void initializeSessionReplay() {
		JyVis.resetSystemState();
	}

}
