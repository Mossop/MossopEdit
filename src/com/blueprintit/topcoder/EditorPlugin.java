package com.blueprintit.topcoder;

import javax.swing.JPanel;
import java.util.List;
import com.topcoder.client.contestant.ProblemComponentModel;
import com.topcoder.shared.problem.Renderer;
import com.topcoder.shared.language.Language;

public abstract class EditorPlugin
{
	public abstract JPanel getEditorPanel();
		
	public abstract String getSource();
		
	public abstract void setSource(String source);
		
	public void configure()
	{
	}
		
	public void clear()
	{
	}
		
	public void setTextEnabled(Boolean enable)
	{
		setTextEnabled(enable.booleanValue());
	}
		
	protected void setTextEnabled(boolean enable)
	{
	}
		
	public void setProblemComponent(ProblemComponentModel component, Language language, Renderer renderer)
	{
	}
		
	public void setLanguage(Integer languageType)
	{
		setLanguage(languageType.intValue());
	}
		
	protected void setLanguage(int languageType)
	{
	}
		
	public void setProblem(String problemText)
	{
	}
		
	public void setSignature(String className, String methodName, List parmTypes, String rcType)
	{
	}
		
	public void setName(String name)
	{
	}
		
	public void install()
	{
	}
		
	public void uninstall()
	{
	}
		
	public void startUsing()
	{
	}
		
	public void stopUsing()
	{
	}
		
	public void dispose()
	{
	}
		
	public Boolean isCacheable()
	{
		return Boolean.TRUE;
	}
		
	public Boolean setCompileResults(Boolean success, String message)
	{
		return new Boolean(setCompileResults(success.booleanValue(),message));
	}
		
	protected boolean setCompileResults(boolean success, String message)
	{
		return false;
	}
}
