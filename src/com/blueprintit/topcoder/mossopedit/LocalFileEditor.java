package com.blueprintit.topcoder.mossopedit;

import com.topcoder.client.contestApplet.common.LocalPreferences;
import com.topcoder.shared.language.*;
import com.topcoder.shared.problem.TestCase;
import com.topcoder.shared.problem.DataType;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.StringWriter;
import java.io.PrintWriter;

public class LocalFileEditor extends BaseEditor
{
	JTextArea status;
	JPanel panel;
	JCheckBox useclassname;
	JTextField directory;
	JTextField filename;
	
	private static final String VERSION = "0.9";
	private static final String START_TEST_COMMENT = "// Testing code begins";
	private static final String END_TEST_COMMENT = "// Testing code ends";
	private static final String TEST_VARIABLE = "$TESTCODE$";
	private static final String TEST_COMMENT = "// Tested with MossopEdit v"+VERSION;
	
	public LocalFileEditor()
	{
		status = new JTextArea("LocalFileEditor started.\n");
		status.setEditable(false);

		panel = new JPanel(new BorderLayout());
		panel.add(new JScrollPane(status),BorderLayout.CENTER);
		panel.validate();
	}
	
	public JPanel getEditorPanel()
	{
		LocalPreferences prefs = LocalPreferences.getInstance();
		status.setForeground(prefs.getColor(LocalPreferences.PROBLEMFORE));
		status.setBackground(prefs.getColor(LocalPreferences.PROBLEMBACK));
		status.setFont(new Font(prefs.getFont(LocalPreferences.PROBLEMFONT),Font.PLAIN,prefs.getFontSize(LocalPreferences.PROBLEMFONTSIZE)));

		return panel;
	}

	private String constructInitialiser(String name, DataType type, String value)
	{
		if (type.getDimension()>0)
		{
			if (language.getId()!=CPPLanguage.ID)
			{
				return "\t"+name+" = new "+type.getDescriptor(language)+" "+value;
			}
			else
			{
				return "";
			}
		}
		else
		{
			return "\t"+name+" = "+value;
		}
	}
	
	private void writeValueTest(PrintWriter test, String indent, String result, String expected, DataType type, String element)
	{
		if (type.getBaseName().equals("String"))
		{
			switch (language.getId())
			{
				case JavaLanguage.ID:
					test.println(indent+"if (!"+result+".equals("+expected+"))");
					test.println(indent+"{");
					test.println(indent+"\tSystem.out.println(\"Result\""+element+"\" should have been \"+"+expected+"+\" but was \"+"+result+");");
					test.println(indent+"\treturn false;");
					test.println(indent+"}");
					break;
				case CSharpLanguage.ID:
					test.println(indent+"if (!"+result+".Equals("+expected+"))");
					test.println(indent+"{");
					test.println(indent+"\tConsole.WriteLine(\"Result\""+element+"\" should have been \"+"+expected+"+\" but was \"+"+result+");");
					test.println(indent+"\treturn false;");
					test.println(indent+"}");
					break;
				case CPPLanguage.ID:
					test.println(indent+"if ("+result+"!="+expected+")");
					test.println(indent+"{");
					test.println(indent+"\tcout << \"Result\""+element.replaceAll("\\+","<<")+"\" should have been \" << "+expected+" << \" but was \" << "+result+" << \"\\n\";");
					test.println(indent+"\treturn false;");
					test.println(indent+"}");
					break;
			}
		}
		else
		{
			switch (language.getId())
			{
				case JavaLanguage.ID:
					test.println(indent+"if ("+result+"!="+expected+")");
					test.println(indent+"{");
					test.println(indent+"\tSystem.out.println(\"Result\""+element+"\" should have been \"+"+expected+"+\" but was \"+"+result+");");
					test.println(indent+"\treturn false;");
					test.println(indent+"}");
					break;
				case CSharpLanguage.ID:
					test.println(indent+"if ("+result+"!="+expected+")");
					test.println(indent+"{");
					test.println(indent+"\tConsole.WriteLine(\"Result\""+element+"\" should have been \"+"+expected+"+\" but was \"+"+result+");");
					test.println(indent+"\treturn false;");
					test.println(indent+"}");
					break;
				case CPPLanguage.ID:
					test.println(indent+"if ("+result+"!="+expected+")");
					test.println(indent+"{");
					test.println(indent+"\tcout << \"Result\""+element.replaceAll("\\+","<<")+"\" should have been \" << "+expected+" << \" but was \" << "+result+" << \"\\n\";");
					test.println(indent+"\treturn false;");
					test.println(indent+"}");
					break;
			}
		}
	}
	
	private void writeArrayCheck(PrintWriter test, DataType type, char letter, String indent, String resultvar, String expectedvar, String element)
	{
		if (type.getDimension()>0)
		{
			switch (language.getId())
			{
				case JavaLanguage.ID:
					test.println(indent+"if ("+resultvar+".length!="+expectedvar+".length)");
					test.println(indent+"{");
					test.println(indent+"\tSystem.out.println(\"Result\""+element+"\" should have had \"+"+expectedvar+".length+\" elements but had \"+"+resultvar+".length);");
					test.println(indent+"\treturn false;");
					test.println(indent+"}");
					break;
				case CSharpLanguage.ID:
					test.println(indent+"if ("+resultvar+".Length!="+expectedvar+".Length)");
					test.println(indent+"{");
					test.println(indent+"\tConsole.WriteLine(\"Result\""+element+"\" should have had \"+"+expectedvar+".Length+\" elements but had \"+"+resultvar+".Length);");
					test.println(indent+"\treturn false;");
					test.println(indent+"}");
					break;
				case CPPLanguage.ID:
					test.println(indent+"if ("+resultvar+".size()!="+expectedvar+".size())");
					test.println(indent+"{");
					test.println(indent+"\tcout << \"Result\""+element.replaceAll("\\+","<<")+"\" should have had \" << (int)"+expectedvar+".size() << \" elements but had \" << (int)"+resultvar+".size() << \"\\n\";");
					test.println(indent+"\treturn 0;");
					test.println(indent+"}");
					break;
			}
			test.print(indent+"for (int _"+letter+"=0; _"+letter+"<");
			switch (language.getId())
			{
				case JavaLanguage.ID:
					test.print(resultvar+".length");
					break;
				case CSharpLanguage.ID:
					test.print(resultvar+".Length");
					break;
				case CPPLanguage.ID:
					test.print(""+resultvar+".size()");
					break;
			}
			test.println("; ++_"+letter+")");
			test.println(indent+"{");
			resultvar=resultvar+"[_"+letter+"]";
			expectedvar=expectedvar+"[_"+letter+"]";
			element=element+"\"[\"+_"+letter+"+\"]\"+";
			letter++;
			try
			{
				writeArrayCheck(test,type.reduceDimension(),letter,indent+"\t",resultvar,expectedvar,element);
			}
			catch (Exception e)
			{
			}
			test.println(indent+"}");
		}
		else
		{
			writeValueTest(test,indent,resultvar,expectedvar,type,element);
		}
	}
	
	protected void generateVariables()
	{
		try
		{
			super.generateVariables();
			StringWriter testcode = new StringWriter();
			PrintWriter test = new PrintWriter(testcode);
			test.println(START_TEST_COMMENT);
			
			switch (language.getId())
			{
				case JavaLanguage.ID:
					test.println("public static boolean _run_test_case($RC$ _expected, $METHODPARMS$)");
					break;
				case CSharpLanguage.ID:
					test.println("public static bool _run_test_case($RC$ _expected, $METHODPARMS$)");
					break;
				case CPPLanguage.ID:
					test.println("int _run_test_case($RC$ _expected, $METHODPARMS$)");
					break;
			}
			test.println("{");
			switch (language.getId())
			{
				case CPPLanguage.ID:
					test.println("\t$CLASSNAME$ _class;");
					break;
				default:
					test.println("\t$CLASSNAME$ _class = new $CLASSNAME$();");
					break;
			}
			test.print("\t$RC$ _result = _class.$METHODNAME$(");
			String namelist = "";
			String[] names = problem.getParamNames();
			for (int loop=0; loop<names.length; loop++)
			{
				namelist+=names[loop];
				if ((loop+1)<names.length)
				{
					namelist+=",";
				}
			}
			test.print(namelist);
			test.println(");");
	
			writeArrayCheck(test,problem.getReturnType(),'a',"\t","_result","_expected","+");
	
			switch (language.getId())
			{
				case JavaLanguage.ID:
					test.println("\treturn true;");
					break;
				case CSharpLanguage.ID:
					test.println("\treturn true;");
					break;
				case CPPLanguage.ID:
					test.println("\treturn 1;");
					break;
			}
			test.println("}\n");
			switch (language.getId())
			{
				case JavaLanguage.ID:
					test.println("public static void main(String[] args)");
					break;
				case CSharpLanguage.ID:
					test.println("public static void Main(string[] args)");
					break;
				case CPPLanguage.ID:
					test.println("void main()");
					break;
			}
			test.println("{");
			DataType[] types = problem.getParamTypes();
			for (int loop=0; loop<names.length; loop++)
			{
				test.println("\t"+types[loop].getDescriptor(language)+" "+names[loop]+";");
			}
			test.println("\t"+problem.getReturnType().getDescriptor(language)+" _expected;");
			test.println();
			TestCase[] cases = problem.getTestCases();
			for (int loop=0; loop<cases.length; loop++)
			{
				for (int var=0; var<types.length; var++)
				{
					test.println(constructInitialiser(names[var],types[var],cases[loop].getInput()[var])+";");
				}
				test.println(constructInitialiser("_expected",problem.getReturnType(),cases[loop].getOutput())+";");
				switch (language.getId())
				{
					case JavaLanguage.ID:
						test.println("\tSystem.out.print(\"Test "+loop+": \");");
						break;
					case CSharpLanguage.ID:
						test.println("\tConsole.Write(\"Test "+loop+": \");");
						break;
					case CPPLanguage.ID:
						test.println("\tcout << \"Test "+loop+": \";");
						break;
				}
				test.println("\tif (_run_test_case(_expected,"+namelist+"))");
				test.println("\t{");
				switch (language.getId())
				{
					case JavaLanguage.ID:
						test.println("\t\tSystem.out.println(\"Passed\");");
						break;
					case CSharpLanguage.ID:
						test.println("\t\tConsole.WriteLine(\"Passed\");");
						break;
					case CPPLanguage.ID:
						test.println("\t\tcout << \"Passed\\n\";");
						break;
				}
				test.println("\t}");
				test.println();
			}
			test.println("}");
			test.print(END_TEST_COMMENT);
			variables.put(TEST_VARIABLE,testcode.toString());
			variables.put(TEST_COMMENT,testcode.toString());
		}
		catch (Exception e)
		{
			status.append("Exception generating test code - "+e.toString()+"\n");
		}
	}
	
	public void clear()
	{
		setSource("");
	}
	
	public String getSource()
	{
		String dir = getPreference("directory","");
		if (!dir.endsWith(File.separator))
		{
			dir=dir+File.separator;
		}
		String file = getPreference("filename","");
		if (getPreference("useclassname","false").equals("true"))
		{
			file=problem.getClassName();
		}
		switch (language.getId())
		{
			case CSharpLanguage.ID:
				file+=".cs";
				break;
			case JavaLanguage.ID:
				file+=".java";
				break;
			case CPPLanguage.ID:
				file+=".cpp";
				break;
		}
		status.append("Reading source from file "+dir+file+"\n");
		try
		{
			BufferedReader in = new BufferedReader(new FileReader(dir+file));
			StringBuffer source = new StringBuffer();
			String line = in.readLine();
			while (line!=null)
			{
				source.append(line);
				source.append('\n');
				line=in.readLine();
			}
			in.close();
			int lpos = source.indexOf(START_TEST_COMMENT);
			int rpos = source.indexOf(END_TEST_COMMENT);
			if ((lpos>=0)&&(rpos>=0))
			{
				source.delete(lpos,rpos+END_TEST_COMMENT.length());
				source.insert(lpos,TEST_COMMENT);
			}
			return source.toString();
		}
		catch (IOException e)
		{
			status.append("Exception reading from file - "+e.getMessage()+"\n");
		}
		return "";
	}

	protected void setGeneratedSource(String source)
	{
		String dir = getPreference("directory","");
		if (!dir.endsWith(File.separator))
		{
			dir=dir+File.separator;
		}
		String file = getPreference("filename","");
		if (getPreference("useclassname","false").equals("true"))
		{
			file=problem.getClassName();
		}
		switch (language.getId())
		{
			case CSharpLanguage.ID:
				file+=".cs";
				break;
			case JavaLanguage.ID:
				file+=".java";
				break;
			case CPPLanguage.ID:
				file+=".cpp";
				break;
		}
		status.append("Writing source to file "+dir+file+"\n");
		try
		{
			FileWriter out = new FileWriter(dir+file);
			out.write(source);
			out.flush();
			out.close();
		}
		catch (IOException e)
		{
			status.append("Exception writing to file - "+e.getMessage()+"\n");
		}
	}	

	public void uninstall()
	{
		removePreference("useclassname");
		removePreference("directory");
		removePreference("filename");
		super.uninstall();
	}
	
	protected void prepareTabbedPane(JTabbedPane pane)
	{
		LocalPreferences prefs = LocalPreferences.getInstance();
		JPanel options = new JPanel(new GridBagLayout());
		Color fore = prefs.getColor(LocalPreferences.MESSAGEFORE);
		Color back = prefs.getColor(LocalPreferences.MESSAGEBACK);
		options.setForeground(fore);
		options.setBackground(back);
		Font font = new Font(prefs.getFont(LocalPreferences.MESSAGEFONT),Font.PLAIN,prefs.getFontSize(LocalPreferences.MESSAGEFONTSIZE));
		
		options.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
		options.setName("General");
		pane.add(options,0);
		
		Insets inset = new Insets(2,2,2,2);
		
		GridBagConstraints cons = new GridBagConstraints();
		cons.gridx=0;
		cons.gridy=0;
		cons.insets=inset;
		cons.anchor=GridBagConstraints.EAST;
		
		JLabel label = new JLabel("Directory");
		label.setForeground(fore);
		label.setBackground(back);
		label.setFont(font);
		options.add(label,cons);

		cons = new GridBagConstraints();
		cons.gridx=1;
		cons.gridy=0;
		cons.insets=inset;
		cons.weightx=1;
		cons.fill=GridBagConstraints.HORIZONTAL;
		cons.anchor=GridBagConstraints.WEST;
		
		directory = new JTextField(getPreference("directory",""));
		
		options.add(directory,cons);

		cons = new GridBagConstraints();
		cons.gridx=2;
		cons.gridy=0;
		cons.insets=inset;
		cons.anchor=GridBagConstraints.WEST;
		
		JButton browsebtn = new JButton("Browse...");
		
		options.add(browsebtn,cons);

		cons = new GridBagConstraints();
		cons.gridx=0;
		cons.gridy=1;
		cons.insets=inset;
		cons.weightx=1;
		cons.gridwidth=3;
		cons.anchor=GridBagConstraints.WEST;
		
		useclassname = new JCheckBox("Use class name as filename");

		useclassname.setForeground(fore);
		useclassname.setBackground(back);
		useclassname.setFont(font);
		
		options.add(useclassname,cons);

		cons = new GridBagConstraints();
		cons.gridx=0;
		cons.gridy=2;
		cons.insets=inset;
		cons.weighty=1;
		cons.anchor=GridBagConstraints.NORTHEAST;
		
		label = new JLabel("Filename:");
		label.setForeground(fore);
		label.setBackground(back);
		label.setFont(font);
		options.add(label,cons);

		cons = new GridBagConstraints();
		cons.gridx=1;
		cons.gridy=2;
		cons.insets=inset;
		cons.weightx=1;
		cons.weighty=1;
		cons.fill=GridBagConstraints.HORIZONTAL;
		cons.anchor=GridBagConstraints.NORTHWEST;
		
		filename = new JTextField(getPreference("filename",""));
		
		options.add(filename,cons);
		
		if (getPreference("useclassname","false").equals("true"))
		{
			useclassname.setSelected(true);
			filename.setEnabled(false);
		}
		
		useclassname.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e)
			{
				filename.setEnabled(!useclassname.isSelected());
			}
		});
		
		browsebtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				JFileChooser dirbrowse = new JFileChooser();
				dirbrowse.setDialogTitle("Select the directory");
				dirbrowse.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int result = dirbrowse.showOpenDialog(null);
				if (result==JFileChooser.APPROVE_OPTION)
				{
					directory.setText(dirbrowse.getSelectedFile().getAbsolutePath());
				}
			}
		});
	}
	
	protected void saveExtraPreferences()
	{
		if (useclassname.isSelected())
		{
			setPreference("useclassname","true");
		}
		else
		{
			setPreference("useclassname","false");
		}
		setPreference("directory",directory.getText());
		setPreference("filename",filename.getText());
	}
}
