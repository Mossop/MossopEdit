/*
 * $Author$
 * $RCSfile$
 * $Date$
 * $Revision$
 */
package com.blueprintit.topcoder.remoting;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import com.topcoder.shared.language.Language;

/**
 * @author Dave
 */
public class CommandListener implements Runnable
{
	private RemotelyCalledPlugin plugin;
	
	public CommandListener(RemotelyCalledPlugin plugin)
	{
		this.plugin=plugin;
		(new Thread(this)).start();
	}
	
	public void executeCommand(Command command, PluginDataStream stream) throws IOException
	{
		if (command.equals(Command.START_USING))
		{
			plugin.startUsing();
		}
		else if (command.equals(Command.STOP_USING))
		{
			plugin.stopUsing();
		}
		else if (command.equals(Command.GET_SOURCE))
		{
			stream.writeString(plugin.getSource());
		}
		else if (command.equals(Command.CLEAR_SOURCE))
		{
			plugin.clear();
		}
		else if (command.equals(Command.SET_SOURCE))
		{
			plugin.setSource(stream.readString());
		}
		else if (command.equals(Command.SET_PROBLEM))
		{
			RemoteProblemComponentModel model = new RemoteProblemComponentModel();
			model.customReadObject(stream);
			Language language = (Language)stream.readObject();
			plugin.setProblemComponent(model,language,null);
		}
		else if (command.equals(Command.DISCONNECT))
		{
			plugin.dispose();
		}
	}
	
	public void run()
	{
		ServerSocket listener;
		try
		{
			listener = new ServerSocket(6767);
		}
		catch (IOException e)
		{
			e.printStackTrace();
			return;
		}
		boolean running = true;
		while (running)
		{
			Socket applet;
			PluginDataStream stream;
			try
			{
				applet = listener.accept();
				stream = new PluginDataStream(applet.getInputStream(),applet.getOutputStream());
				System.out.println("Topcoder connected");
			}
			catch (IOException e)
			{
				e.printStackTrace();
				break;
			}
			Object data;
			while (true)
			{
				try
				{
					data = stream.readObject();
				}
				catch (IOException e)
				{
					System.out.println("Error receiving data. Closing up");
					e.printStackTrace();
					plugin.dispose();
					break;
				}

				if (!(data instanceof Command))
				{
					System.err.println("Error, received a non-command object: "+data.getClass().getName());
					break;
				}
				
				Command command = (Command)data;
				try
				{
					executeCommand(command,stream);
				}
				catch (IOException e)
				{
					e.printStackTrace();
					break;
				}
				
				if (command.equals(Command.DISCONNECT))
				{
					System.out.println("Topcoder signalled disconnect");
					break;
				}
			}
			try
			{
				applet.close();
				listener.close();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}
}
