/*
 * $Author$
 * $RCSfile$
 * $Date$
 * $Revision$
 */
package com.blueprintit.topcoder.remoting;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.topcoder.shared.netCommon.CSHandler;

/**
 * @author Dave
 */
public class PluginDataStream extends CSHandler
{
	public PluginDataStream(InputStream in, OutputStream out)
	{
		setDataInput(new DataInputStream(in));
		setDataOutput(new DataOutputStream(out));
	}
	
	protected boolean writeObjectOverride(Object arg0) throws IOException
	{
		// TODO Auto-generated method stub
		return false;
	}
}
