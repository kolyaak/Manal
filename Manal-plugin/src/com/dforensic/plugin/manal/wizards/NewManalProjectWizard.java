package com.dforensic.plugin.manal.wizards;

import java.io.File;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExecutableExtension;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.WorkbenchException;

import com.dforensic.plugin.manal.model.ProjectProperties;
import com.dforensic.plugin.manal.perspective.SuspectAnalysisPerspective;

/**
 * 
 * Refer to the following article
 * http://www.ibm.com/developerworks/ru/library/os-eclipse-custwiz/
 * 
 * @author Zeoo
 * 
 */
public class NewManalProjectWizard extends Wizard implements INewWizard,
		IExecutableExtension {

	private WizardManalPropertiesPage propertiesPage;

	private IWorkbench workbench;
	private IProject project;

	@Override
	public void addPages() {
		propertiesPage = new WizardManalPropertiesPage();
		addPage(propertiesPage);

		super.addPages();
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		this.workbench = workbench;
	}

	@Override
	public void setInitializationData(IConfigurationElement config,
			String propertyName, Object data) throws CoreException {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean performFinish() {
		ProjectProperties.setApkNameVal(propertiesPage.getApkFileName());
		ProjectProperties.setPrjNameVal(propertiesPage.getProjectName());
		ProjectProperties.setAndroidPathVal(propertiesPage.getAndroidDirectoryName());
		
		
		// apk2java decompile progress bar.
		
		boolean res = importProject();  //progress bar dialog.
		
		if (res) {
			openPerspective(SuspectAnalysisPerspective.SUSPECT_ANAL_PERSP_ID);
		}
		
		return res;
	}

	private boolean importProject() {
		String baseDir = propertiesPage.getDecompiledSourceDirectoryName();

		if (baseDir != null) {
			File prjDir = new File(baseDir);

			try {
				IProjectDescription description = ResourcesPlugin
						.getWorkspace()
						.loadProjectDescription(
								new Path(prjDir.getAbsolutePath() + "/.project"));
				project = ResourcesPlugin.getWorkspace().getRoot()
						.getProject(description.getName());
				project.create(description, null);
				project.open(null);
				
				project.setPersistentProperty(new QualifiedName(ProjectProperties.QUALIFIER,
						ProjectProperties.getApkNameKey()),
						ProjectProperties.getApkNameVal());
				project.setPersistentProperty(new QualifiedName(ProjectProperties.QUALIFIER,
						ProjectProperties.getPrjNameKey()),
						ProjectProperties.getPrjNameVal());
				project.setPersistentProperty(new QualifiedName(ProjectProperties.QUALIFIER,
						ProjectProperties.getAndroidPathKey()),
						ProjectProperties.getAndroidPathVal());

				return true;

			} catch (CoreException e) {
				System.err.println("Not valid project to be opened.");
				return false;
			}
		} else {
			System.err
					.println("Directory to import a project is not specified.");
			return false;
		}
	}

	private void openPerspective(String perspectiveID) {
		IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();
		try {
			workbench.showPerspective(perspectiveID, window);
		} catch (WorkbenchException e) {
			System.err.println("Unable to open Perspective [" + perspectiveID + "].");
			MessageDialog.openError(window.getShell(),
					"Error Opening Perspective",
					"Could not open Perspective with ID: " + perspectiveID);
		}
	}

}
