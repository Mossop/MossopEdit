/*
 * $Author$
 * $RCSfile$
 * $Date$
 * $Revision$
 */
package com.blueprintit.topcoder.remoting;

import com.topcoder.client.contestant.ProblemComponentModel;
import com.topcoder.shared.language.Language;
import com.topcoder.shared.problem.Renderer;

/**
 * @author Dave
 */
public interface RemotelyCalledPlugin
{
	public String getSource();
		
	public void setSource(String source);
		
	public void clear();
		
	public void setProblemComponent(ProblemComponentModel component, Language language, Renderer renderer);
		
	public void startUsing();
		
	public void stopUsing();
		
	public void dispose();
}
