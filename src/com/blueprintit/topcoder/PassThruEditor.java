package com.blueprintit.topcoder;

import com.topcoder.shared.language.Language;
import com.topcoder.client.contestant.ProblemComponentModel;
import com.topcoder.shared.problem.Renderer;
import java.lang.reflect.Method;
import java.util.List;
import javax.swing.JPanel;

public class PassThruEditor extends AbstractEditorPlugin
{
	private Object plugin;
	
	private PassThruEditor(Object target)
	{
		plugin=target;
	}
	
	public static AbstractEditorPlugin getPlugin(String classname)
	{
		try
		{
			Class pluginclass = Class.forName(classname);
			Object plugin = pluginclass.newInstance();
			if (plugin instanceof AbstractEditorPlugin)
			{
				return (AbstractEditorPlugin)plugin;
			}
			Method check = pluginclass.getMethod("getEditorPanel",null);
			if (!check.getReturnType().equals(JPanel.class))
			{
				return null;
			}
			check = pluginclass.getMethod("getSource",null);
			if (!check.getReturnType().equals(String.class))
			{
				return null;
			}
			check = pluginclass.getMethod("setSource",new Class[] {String.class});
			return new PassThruEditor(plugin);
		}
		catch (Exception e)
		{
			return null;
		}
	}

	public JPanel getEditorPanel()
	{
		try
		{
			Method call = plugin.getClass().getMethod("getEditorPlugin",null);
			return (JPanel)call.invoke(plugin,null);
		}
		catch (Exception e)
		{
		}
		return null;
	}
		
	public String getSource()
	{
		try
		{
			Method call = plugin.getClass().getMethod("getSource",null);
			return (String)call.invoke(plugin,null);
		}
		catch (Exception e)
		{
		}
		return null;
	}
		
	public void setSource(String source)
	{
		try
		{
			Method call = plugin.getClass().getMethod("setSource",new Class[] {String.class});
			call.invoke(plugin,new Object[] {source});
		}
		catch (Exception e)
		{
		}
	}
		
	public void configure()
	{
		try
		{
			Method call = plugin.getClass().getMethod("configure",null);
			call.invoke(plugin,null);
		}
		catch (Exception e)
		{
		}
	}
		
	public void clear()
	{
		try
		{
			Method call = plugin.getClass().getMethod("clear",null);
			call.invoke(plugin,null);
		}
		catch (Exception e)
		{
		}
	}
		
	public void setTextEnabled(Boolean enable)
	{
		try
		{
			Method call = plugin.getClass().getMethod("setTextEnabled",new Class[] {Boolean.class});
			call.invoke(plugin,new Object[] {enable});
		}
		catch (Exception e)
		{
		}
	}
		
	public void setProblemComponent(ProblemComponentModel component, Language language, Renderer renderer)
	{
		try
		{
			Method call = plugin.getClass().getMethod("setProblemComponent",new Class[] {ProblemComponentModel.class,Language.class,Renderer.class});
			call.invoke(plugin,new Object[] {component,language,renderer});
		}
		catch (Exception e)
		{
		}
	}
		
	public void setLanguage(Integer languageType)
	{
		try
		{
			Method call = plugin.getClass().getMethod("setLanguage",new Class[] {Integer.class});
			call.invoke(plugin,new Object[] {languageType});
		}
		catch (Exception e)
		{
		}
	}
		
	public void setProblem(String problemText)
	{
		try
		{
			Method call = plugin.getClass().getMethod("setProblem",new Class[] {String.class});
			call.invoke(plugin,new Object[] {problemText});
		}
		catch (Exception e)
		{
		}
	}
		
	public void setSignature(String className, String methodName, List parmTypes, String rcType)
	{
		try
		{
			Method call = plugin.getClass().getMethod("setSignature",new Class[] {String.class,String.class,List.class,String.class});
			call.invoke(plugin,new Object[] {className,methodName,parmTypes,rcType});
		}
		catch (Exception e)
		{
		}
	}
		
	public void setName(String name)
	{
		try
		{
			Method call = plugin.getClass().getMethod("setName",new Class[] {String.class});
			call.invoke(plugin,new Object[] {name});
		}
		catch (Exception e)
		{
		}
	}
		
	public void install()
	{
		try
		{
			Method call = plugin.getClass().getMethod("install",null);
			call.invoke(plugin,null);
		}
		catch (Exception e)
		{
		}
	}
		
	public void uninstall()
	{
		try
		{
			Method call = plugin.getClass().getMethod("uninstall",null);
			call.invoke(plugin,null);
		}
		catch (Exception e)
		{
		}
	}
		
	public void startUsing()
	{
		try
		{
			Method call = plugin.getClass().getMethod("startUsing",null);
			call.invoke(plugin,null);
		}
		catch (Exception e)
		{
		}
	}
		
	public void stopUsing()
	{
		try
		{
			Method call = plugin.getClass().getMethod("stopUsing",null);
			call.invoke(plugin,null);
		}
		catch (Exception e)
		{
		}
	}
		
	public void dispose()
	{
		try
		{
			Method call = plugin.getClass().getMethod("dispose",null);
			call.invoke(plugin,null);
		}
		catch (Exception e)
		{
		}
	}
		
	public Boolean isCacheable()
	{
		try
		{
			Method call = plugin.getClass().getMethod("isCacheable",null);
			return (Boolean)call.invoke(plugin,null);
		}
		catch (Exception e)
		{
		}
		return Boolean.TRUE;
	}
		
	public Boolean setCompileResults(Boolean success, String message)
	{
		try
		{
			Method call = plugin.getClass().getMethod("setCompileResults",new Class[] {Boolean.class,String.class});
			call.invoke(plugin,new Object[] {success,message});
		}
		catch (Exception e)
		{
		}
		return Boolean.FALSE;
	}
}
