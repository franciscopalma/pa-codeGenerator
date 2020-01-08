package pt.iscte.pidesco.codegenerator;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import pt.iscte.pidesco.codegenerator.extensibility.MethodAction;
import pt.iscte.pidesco.extensibility.PidescoServices;
import pt.iscte.pidesco.extensibility.PidescoTool;
import pt.iscte.pidesco.javaeditor.service.JavaEditorServices;


public class ClassTool implements PidescoTool {

	private Shell shell;
	private Button constructorButton, mainButton;
	private Set<Button> buttonsName = new LinkedHashSet<>();
	private String createConstructor = "";
	private String createMain = "";
	private String output = "";
	private String extOutput = "";

	@Override
	public void run(boolean selected) {
		BundleContext context = Activator.getContext();
		//obter a interface para estes serviços (service Reference)
		ServiceReference<PidescoServices> serviceReference = context.getServiceReference(PidescoServices.class);
		PidescoServices projServ = context.getService(serviceReference);

		ServiceReference<JavaEditorServices> serviceReferenceJavaEditor = context.getServiceReference(JavaEditorServices.class);
		JavaEditorServices projServJavaEditor = context.getService(serviceReferenceJavaEditor);

		shell = new Shell ();
		shell.setText ("New Java Class");
		shell.setSize (500, 400);

		GridLayout gridlayout = new GridLayout();
		gridlayout.numColumns = 2;
		shell.setLayout(gridlayout);

		Label classlabel = new Label(shell, SWT.NULL);
		classlabel.setText("Name:");

		Text className = new Text(shell, SWT.BORDER);
		className.setTextLimit(20);
	
		Label modifiersLabel = new Label(shell, SWT.NULL);
		modifiersLabel.setText("Modifiers:");

		Combo combo = new Combo(shell, SWT.READ_ONLY);
		combo.setItems(" abstract"," final", "");
		
		buttonsName.clear();
		//cleanValues();
		
		prepareContent();
		

		Button finishButton = new Button(shell, SWT.NULL);
		finishButton.setText("Finish");
		finishButton.addListener(SWT.Selection, new Listener() {

			@Override
			public void handleEvent(Event event) {
				switch(event.type) {
				case SWT.Selection:
					//get root file
					
					File root = projServ.getWorkspaceRoot();

					String classNameUpper = className.getText().substring(0,1).toUpperCase()+className.getText().substring(1);
					File newFile = new File(root.toString(), classNameUpper+".java");
					
					//Create class structure
					String createClass = "public"+combo.getText()+" class " + classNameUpper +"{\n\n";



					IExtensionRegistry reg = Platform.getExtensionRegistry();
					//da me todos os elementos que arem a esta extençao

					IConfigurationElement[] elements = reg.getConfigurationElementsFor("pt.iscte.pidesco.codegenerator.codeactions2");
					for(IConfigurationElement e : elements) {

						try {
							MethodAction action = (MethodAction) e.createExecutableExtension("class");
							HashMap<String, String> map = action.run();

							for (Map.Entry<String, String> entry: map.entrySet()) {
								for(Button button: buttonsName) {
									if(button.getText().equals(entry.getKey()) && button.getSelection())
										extOutput =  extOutput + "\n\n" + entry.getValue();


								}
							}
							map.clear();

						} catch (CoreException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}

					
					if(constructorButton.getSelection()) {
						createConstructor = buildConstructor(classNameUpper);
						System.out.println("is enable");
					}else {
						System.out.println("is not");
					}
					if(mainButton.getSelection())
						createMain = buildMain();
					output = createClass + "\n" + createConstructor + "\n" + createMain + "\n" + extOutput +"\n\n}";

					try {

						//create new File in root
						newFile.createNewFile();

						//write in file.java
						projServJavaEditor.setText(newFile, output);
						projServJavaEditor.saveFile(newFile);

					} catch (IOException e) {
						System.out.println("Erro ao criar ficheiro");
						e.printStackTrace();
					}
					System.out.println(newFile.toString());
					
				}
				System.out.println("clean\n"+ buttonsName.size());
				buttonsName.clear();
				System.out.println("clean\n"+ buttonsName.size());
				cleanValues();
				
				shell.close();
				
				
			}
			
		});

	
		shell.open();
		



	}

	public void prepareContent() {

		//		Label constructorLabel = new Label(shell, SWT.NULL);
		//		constructorLabel.setText("Constructor");

		constructorButton = new Button(shell, SWT.CHECK);
		constructorButton.setText("Constructor");

		//		Label mainLabel = new Label(shell, SWT.NULL);
		//		mainLabel.setText("Main");

		mainButton = new Button(shell, SWT.CHECK);
		mainButton.setText("Main");

		buttonsName.add(constructorButton);
		buttonsName.add(mainButton);

		IExtensionRegistry reg = Platform.getExtensionRegistry();
		//da me todos os elementos que arem a esta extençao
		IConfigurationElement[] elements = reg.getConfigurationElementsFor("pt.iscte.pidesco.codegenerator.codeactions2");
		for(IConfigurationElement e : elements) {

			try {
				MethodAction action = (MethodAction) e.createExecutableExtension("class");
				HashMap<String, String> map = action.run();

				for (String key : map.keySet()) {

					Button a = new Button(shell, SWT.CHECK);
					a.setText(key);
					buttonsName.add(a);

				}

				System.out.println("prep\n"+buttonsName.size());



			} catch (CoreException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}


		}



	}

	public String buildConstructor(String name) {
		String constructor = "\t"+"public " + name +"()" + "{\n\n\t}";
		return constructor;
	}

	public void getExtensionValues() {

	}

	public String buildMain() {
		String main = "\t" + "public static void main(String[] args){\n\n\t}";
		return main;
	}

	public void cleanValues() {
		createConstructor = "";
		createMain = "";
		output = "";
		extOutput = "";
	}


}