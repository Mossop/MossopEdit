/*
 * $Author$
 * $RCSfile$
 * $Date$
 * $Revision$
 */
package com.blueprintit.topcoder;

import javax.swing.JPanel;

import com.topcoder.client.contestant.ProblemComponentModel;
import com.topcoder.shared.language.Language;
import com.topcoder.shared.problem.Renderer;

/**
 * @author Dave
 */
public interface EditorPlugin
{
	public JPanel getEditorPanel();
	
	public String getSource();
		
	public void setSource(String source);
		
	public void configure();
		
	public void clear();
		
	public void setTextEnabled(Boolean enable);
		
	public void setProblemComponent(ProblemComponentModel component, Language language, Renderer renderer);
		
	public void setName(String name);
		
	public void install();
		
	public void uninstall();
		
	public void startUsing();
		
	public void stopUsing();
		
	public void dispose();
		
	public Boolean isCacheable();
		
	public Boolean setCompileResults(Boolean success, String message);
}
