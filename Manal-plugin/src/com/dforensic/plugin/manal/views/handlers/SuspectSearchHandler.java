/*
 *  <Manal project is an eclipse plugin for the automation of malware analysis.>
 *  Copyright (C) <2014>  <Nikolay Akatyev, Hojun Son>
 *  This file is part of Manal project.
 *
 *  Manal project is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, version 3 of the License.
 *
 *  Manal project is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with Manal project. If not, see <http://www.gnu.org/licenses/>.
 *  
 *  Contact information of contributors:
 *  - Nikolay Akatyev: nikolay.akatyev@gmail.com
 *  - Hojun Son: smuoon4680@gmail.com
 */
package com.dforensic.plugin.manal.views.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;

import com.dforensic.plugin.manal.ManalManager;
import com.dforensic.plugin.manal.model.ProjectProperties;

/**
 * Our sample handler extends AbstractHandler, an IHandler base class.
 * @see org.eclipse.core.commands.IHandler
 * @see org.eclipse.core.commands.AbstractHandler
 */
public class SuspectSearchHandler extends AbstractHandler {
	/**
	 * The constructor.
	 */
	private Shell shell;
	public SuspectSearchHandler() {
	}

	/**
	 * the command has been executed, so extract extract the needed information
	 * from the application context.
	 */
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
	    this.shell = window.getShell();
		if (window != null)
	    {
			try {
		        IStructuredSelection selection = (IStructuredSelection) window.getSelectionService().getSelection();
		        Object firstElement = selection.getFirstElement();
		        if (firstElement instanceof IAdaptable)
		        {
		            IProject project = (IProject)((IAdaptable)firstElement).getAdapter(IProject.class);
		            try {
						ProjectProperties.setApkNameVal(
								project.getPersistentProperty(new QualifiedName(ProjectProperties.QUALIFIER,
										ProjectProperties.getApkNameKey())));
//						ProjectProperties.setPrjNameVal(
//								project.getPersistentProperty(new QualifiedName(ProjectProperties.QUALIFIER,
//										ProjectProperties.getPrjNameKey())));
						ProjectProperties.setAndroidPathVal(
								project.getPersistentProperty(new QualifiedName(ProjectProperties.QUALIFIER,
										ProjectProperties.getAndroidPathKey())));
					} catch (CoreException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		        }
			} catch (Exception e) {
				MessageDialog.openInformation(window.getShell(), "No selected project", 
						"Please, select a project to analyse.");
			}
	    }
		
		ManalManager manager = new ManalManager();
		/*
		Display  display  =  PlatformUI.getWorkbench().getDisplay();
        Shell  shell  =  new  Shell(display);
        shell.setLayout(new  GridLayout());
        shell.setSize(150,60);
        ProgressBar  pb  =  new  ProgressBar(shell,  SWT.HORIZONTAL  |  SWT.SMOOTH);
        pb.setLayoutData(new  GridData(GridData.FILL_HORIZONTAL));
        pb.setMinimum(0);
        pb.setMaximum(30);
        
        new myProgressBar(display,pb).start();
        shell.open();
        while  (!shell.isDisposed())  {
            if  (!display.readAndDispatch())  {
                    //display.sleep();
            	manager.searchSuspiciousApi();
            }
        }*/
      
		manager.searchSuspiciousApi();
		
		/*
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
	    IWorkbenchPage page = window.getActivePage();
	    SuspectListVw view = (SuspectListVw) page.findView(SuspectListVw.ID);
		view.openJavaSourceEditor();
	    MessageDialog.openInformation(
				window.getShell(),
				"Manal",
				"Hello, Eclipse world");
		*/		
		return null;
	}
}
