/*
 * $Author$
 * $RCSfile$
 * $Date$
 * $Revision$
 */
package com.blueprintit.topcoder;

import com.blueprintit.topcoder.remoting.CommandListener;
import com.blueprintit.topcoder.remoting.RemotelyCalledPlugin;
import com.topcoder.client.contestant.ProblemComponentModel;
import com.topcoder.shared.language.Language;
import com.topcoder.shared.problem.DataType;
import com.topcoder.shared.problem.Renderer;

/**
 * @author Dave
 */
public class TestPlugin implements RemotelyCalledPlugin
{
	public TestPlugin()
	{
		new CommandListener(this);
	}
	
	public String getSource()
	{
		System.out.println("getSource called");
		return "";
	}

	public void setSource(String source)
	{
		System.out.println("setSource called");
	}

	public void clear()
	{
		System.out.println("clear called");
	}

	public void setProblemComponent(ProblemComponentModel component, Language language, Renderer renderer)
	{
		System.out.println("setProblemComponent called");
		System.out.print(component.getReturnType().getDescriptor(language)+" ");
		System.out.print(component.getClassName()+".");
		System.out.print(component.getMethodName()+"(");
		String[] names = component.getParamNames();
		DataType[] types = component.getParamTypes();
		StringBuffer params = new StringBuffer();
		for (int loop=0; loop<names.length; loop++)
		{
			System.out.print(types[loop].getDescriptor(language)+" ");
			System.out.print(names[loop]);
			if ((loop+1)<names.length)
			{
				System.out.print(", ");
			}
		}
		System.out.println(")");
	}

	public void startUsing()
	{
		System.out.println("startUsing called");
	}

	public void stopUsing()
	{
		System.out.println("stopUsing called");
	}

	public void dispose()
	{
		System.out.println("dispose called");
	}

	public static void main(String[] args)
	{
		new TestPlugin();
	}
}
