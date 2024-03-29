package com.blueprintit.topcoder;

import javax.swing.JPanel;
import com.topcoder.client.contestant.ProblemComponentModel;
import com.topcoder.shared.problem.Renderer;
import com.topcoder.shared.language.Language;

public abstract class AbstractEditorPlugin implements EditorPlugin
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
	}
		
	public void setProblemComponent(ProblemComponentModel component, Language language, Renderer renderer)
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
		return Boolean.FALSE;
	}
}
