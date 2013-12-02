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

	private static final String SERVERNAME_TITLE = Messages.MESG_SERVER_NAME;
	private static final String USERNAME_TITLE = Messages.MESG_USER_NAME;
	private static final String REMOTEPATH_TITLE = Messages.MESG_REMOTE_PATH;
	private static final String EXCLUDE_TITLE = Messages.MESG_DO_NOT_SYNC_SELECTED_DIRS;
	
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
			String serverValue = ((IResource) getElement()).getPersistentProperty(new QualifiedName("", IProperty.SERVERNAME)); //$NON-NLS-1$
			serverText.setText((serverValue != null) ? serverValue : ""); //$NON-NLS-1$
		} catch (CoreException e) {
			serverText.setText(""); //$NON-NLS-1$
		}		
		
		/*
		 * ---------------- nome utente
		 */
		Label userLabel = new Label(composite, SWT.NONE);
		userLabel.setText(USERNAME_TITLE);


		userText = new Text(composite, SWT.SINGLE | SWT.BORDER);
		userText.setLayoutData(gd);


		try {
			String userValue = ((IResource) getElement()).getPersistentProperty(new QualifiedName("", IProperty.USERNAME)); //$NON-NLS-1$
			userText.setText((userValue != null) ? userValue : ""); //$NON-NLS-1$
		} catch (CoreException e) {
			userText.setText(""); //$NON-NLS-1$
		}	
		
		/*
		 * ---------------- percorso remoto
		 */
		Label remotePathLabel = new Label(composite, SWT.NONE);
		remotePathLabel.setText(REMOTEPATH_TITLE);


		remotepathText= new Text(composite, SWT.SINGLE | SWT.BORDER);
		remotepathText.setLayoutData(gd);


		try {
			String serverPathValue = ((IResource) getElement()).getPersistentProperty(new QualifiedName("", IProperty.REMOTEPATH)); //$NON-NLS-1$
			remotepathText.setText((serverPathValue != null) ? serverPathValue : ""); //$NON-NLS-1$
		} catch (CoreException e) {
			remotepathText.setText(""); //$NON-NLS-1$
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
			String excludeValue = ((IResource) getElement()).getPersistentProperty(new QualifiedName("", IProperty.EXCLUDE)); //$NON-NLS-1$
			excludeText.setText((excludeValue != null) ? excludeValue : ""); //$NON-NLS-1$
		} catch (CoreException e) {
			excludeText.setText(""); //$NON-NLS-1$
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
		serverText.setText(""); //$NON-NLS-1$
		userText.setText(""); //$NON-NLS-1$
		remotepathText.setText(""); //$NON-NLS-1$
		excludeText.setText("");	 //$NON-NLS-1$
	}
	
	public boolean performOk() {

		/*
		 * ---------------- server name
		 */
		try {
			((IResource) getElement()).setPersistentProperty(
				new QualifiedName("", IProperty.SERVERNAME), //$NON-NLS-1$
				serverText.getText());
		} catch (CoreException e) {
			return false;
		}
		
		/*
		 * ---------------- nome utente
		 */		
		try {
			((IResource) getElement()).setPersistentProperty(
				new QualifiedName("", IProperty.USERNAME), //$NON-NLS-1$
				userText.getText());
		} catch (CoreException e) {
			return false;
		}
		

		/*
		 * ---------------- directory remota
		 */
		try {
			((IResource) getElement()).setPersistentProperty(
				new QualifiedName("", IProperty.REMOTEPATH), //$NON-NLS-1$
				remotepathText.getText());
		} catch (CoreException e) {
			return false;
		}
		
		/*
		 * ---------------- esclusioni
		 */
		try {
			((IResource) getElement()).setPersistentProperty(
				new QualifiedName("", IProperty.EXCLUDE), //$NON-NLS-1$
				excludeText.getText());
		} catch (CoreException e) {
			return false;
		}
		
		return true;
	}

}