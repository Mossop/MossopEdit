/*
 * $Author$
 * $RCSfile$
 * $Date$
 * $Revision$
 */
package com.blueprintit.topcoder.remoting;

import java.awt.BorderLayout;
import java.awt.Font;
import java.net.Socket;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import com.blueprintit.topcoder.AbstractEditorPlugin;
import com.topcoder.client.contestApplet.common.LocalPreferences;
import com.topcoder.client.contestant.ProblemComponentModel;
import com.topcoder.shared.language.Language;
import com.topcoder.shared.problem.Renderer;

/**
 * @author Dave
 */
public class RemotePlugin extends AbstractEditorPlugin
{
	private JTextArea status;
	private JPanel panel;
	private PluginDataStream stream;
	private String cachedsource;

	public RemotePlugin()
	{
		status = new JTextArea("RemoteEditorPlugin started.\n");
		status.setEditable(false);

		panel = new JPanel(new BorderLayout());
		panel.add(new JScrollPane(status),BorderLayout.CENTER);
		panel.validate();		
	}
	
	public JPanel getEditorPanel()
	{
		LocalPreferences prefs = LocalPreferences.getInstance();
		status.setForeground(prefs.getColor(LocalPreferences.PROBLEMFORE));
		status.setBackground(prefs.getColor(LocalPreferences.PROBLEMBACK));
		status.setFont(new Font(prefs.getFont(LocalPreferences.PROBLEMFONT),Font.PLAIN,prefs.getFontSize(LocalPreferences.PROBLEMFONTSIZE)));

		return panel;
	}

	public void log(String message)
	{
		status.append(message+"\n");
		status.repaint();
	}
	
	public void log(String message, Throwable e)
	{
		status.append(message+"\n");
		status.append(e.getMessage()+"\n");
		status.repaint();
	}
	
	public void clear()
	{
		try
		{
			stream.writeObject(Command.CLEAR_SOURCE);
		}
		catch (Exception e)
		{
			log("Unable to communicate with remote plugin",e);
		}
	}

	public void dispose()
	{
		try
		{
			stream.writeObject(Command.DISCONNECT);
		}
		catch (Exception e)
		{
			log("Unable to communicate with remote plugin",e);
		}
	}

	public Boolean isCacheable()
	{
		return Boolean.TRUE;
	}

	public void startUsing()
	{
		try
		{
			if (stream==null)
			{
				Socket connection = new Socket("localhost",6767);
				stream = new PluginDataStream(connection.getInputStream(),connection.getOutputStream());
				stream.writeInt(1);
				int version = stream.readInt();
				if (version!=1)
				{
					log("Remote plugin listener is an invalid version. Please update your applet plugin");
					return;
				}
				String ident = stream.readString();
				log("Connected to remote plugin: "+ident);
			}
			stream.writeObject(Command.START_USING);
		}
		catch (Exception e)
		{
			log("unable to connect to remote plugin",e);
		}
	}

	public void stopUsing()
	{
		try
		{
			stream.writeObject(Command.STOP_USING);
		}
		catch (Exception e)
		{
			log("Unable to communicate with remote plugin",e);
		}
	}

	public void setProblemComponent(ProblemComponentModel component, Language language, Renderer renderer)
	{
		log("getComponentTypeID: "+component.getComponentTypeID());
		log("getID: "+component.getID());
		log("getPoints: "+component.getPoints());
		log("problem.getProblemID: "+component.getProblem().getProblemID());
		log("problem.getProblemType: "+component.getProblem().getProblemType());
		log("round.getContestName: "+component.getProblem().getRound().getContestName());
		log("round.getRoundName: "+component.getProblem().getRound().getRoundName());
		log("round.getRoundID: "+component.getProblem().getRound().getRoundID());
		log("round.getRoundType: "+component.getProblem().getRound().getRoundType());
		log("problem.getProblem().getName()"+component.getProblem().getProblem().getName());
		log("problem.getProblem().getProblemId()"+component.getProblem().getProblem().getProblemId());
		log("problem.getProblem().getProblemTypeID()"+component.getProblem().getProblem().getProblemTypeID());
		try
		{
			stream.writeObject(Command.SET_PROBLEM);
			stream.writeLong(component.getID().longValue());
			stream.writeInt(component.getComponentTypeID().intValue());
			stream.writeDouble(component.getPoints().doubleValue());
			stream.writeObject(component.getComponentChallengeData());
			stream.writeObject(component.getComponent());
			stream.writeObject(language);
		}
		catch (Exception e)
		{
			log("Unable to communicate with remote plugin",e);
		}
	}
	
	public String getSource()
	{
		try
		{
			stream.writeObject(Command.GET_SOURCE);
			cachedsource=stream.readString();
		}
		catch (Exception e)
		{
			log("Unable to communicate with remote plugin",e);
		}
		return cachedsource;
	}

	public void setSource(String source)
	{
		cachedsource=source;
		try
		{
			stream.writeObject(Command.SET_SOURCE);
			stream.writeString(source);
		}
		catch (Exception e)
		{
			log("Unable to communicate with remote plugin",e);
		}
	}
}
