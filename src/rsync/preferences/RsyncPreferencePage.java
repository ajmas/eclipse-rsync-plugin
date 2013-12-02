package rsync.preferences;

import org.eclipse.jface.preference.*;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.IWorkbench;

import rsync.RsyncPlugin;

public class RsyncPreferencePage
	extends FieldEditorPreferencePage
	implements IWorkbenchPreferencePage {

	public RsyncPreferencePage() {
		super(GRID);
		setPreferenceStore(RsyncPlugin.getDefault().getPreferenceStore());
		setDescription(Messages.MESG_RSYNC_PREFERENCES);
	}
	
	/**
	 * Creates the field editors. Field editors are abstractions of
	 * the common GUI blocks needed to manipulate various types
	 * of preferences. Each field editor knows how to save and
	 * restore itself.
	 */
	public void createFieldEditors() {
		addField(new DirectoryFieldEditor(PreferenceConstants.P_PATH_RSYNC, Messages.MESG_RSYNC_LOCATION, getFieldEditorParent()));
		addField(new StringFieldEditor(PreferenceConstants.P_PAR_RSYNC_PUTGET, Messages.MESG_SYNC_PARAMETERS, getFieldEditorParent()));
		addField(new StringFieldEditor(PreferenceConstants.P_PAR_RSYNC_PUTGET_ONLY, Messages.MESG_UPLOAD_DOWNLOAD_PARAMETERS, getFieldEditorParent()));
		addField(new StringFieldEditor(PreferenceConstants.P_PAR_RSYNC_RUNDRY, Messages.MESG_SYNC_SIMULATION_PARAMETERS, getFieldEditorParent()));	
		addField(new StringFieldEditor(PreferenceConstants.P_PAR_RSYNC_EXCLUSIONS, Messages.MESG_FILES_FOLDER_TO_EXCLUDE, getFieldEditorParent()));		
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
	 */
	public void init(IWorkbench workbench) {
	}
	
}