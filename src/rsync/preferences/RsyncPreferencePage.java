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
		setDescription("Preferenze di rsync");
	}
	
	/**
	 * Creates the field editors. Field editors are abstractions of
	 * the common GUI blocks needed to manipulate various types
	 * of preferences. Each field editor knows how to save and
	 * restore itself.
	 */
	public void createFieldEditors() {
		addField(new DirectoryFieldEditor(PreferenceConstants.P_PATH_RSYNC, "Dove si trova rsync:", getFieldEditorParent()));
		addField(new StringFieldEditor(PreferenceConstants.P_PAR_RSYNC_PUTGET, "Parametri per sincronizzazione locale/remoto (e viceversa):", getFieldEditorParent()));
		addField(new StringFieldEditor(PreferenceConstants.P_PAR_RSYNC_PUTGET_ONLY, "Parametri per il solo upload/download aggiornamenti:", getFieldEditorParent()));
		addField(new StringFieldEditor(PreferenceConstants.P_PAR_RSYNC_RUNDRY, "Parametri per la simulazione  di sincronizzazione locale/remoto (e viceversa):", getFieldEditorParent()));	
		addField(new StringFieldEditor(PreferenceConstants.P_PAR_RSYNC_EXCLUSIONS, "Escludi per tutti i progetti i seguenti file o directory\n(usare sempre la formula --exclude='XYZ'):", getFieldEditorParent()));		
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
	 */
	public void init(IWorkbench workbench) {
	}
	
}