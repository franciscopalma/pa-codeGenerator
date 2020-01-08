package pt.iscte.pidesco.codegenerator;

import java.io.File;
import java.util.Random;

import org.eclipse.jface.text.ITextSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Label;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import pt.iscte.pidesco.extensibility.PidescoServices;
import pt.iscte.pidesco.extensibility.PidescoTool;
import pt.iscte.pidesco.javaeditor.service.JavaEditorServices;
import pt.iscte.pidesco.projectbrowser.service.ProjectBrowserServices;

public class PackageTool implements PidescoTool {


	@Override
	public void run(boolean activate) {
		BundleContext context = Activator.getContext();
		//obter a interface para estes serviços (service Reference)
		ServiceReference<JavaEditorServices> serviceReferenceJavaEditor = context.getServiceReference(JavaEditorServices.class);
		JavaEditorServices javaServ = context.getService(serviceReferenceJavaEditor);

		ServiceReference<PidescoServices> serviceReference = context.getServiceReference(PidescoServices.class);
		PidescoServices projServ = context.getService(serviceReference);

		File root = projServ.getWorkspaceRoot();


		Random r = new Random();
		int id =(int) r.nextInt(10000);
		new File(root.getAbsolutePath()+"/"+"folder"+id).mkdir();
		
		projServ.runTool(ProjectBrowserServices.REFRESH_TOOL_ID, true);
		

	}



}
