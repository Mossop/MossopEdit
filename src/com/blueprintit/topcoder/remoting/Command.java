/*
 * $Author$
 * $RCSfile$
 * $Date$
 * $Revision$
 */
package com.blueprintit.topcoder.remoting;

import java.io.IOException;
import java.io.ObjectStreamException;

import com.topcoder.shared.netCommon.CSReader;
import com.topcoder.shared.netCommon.CSWriter;
import com.topcoder.shared.netCommon.CustomSerializable;

/**
 * @author Dave
 */
public class Command implements CustomSerializable
{
	public static final Command DISCONNECT = new Command(-2);
	public static final Command STOP_USING = new Command(-1);
	public static final Command START_USING = new Command(0);
	public static final Command GET_SOURCE = new Command(1);
	public static final Command SET_SOURCE = new Command(2);
	public static final Command CLEAR_SOURCE = new Command(3);
	public static final Command SET_PROBLEM = new Command(4);
	
	private int value;
	
	public Command()
	{
	}
	
	private Command(int value)
	{
		this.value=value;
	}

	public int getValue()
	{
		return value;
	}
	
	public boolean equals(Object o)
	{
		if (o == this)
		{
			return true;
		}
		if (!(o instanceof Command))
		{
			return false;
		}
		Command other = (Command) o;
		return getValue() == other.getValue();
	}

	public void customWriteObject(CSWriter writer) throws IOException
	{
		writer.writeInt(value);
	}

	public void customReadObject(CSReader reader) throws IOException, ObjectStreamException
	{
		value=reader.readInt();
	}
}
