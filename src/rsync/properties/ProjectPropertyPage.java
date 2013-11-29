package rsync.properties;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.PropertyPage;

public class ProjectPropertyPage extends PropertyPage {

	private static final String SERVERNAME_TITLE = "Server name:";
	private static final String USERNAME_TITLE = "User name:";
	private static final String REMOTEPATH_TITLE = "Remote path";
	private static final String EXCLUDE_TITLE = "Do not sycronize the following dirs/files:\n(use --exclude='XYZ' for each exclusion):";
	
	private Text serverText;
	private Text userText;
	private Text remotepathText;
	private Text excludeText;

	/**
	 * Constructor for SamplePropertyPage.
	 */
	public ProjectPropertyPage() {
		super();
	}

	/**
	 * prepareItems
	 * 
	 */
	private void prepareItems(Composite parent) {
		Composite composite = createDefaultComposite(parent);

		/*
		 * ---------------- server name
		 */
		Label serverLabel = new Label(composite, SWT.NONE);
		serverLabel.setText(SERVERNAME_TITLE);

		serverText = new Text(composite, SWT.SINGLE | SWT.BORDER);
		
		// creo il layout in cui inserire la casella di testo
		GridData gd = new GridData();
		gd.widthHint = convertWidthInCharsToPixels(40);
		serverText.setLayoutData(gd);

		try {
			String serverValue = ((IResource) getElement()).getPersistentProperty(new QualifiedName("", IProperty.SERVERNAME));
			serverText.setText((serverValue != null) ? serverValue : "");
		} catch (CoreException e) {
			serverText.setText("");
		}		
		
		/*
		 * ---------------- nome utente
		 */
		Label userLabel = new Label(composite, SWT.NONE);
		userLabel.setText(USERNAME_TITLE);


		userText = new Text(composite, SWT.SINGLE | SWT.BORDER);
		userText.setLayoutData(gd);


		try {
			String userValue = ((IResource) getElement()).getPersistentProperty(new QualifiedName("", IProperty.USERNAME));
			userText.setText((userValue != null) ? userValue : "");
		} catch (CoreException e) {
			userText.setText("");
		}	
		
		/*
		 * ---------------- percorso remoto
		 */
		Label remotePathLabel = new Label(composite, SWT.NONE);
		remotePathLabel.setText(REMOTEPATH_TITLE);


		remotepathText= new Text(composite, SWT.SINGLE | SWT.BORDER);
		remotepathText.setLayoutData(gd);


		try {
			String serverPathValue = ((IResource) getElement()).getPersistentProperty(new QualifiedName("", IProperty.REMOTEPATH));
			remotepathText.setText((serverPathValue != null) ? serverPathValue : "");
		} catch (CoreException e) {
			remotepathText.setText("");
		}		

		/*
		 * ---------------- esclusioni del progetto
		 */
		GridData gd_multi = new GridData();
		gd_multi.widthHint = convertWidthInCharsToPixels(40);
		gd_multi.heightHint = convertWidthInCharsToPixels(15);	
		Label excludeLabel = new Label(composite, SWT.NONE);
		excludeLabel.setText(EXCLUDE_TITLE);

		excludeText= new Text(composite, SWT.MULTI | SWT.WRAP | SWT.V_SCROLL | SWT.BORDER);			
		excludeText.setLayoutData(gd_multi);


		try {
			String excludeValue = ((IResource) getElement()).getPersistentProperty(new QualifiedName("", IProperty.EXCLUDE));
			excludeText.setText((excludeValue != null) ? excludeValue : "");
		} catch (CoreException e) {
			excludeText.setText("");
		}			
	}

	/**
	 * @see PreferencePage#createContents(Composite)
	 */
	protected Control createContents(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		composite.setLayout(layout);
		GridData data = new GridData(GridData.FILL);
		data.grabExcessHorizontalSpace = true;
		composite.setLayoutData(data);

		prepareItems(composite); // creo gli elementi 

		return composite;
	}

	private Composite createDefaultComposite(Composite parent) {
		Composite composite = new Composite(parent, SWT.NULL);
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		composite.setLayout(layout);

		GridData data = new GridData();
		data.verticalAlignment = GridData.FILL;
		data.horizontalAlignment = GridData.FILL;
		composite.setLayoutData(data);

		return composite;
	}

	protected void performDefaults() {
		// Populate the owner text field with the default value
		serverText.setText("");
		userText.setText("");
		remotepathText.setText("");
		excludeText.setText("");	
	}
	
	public boolean performOk() {

		/*
		 * ---------------- server name
		 */
		try {
			((IResource) getElement()).setPersistentProperty(
				new QualifiedName("", IProperty.SERVERNAME),
				serverText.getText());
		} catch (CoreException e) {
			return false;
		}
		
		/*
		 * ---------------- nome utente
		 */		
		try {
			((IResource) getElement()).setPersistentProperty(
				new QualifiedName("", IProperty.USERNAME),
				userText.getText());
		} catch (CoreException e) {
			return false;
		}
		

		/*
		 * ---------------- directory remota
		 */
		try {
			((IResource) getElement()).setPersistentProperty(
				new QualifiedName("", IProperty.REMOTEPATH),
				remotepathText.getText());
		} catch (CoreException e) {
			return false;
		}
		
		/*
		 * ---------------- esclusioni
		 */
		try {
			((IResource) getElement()).setPersistentProperty(
				new QualifiedName("", IProperty.EXCLUDE),
				excludeText.getText());
		} catch (CoreException e) {
			return false;
		}
		
		return true;
	}

}