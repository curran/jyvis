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

import java.awt.Container;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;

/**
 * A UI for stepping through sessions.
 * 
 * @author Curran Kelleher
 * 
 */
@SuppressWarnings("serial")
public class ReplayUI extends JFrame {

	/**
	 * Construct a UI for the specified session history
	 * 
	 * @param scripts
	 *            the list of scripts which define the session to replay
	 */
	public ReplayUI(List<SessionEntry> session) {
		final SessionReplayer sessionReplayer = new SessionReplayer(session);

		sessionReplayer.initializeSessionReplay();

		// make the "Play Next Script" button
		final JButton nextButton = new JButton("Play Next Event");
		// make the "Play All Scripts" button
		final JButton allButton = new JButton("Play All Events");

		nextButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				boolean sessionIsFinished = sessionReplayer.executeNextEvent();
				if (sessionIsFinished)
					setFinishedAppearance(nextButton, allButton);
			}
		});

		allButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				sessionReplayer.executeAllEvents();
				setFinishedAppearance(nextButton, allButton);
			}
		});
		// add the buttons, set up the frame
		Container contentPane = getContentPane();
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.X_AXIS));
		contentPane.add(nextButton);
		contentPane.add(allButton);
		setBounds(0, 0, 0, 0);
		setVisible(true);
		setVisible(false);
		Rectangle minimalFrameBounds = getBounds();
		setBounds(0, 0, minimalFrameBounds.width + 100,
				minimalFrameBounds.height + 30);
		setVisible(true);
	}

	private void setFinishedAppearance(JButton nextButton, JButton allButton) {
		nextButton.setEnabled(false);
		allButton.setEnabled(false);
		setTitle("Finished session");
		addWindowListener(new WindowAdapter() {
			public void windowDeactivated(WindowEvent arg0) {
				dispose();
			}
		});
	}
}
/*
 * CVS Log
 * 
 * $Log: ReplayUI.java,v $
 * Revision 1.1  2007/08/15 17:59:22  curran
 * Initial commit to SourceForge
 * Revision 1.1 2007/08/01 17:56:36 ckellehe Implemented
 * session saving and loading
 * 
 * Revision 1.4 2007/06/29 14:34:38 ckellehe Made replay box sized more
 * correctly and always on top Revision 1.3 2007/06/27 20:03:08 ckellehe Fixed a
 * bug where the internal frame created fr a visualization by a script was not
 * getting displayed properly.
 * 
 * Revision 1.2 2007/06/27 15:46:34 ckellehe Got sessions working Revision 1.1
 * 2007/06/27 14:49:02 ckellehe Renamed some scripting related files, created
 * general XML file IO, changed color and shape map to use it, restructured
 * sessions
 * 
 * Revision 1.4 2007/06/15 18:01:27 ckellehe Added an additional String
 * parameter to ScriptBottleneck.exec, specifying what language interpreter to
 * use for executing the script
 * 
 * Revision 1.3 2007/06/14 15:50:56 ckellehe Renamed JythonBottleneck to
 * ScriptBottleneck
 * 
 * Revision 1.2 2007/06/08 15:01:45 ckellehe Initial Creation
 * 
 */
