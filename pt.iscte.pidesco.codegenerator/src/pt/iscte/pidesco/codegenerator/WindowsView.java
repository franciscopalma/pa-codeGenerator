package pt.iscte.pidesco.codegenerator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import pt.iscte.pidesco.extensibility.PidescoServices;
import pt.iscte.pidesco.extensibility.PidescoView;
import pt.iscte.pidesco.javaeditor.service.JavaEditorServices;
import pt.iscte.pidesco.projectbrowser.service.ProjectBrowserServices;

public class WindowsView implements PidescoView {

	@Override
	public void createContents(Composite viewArea, Map<String, Image> imageMap) {
		BundleContext context = Activator.getContext();
		//obter a interface para estes serviços (service Reference)
		ServiceReference<PidescoServices> serviceReference = context.getServiceReference(PidescoServices.class);
		PidescoServices projServ = context.getService(serviceReference);
		
		ServiceReference<JavaEditorServices> serviceReferenceJavaEditor = context.getServiceReference(JavaEditorServices.class);
		JavaEditorServices projServJavaEditor = context.getService(serviceReferenceJavaEditor);
		
		GridLayout gridlayout = new GridLayout();
		gridlayout.numColumns = 2;
		viewArea.setLayout(gridlayout);
		
		Label classlabel = new Label(viewArea, SWT.NULL);
		classlabel.setText("Name:");
		
		Text className = new Text(viewArea, SWT.BORDER);
		className.setText("-------- Class Name ------");
//		GridData gridData = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
//        gridData.horizontalSpan = gridlayout.numColumns - 1;
//        className.setLayoutData(gridData);
        
        Label modifiersLabel = new Label(viewArea, SWT.NULL);
        modifiersLabel.setText("Modifiers:");
        
        Combo combo = new Combo(viewArea, SWT.READ_ONLY);
        combo.setItems(" abstract"," final", "");
//        combo.setLayoutData(gridData);
        
        Label constructorLabel = new Label(viewArea, SWT.NULL);
        constructorLabel.setText("Constructor");
        
        Button constructorButton = new Button(viewArea, SWT.CHECK);
        
        Label mainLabel = new Label(viewArea, SWT.NULL);
        mainLabel.setText("Main");
        
        Button mainButton = new Button(viewArea, SWT.CHECK);
        
        Button finishButton = new Button(viewArea, SWT.NULL);
        finishButton.setText("Finish");
        finishButton.addListener(SWT.Selection, new Listener() {
			
			@Override
			public void handleEvent(Event event) {
				switch(event.type) {
				case SWT.Selection:
					//get root file
					File root = projServ.getWorkspaceRoot();
					
					File newFile = new File(root.toString(), className.getText().substring(0,1).toUpperCase()+className.getText().substring(1)+".java");
					
					//Create class structure
				    String createClass = "public"+combo.getText()+" class " + className.getText().substring(0,1).toUpperCase()+className.getText().substring(1)+"{\n\n\n\n}";

					try {
						
						//create new File in root
						newFile.createNewFile();
						
						//write in file.java
					    projServJavaEditor.setText(newFile, createClass);
					    projServJavaEditor.saveFile(newFile);

					} catch (IOException e) {
						System.out.println("Erro ao criar ficheiro");
						e.printStackTrace();
					}
					System.out.println(newFile.toString());
					
				}
				
			}
		});
        
        
        
        
        

	}

}
