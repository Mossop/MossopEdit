package com.blueprintit.topcoder.mossopedit;

import com.blueprintit.topcoder.*;
import com.topcoder.client.contestApplet.common.LocalPreferences;
import com.topcoder.shared.language.*;
import com.topcoder.shared.problem.DataType;
import com.topcoder.client.contestant.ProblemComponentModel;
import com.topcoder.shared.problem.Renderer;
import java.util.Iterator;
import java.util.Map;
import java.util.HashMap;
import javax.swing.border.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.io.IOException;

public abstract class BaseEditor extends EditorPlugin
{
	protected Map variables = new HashMap();
	protected ProblemComponentModel problem;
	protected Language language;
	protected String name;
	
	private static final String JAVATEMPLATE =
		"import java.util.*;\n\npublic class $CLASSNAME$\n{\n\tpublic $RC$ $METHODNAME$($METHODPARMS$)\n\t{\n\t}\n\n\t$TESTCODE$\n}\n";

	private static final String CSHARPTEMPLATE =
		"using System;\nusing System.Collections;\n\npublic class $CLASSNAME$\n{\n\tpublic $RC$ $METHODNAME$($METHODPARMS$)\n\t{\n\t}\n\n\t$TESTCODE$\n}\n";
		
	private static final String CPPTEMPLATE =
		"#include <string>\n#include <iostream>\n#include <vector>\nusing namespace std;\n\nclass $CLASSNAME$ {\n\tpublic:\n\t$RC$ $METHODNAME$($METHODPARMS$) {\n\t}\n};\n\n$TESTCODE$\n";
		
	public void setName(String name)
	{
		this.name=name;
	}
	
	protected String getPreference(String pref, String def)
	{
		LocalPreferences prefs = LocalPreferences.getInstance();
		
		return prefs.getProperty("com.blueprintit.topcoder."+name+"."+pref,def);
	}
	
	protected Color getColorPreference(String pref)
	{
		LocalPreferences prefs = LocalPreferences.getInstance();
		
		return prefs.getColor("com.blueprintit.topcoder."+name+"."+pref);
	}
	
	protected void setPreference(String pref, String value)
	{
		LocalPreferences prefs = LocalPreferences.getInstance();
		
		prefs.setProperty("com.blueprintit.topcoder."+name+"."+pref,value);
	}
	
	protected void setColorPreference(String pref, Color value)
	{
		LocalPreferences prefs = LocalPreferences.getInstance();
		
		prefs.setColor("com.blueprintit.topcoder."+name+"."+pref,value);
	}
	
	protected void removePreference(String pref)
	{
		LocalPreferences prefs = LocalPreferences.getInstance();
		
		prefs.removeProperty("com.blueprintit.topcoder."+name+"."+pref);
	}
	
	protected void savePreferences() throws IOException
	{
		LocalPreferences prefs = LocalPreferences.getInstance();
		
		prefs.savePreferences();
	}
	
	public void uninstall()
	{
		removePreference("javatemplate");
		removePreference("csharptemplate");
		removePreference("cpptemplate");
		try
		{
			savePreferences();
		}
		catch (Exception e)
		{
		}
	}
	
	public void setProblemComponent(ProblemComponentModel component, Language language, Renderer renderer)
	{
		problem=component;
		this.language=language;
		variables.clear();
		generateVariables();
	}
	
	protected void generateVariables()
	{
		variables.put("$CLASSNAME$",problem.getClassName());
		variables.put("$METHODNAME$",problem.getMethodName());
		variables.put("$RC$",problem.getReturnType().getDescriptor(language));
		String[] names = problem.getParamNames();
		DataType[] types = problem.getParamTypes();
		StringBuffer params = new StringBuffer();
		for (int loop=0; loop<names.length; loop++)
		{
			params.append(types[loop].getDescriptor(language));
			params.append(" ");
			params.append(names[loop]);
			if ((loop+1)<names.length)
			{
				params.append(", ");
			}
		}
		variables.put("$METHODPARMS$",params.toString());
	}
	
	public void setSource(String source)
	{
		StringBuffer template = new StringBuffer(source);
		if (source.length()==0)
		{
			switch (language.getId())
			{
				case JavaLanguage.ID:
					template = new StringBuffer(getPreference("javatemplate",JAVATEMPLATE));
					break;
				case CSharpLanguage.ID:
					template = new StringBuffer(getPreference("csharptemplate",CSHARPTEMPLATE));
					break;
				case CPPLanguage.ID:
					template = new StringBuffer(getPreference("cpptemplate",CPPTEMPLATE));
					break;
			}
		}
		boolean changed=true;
		String var = null;
		while (changed)
		{
			int earliest = template.length();
			changed=false;
			Iterator it = variables.keySet().iterator();
			while (it.hasNext())
			{
				String item = it.next().toString();
				int pos = template.indexOf(item);
				if ((pos>=0)&&(pos<earliest))
				{
					earliest=pos;
					var=item;
					changed=true;
				}
			}
			if (changed)
			{
				String value = variables.get(var).toString();
				if (value.indexOf("\n")>=0)
				{
					String indent = template.substring(0,earliest);
					int lpos = indent.lastIndexOf('\n');
					if (lpos>=0)
					{
						indent=indent.substring(lpos+1);
					}
					indent = indent.replaceAll("\\S"," ");
					value = value.replaceAll("\n","\n"+indent);
				}
				template.delete(earliest,earliest+var.length());
				template.insert(earliest,value);
			}
		}
		setGeneratedSource(template.toString());
	}
	
	protected void setGeneratedSource(String source)
	{
	}
	
	protected void prepareTabbedPane(JTabbedPane pane)
	{
	}
	
	protected void saveExtraPreferences()
	{
	}
	
	private JEditorPane prepareEditorPane(String text)
	{
		final JEditorPane pane = new JEditorPane("text/plain",text);

		LocalPreferences prefs = LocalPreferences.getInstance();
		pane.setForeground(prefs.getColor(LocalPreferences.EDSTDFORE));
		pane.setBackground(prefs.getColor(LocalPreferences.EDSTDBACK));
		pane.setSelectedTextColor(prefs.getColor(LocalPreferences.EDSTDSELT));
		pane.setSelectionColor(prefs.getColor(LocalPreferences.EDSTDSELB));
		pane.setFont(new Font(prefs.getFont(LocalPreferences.EDSTDFONT),Font.PLAIN,prefs.getFontSize(LocalPreferences.EDSTDFONTSIZE)));

		return pane;
	}
	
	public void configure()
	{
		try
		{
			final JDialog configframe = new JDialog((Frame)null,true);
	
			JTabbedPane tabbedpane = new JTabbedPane();
			tabbedpane.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
			JPanel buttonpanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
			configframe.getContentPane().add(tabbedpane,BorderLayout.CENTER);
			configframe.getContentPane().add(buttonpanel,BorderLayout.SOUTH);
			
			JButton savebtn = new JButton("Save");
			buttonpanel.add(savebtn);
			JButton closebtn = new JButton("Close");
			closebtn.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e)
				{
					configframe.setVisible(false);
					configframe.dispose();
				}
			});
			buttonpanel.add(closebtn);		
	
			JPanel templatepane = new JPanel(new BorderLayout());
			tabbedpane.addTab("Code Template",templatepane);
			
			JPanel combopane = new JPanel(new FlowLayout(FlowLayout.LEFT));
			templatepane.add(combopane,BorderLayout.NORTH);
			JComboBox combobox = new JComboBox();
			combopane.add(combobox);
			final JPanel editorpane = new JPanel(new CardLayout());
			editorpane.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(5,5,5,5),BorderFactory.createEtchedBorder(EtchedBorder.RAISED)));
			templatepane.add(editorpane,BorderLayout.CENTER);
			
			final JEditorPane javatemplate = prepareEditorPane(getPreference("javatemplate",JAVATEMPLATE));
			editorpane.add(new JScrollPane(javatemplate),"Java");
			combobox.addItem("Java");
			
			final JEditorPane cstemplate = prepareEditorPane(getPreference("csharptemplate",CSHARPTEMPLATE));
			editorpane.add(new JScrollPane(cstemplate),"C#");
			combobox.addItem("C#");
			
			final JEditorPane cpptemplate = prepareEditorPane(getPreference("cpptemplate",CPPTEMPLATE));
			editorpane.add(new JScrollPane(cpptemplate),"C++");
			combobox.addItem("C++");
			
			savebtn.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e)
				{
					setPreference("javatemplate",javatemplate.getText());
					setPreference("csharptemplate",cstemplate.getText());
					setPreference("cpptemplate",cpptemplate.getText());
					saveExtraPreferences();
					try
					{
						savePreferences();
					}
					catch (Exception ex)
					{
						JOptionPane.showMessageDialog(null,"Exception while saving preferences","Error",JOptionPane.ERROR_MESSAGE);
					}
				}
			});
			
			combobox.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent e)
				{
					((CardLayout)editorpane.getLayout()).show(editorpane,e.getItem().toString());
				}
			});
			
			prepareTabbedPane(tabbedpane);
			
			configframe.setTitle(name+" configuration");
			configframe.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			configframe.setSize(600,500);
			configframe.setVisible(true);
		}
		catch (Exception e)
		{
			JOptionPane.showMessageDialog(null,e.toString(),"Exception - "+e.getMessage(),JOptionPane.ERROR_MESSAGE);
		}
	}
}
