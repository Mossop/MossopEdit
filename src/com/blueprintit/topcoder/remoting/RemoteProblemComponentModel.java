/*
 * $Author$
 * $RCSfile$
 * $Date$
 * $Revision$
 */
package com.blueprintit.topcoder.remoting;

import java.io.IOException;
import java.io.ObjectStreamException;

import com.topcoder.client.contestant.ProblemComponentModel;
import com.topcoder.client.contestant.ProblemModel;
import com.topcoder.netCommon.contestantMessages.response.data.ComponentChallengeData;
import com.topcoder.shared.netCommon.CSReader;
import com.topcoder.shared.netCommon.CSWriter;
import com.topcoder.shared.netCommon.CustomSerializable;
import com.topcoder.shared.problem.Constraint;
import com.topcoder.shared.problem.DataType;
import com.topcoder.shared.problem.Element;
import com.topcoder.shared.problem.ProblemComponent;
import com.topcoder.shared.problem.TestCase;

/**
 * @author Dave
 */
public class RemoteProblemComponentModel implements ProblemComponentModel, CustomSerializable
{
	private ProblemComponent component;
	private ComponentChallengeData challengeData;
	private Long id;
	private Integer componentID;
	private Double points;
	
	public RemoteProblemComponentModel()
	{
	}
	
	public Long getID()
	{
		return id;
	}

	public Integer getComponentTypeID()
	{
		return componentID;
	}

	public ProblemModel getProblem()
	{
		// TODO sort this out
		return null;
	}

	public Double getPoints()
	{
		return points;
	}

	public ProblemComponent getComponent()
	{
		return component;
	}

	public boolean hasSignature()
	{
		// TODO Auto-generated method stub
		return false;
	}

	public String getClassName()
	{
		return component.getClassName();
	}

	public String getMethodName()
	{
		return component.getMethodName();
	}

	public DataType getReturnType()
	{
		return component.getReturnType();
	}

	public DataType[] getParamTypes()
	{
		return component.getParamTypes();
	}

	public String[] getParamNames()
	{
		return component.getParamNames();
	}

	public ComponentChallengeData getComponentChallengeData()
	{
		return challengeData;
	}

	public boolean hasStatement()
	{
		// TODO figure out what this should return
		return true;
	}

	public boolean hasIntro()
	{
		return (component.getIntro()!=null);
	}

	public Element getIntro()
	{
		return component.getIntro();
	}

	public boolean hasSpec()
	{
		return (component.getSpec()!=null);
	}

	public Element getSpec()
	{
		return component.getSpec();
	}

	public boolean hasNotes()
	{
		return (component.getNotes()!=null)&&(component.getNotes().length>0);
	}

	public Element[] getNotes()
	{
		return component.getNotes();
	}

	public boolean hasConstraints()
	{
		return (component.getConstraints()!=null)&&(component.getConstraints().length>0);
	}

	public Constraint[] getConstraints()
	{
		return component.getConstraints();
	}

	public boolean hasTestCases()
	{
		return (component.getTestCases()!=null)&&(component.getTestCases().length>0);
	}

	public TestCase[] getTestCases()
	{
		return component.getTestCases();
	}

	public boolean hasDefaultSolution()
	{
		return (component.getDefaultSolution()!=null)&&(component.getDefaultSolution().length()>0);
	}

	public String getDefaultSolution()
	{
		return component.getDefaultSolution();
	}

	public void customWriteObject(CSWriter arg0) throws IOException
	{
	}

	public void customReadObject(CSReader reader) throws IOException, ObjectStreamException
	{
		id = new Long(reader.readLong());
		componentID = new Integer(reader.readInt());
		points = new Double(reader.readDouble());
		challengeData = (ComponentChallengeData)reader.readObject();
		component = (ProblemComponent)reader.readObject();
	}
}
