package com.dforensic.plugin.manal.wizards;

import java.io.File;

import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

/**
 * The "New" properties wizard page allows setting the properties of the Manal
 * project for suspect analysis. That properties are a project name, a path to
 * an apk, a path to the android SDK.
 */

public class WizardManalPropertiesPage extends WizardPage {
	private Text projectNameText;
	private Text apkFileText;
	private Text sourcePrjDirectoryText;
	private Composite parent;

	public Composite getParent() {
		return this.parent;
	}

	public WizardManalPropertiesPage() {
		super("wizardPage");
		setTitle("Set properties of suspect analysis project");
		setDescription("Set project name, path to apk and path to anroid "
				+ "platform for consturction of the project.");
	}

	/**
	 * @see IDialogPage#createControl(Composite)
	 */
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		this.parent = parent;
		GridLayout layout = new GridLayout();
		container.setLayout(layout);
		layout.numColumns = 3;
		layout.verticalSpacing = 9;

		// Input project name
		Label label = new Label(container, SWT.NULL);
		label.setText("&Project name:");

		projectNameText = new Text(container, SWT.BORDER | SWT.SINGLE);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		projectNameText.setLayoutData(gd);
		projectNameText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {

			}
		});

		// Input apk path
		label = new Label(container, SWT.NULL);
		label.setText("&Apk file name:");

		apkFileText = new Text(container, SWT.BORDER | SWT.SINGLE);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		apkFileText.setLayoutData(gd);
		apkFileText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				dialogApkChanged();
			}
		});

		Button browseApkBtn = new Button(container, SWT.PUSH);
		browseApkBtn.setText("Browse...");
		browseApkBtn.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				handleBrowseApk();
			}
		});

		// Input decompiled project path
		label = new Label(container, SWT.NULL);
		label.setText("&Decompiled eclipse project directory:");
		
		sourcePrjDirectoryText = new Text(container, SWT.BORDER | SWT.SINGLE);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		sourcePrjDirectoryText.setLayoutData(gd);
		sourcePrjDirectoryText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				dialogSourceDirectoryChanged();
			}
		});

		Button browseSourceDirectoryBtn = new Button(container, SWT.PUSH);
		browseSourceDirectoryBtn.setText("Browse...");
		browseSourceDirectoryBtn.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				handleBrowseDecompiledSource();
			}
		});

		dialogChanged();
		setControl(container);
	}

	// Refer to the following FAQ
	// http://wiki.eclipse.org/FAQ_How_do_I_prompt_the_user_to_select_a_file_or_a_directory%3F
	private void handleBrowseDecompiledSource() {
		//get object which represents the workspace  
		IWorkspace workspace = ResourcesPlugin.getWorkspace();  
		//get location of workspace (java.io.File)  
		File workspaceDirectory = workspace.getRoot().getLocation().toFile();
		String workspaceDirPath = null;
		if (workspaceDirectory != null) {
			workspaceDirPath = workspaceDirectory.getPath();
		}
		DirectoryDialog dirDialog = new DirectoryDialog(getShell());
		dirDialog.setText("Select Eclipse Project Directory");
		dirDialog.setFilterPath(workspaceDirPath);
		String selected = dirDialog.open();
		System.out.println(selected); // decompiled project

		if (selected != null) {
			sourcePrjDirectoryText.setText(selected);
		}
	}
	
	private void handleBrowseApk() {
		FileDialog fileDialog = new FileDialog(getShell());
		fileDialog.setText("Select Apk File");
		fileDialog.setFilterExtensions(new String[] { "*.apk" });
		fileDialog.setFilterNames(new String[] { "apk files(*.apk)" });
		String selected = fileDialog.open();
		System.out.println(selected); //apkname

		if (selected != null) {
			apkFileText.setText(selected);
		}
	}

	private void dialogApkChanged() {
		String apkFileName = getApkFileName();

		if (apkFileName.length() == 0) {
			updateStatus("Apk file name must be specified");
			return;
		}
		if (apkFileName.replace('\\', '/').indexOf('/', 1) > 0) {
			updateStatus("Apk file name must be valid");
			return;
		}
		updateStatus(null);
	}
	
	private void dialogSourceDirectoryChanged() {
		String directoryName = getDecompiledSourceDirectoryName();

		if (directoryName.length() == 0) {
			updateStatus("Eclipse project directory of the decompiled apk must be specified");
			return;
		}
		if (directoryName.replace('\\', '/').indexOf('/', 1) > 0) {
			updateStatus("Eclipse project directory of the decompiled apk must be valid");
			return;
		}
		updateStatus(null);
	}
	
	private void dialogChanged() {
		dialogApkChanged();
		dialogSourceDirectoryChanged();
	}

	private void updateStatus(String message) {
		setErrorMessage(message);
		setPageComplete(message == null);
	}

	public String getProjectName() {
		return projectNameText.getText();
	}

	public String getApkFileName() {
		return apkFileText.getText();
	}
	
	public String getDecompiledSourceDirectoryName() {
		return sourcePrjDirectoryText.getText();
	}
}