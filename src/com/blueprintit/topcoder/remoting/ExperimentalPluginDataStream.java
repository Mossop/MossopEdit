/*
 * $Author$
 * $RCSfile$
 * $Date$
 * $Revision$
 */
package com.blueprintit.topcoder.remoting;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.topcoder.shared.netCommon.CSReader;
import com.topcoder.shared.netCommon.CSWriter;
import com.topcoder.shared.netCommon.CustomSerializable;

/**
 * @author Dave
 */
public class ExperimentalPluginDataStream implements CSReader, CSWriter
{
	private ObjectInput in;
	private ObjectOutput out;
	
	public ExperimentalPluginDataStream(InputStream in, OutputStream out) throws IOException
	{
		super();
		setDataInput(new ObjectInputStream(in));
		setDataOutput(new ObjectOutputStream(out));
	}

	public void setDataInput(DataInput arg0)
	{
		if (arg0 instanceof ObjectInput)
		{
			in=(ObjectInput)arg0;
		}
	}

	public byte readByte() throws IOException
	{
		return in.readByte();
	}

	public short readShort() throws IOException
	{
		return in.readShort();
	}

	public int readInt() throws IOException
	{
		return in.readInt();
	}

	public long readLong() throws IOException
	{
		return in.readLong();
	}

	public boolean readBoolean() throws IOException
	{
		return in.readBoolean();
	}

	public double readDouble() throws IOException
	{
		return in.readDouble();
	}

	public String readString() throws IOException
	{
		return in.readUTF();
	}

	public byte[] readByteArray() throws IOException
	{
		int count = in.readInt();
		byte[] results = new byte[count];
		for (int loop=0; loop<count; loop++)
		{
			results[loop]=in.readByte();
		}
		return results;
	}

	public char[] readCharArray() throws IOException
	{
		int count = in.readInt();
		char[] results = new char[count];
		for (int loop=0; loop<count; loop++)
		{
			results[loop]=in.readChar();
		}
		return results;
	}

	public Object[] readObjectArray() throws IOException
	{
		int count = in.readInt();
		Object[] results = new Object[count];
		for (int loop=0; loop<count; loop++)
		{
			results[loop]=readObject();
		}
		return results;
	}

	public Object[] readObjectArray(Class arg0) throws IOException
	{
		int count = in.readInt();
		Object[] results = new Object[count];
		for (int loop=0; loop<count; loop++)
		{
			results[loop]=readObject(arg0);
		}
		return results;
	}

	public Object[][] readObjectArrayArray() throws IOException
	{
		int count = in.readInt();
		Object[][] results = new Object[count][];
		for (int loop=0; loop<count; loop++)
		{
			results[loop]=readObjectArray();
		}
		return results;
	}

	public ArrayList readArrayList() throws IOException
	{
		int count = in.readInt();
		ArrayList results = new ArrayList();
		for (int loop=0; loop<count; loop++)
		{
			results.add(readObject());
		}
		return results;
	}

	public HashMap readHashMap() throws IOException
	{
		int count = in.readInt();
		HashMap results = new HashMap();
		for (int loop=0; loop<count; loop++)
		{
			results.put(readObject(),readObject());
		}
		return results;
	}

	private Object doReadObject(Class type) throws IOException
	{
		try
		{
			Object result = type.newInstance();
			if (result instanceof CustomSerializable)
			{
				((CustomSerializable)result).customReadObject(this);
				return result;
			}
			else
			{
				throw new IOException("Do not know how to de-serialize this object");
			}
		}
		catch (IllegalAccessException e)
		{
			throw new IOException("Could not create a new instance to de-serialize the object");
		}
		catch (InstantiationException e)
		{
			throw new IOException("Could not create a new instance to de-serialize the object");			
		}
	}
	
	public Object readObject() throws IOException
	{
		int check = in.readInt();
		if (check==-1)
		{
			return null;
		}
		else if (check==0)
		{
			try
			{
				return in.readObject();
			}
			catch (ClassNotFoundException e)
			{
				throw new IOException(e.getMessage());
			}
		}
		else if (check==1)
		{
			String type = in.readUTF();
			try
			{
				doReadObject(Class.forName(type));
			}
			catch (ClassNotFoundException e)
			{
				throw new IOException(e.getMessage());
			}
		}
		throw new IOException("Invalid object header");
	}

	public Object readObject(Class type) throws IOException
	{
		int check = in.readInt();
		if (check==-1)
		{
			return null;
		}
		else if (check==0)
		{
			try
			{
				return in.readObject();
			}
			catch (ClassNotFoundException e)
			{
				throw new IOException(e.getMessage());
			}
		}
		else if (check==1)
		{
			in.readUTF();
			return doReadObject(type);
		}
		throw new IOException("Invalid object header");
	}

	public String[] readStringArray() throws IOException
	{
		int count = in.readInt();
		String[] results = new String[count];
		for (int loop=0; loop<count; loop++)
		{
			results[loop]=readString();
		}
		return results;
	}

	public void setDataOutput(DataOutput arg0)
	{
		if (arg0 instanceof ObjectOutput)
		{
			out=(ObjectOutput)arg0;
		}
	}

	public void writeByte(byte arg0) throws IOException
	{
		out.write(arg0);
	}

	public void writeShort(short arg0) throws IOException
	{
		out.writeShort(arg0);
	}

	public void writeInt(int arg0) throws IOException
	{
		out.writeInt(arg0);
	}

	public void writeLong(long arg0) throws IOException
	{
		out.writeLong(arg0);
	}

	public void writeBoolean(boolean arg0) throws IOException
	{
		out.writeBoolean(arg0);
	}

	public void writeDouble(double arg0) throws IOException
	{
		out.writeDouble(arg0);
	}

	public void writeString(String arg0) throws IOException
	{
		out.writeUTF(arg0);
	}

	public void writeByteArray(byte[] arg0) throws IOException
	{
		out.writeInt(arg0.length);
		for (int loop=0; loop<arg0.length; loop++)
		{
			out.writeByte(arg0[loop]);
		}
	}

	public void writeCharArray(char[] arg0) throws IOException
	{
		out.writeInt(arg0.length);
		for (int loop=0; loop<arg0.length; loop++)
		{
			out.writeChar(arg0[loop]);
		}
	}

	public void writeObjectArray(Object[] arg0) throws IOException
	{
		out.writeInt(arg0.length);
		for (int loop=0; loop<arg0.length; loop++)
		{
			writeObject(arg0[loop]);
		}
	}

	public void writeObjectArrayArray(Object[][] arg0) throws IOException
	{
		out.writeInt(arg0.length);
		for (int loop=0; loop<arg0.length; loop++)
		{
			writeObjectArray(arg0[loop]);
		}
	}

	public void writeArrayList(ArrayList arg0) throws IOException
	{
		out.writeInt(arg0.size());
		for (int loop=0; loop<arg0.size(); loop++)
		{
			writeObject(arg0.get(loop));
		}
	}

	/* (non-Javadoc)
	 * @see com.topcoder.shared.netCommon.CSWriter#writeHashMap(java.util.HashMap)
	 */
	public void writeHashMap(HashMap arg0) throws IOException
	{
		out.writeInt(arg0.size());
		Iterator loop = arg0.entrySet().iterator();
		while (loop.hasNext())
		{
			Map.Entry entry = (Map.Entry)loop.next();
			writeObject(entry.getKey());
			writeObject(entry.getValue());
		}
	}

	public void writeObject(Object arg0) throws IOException
	{
		if (arg0==null)
		{
			out.writeInt(-1);
		}
		else
		{
			if (arg0 instanceof CustomSerializable)
			{
				out.writeInt(1);
				out.writeUTF(arg0.getClass().getName());
				((CustomSerializable)arg0).customWriteObject(this);
			}
			else
			{
				out.writeInt(0);
				out.writeObject(arg0);
			}
		}
	}

	public void writeStringArray(String[] arg0) throws IOException
	{
		out.writeInt(arg0.length);
		for (int loop=0; loop<arg0.length; loop++)
		{
			out.writeUTF(arg0[loop]);
		}
	}
}
