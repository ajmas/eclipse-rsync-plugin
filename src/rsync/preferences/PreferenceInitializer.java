package rsync.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

import rsync.RsyncPlugin;

/**
 * Class used to initialize default preference values.
 */
public class PreferenceInitializer extends AbstractPreferenceInitializer {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer#initializeDefaultPreferences()
	 */
	public void initializeDefaultPreferences() {
		IPreferenceStore store = RsyncPlugin.getDefault().getPreferenceStore();

	
		store.setDefault(PreferenceConstants.P_PATH_RSYNC, "/usr/bin");
		store.setDefault(PreferenceConstants.P_PAR_RSYNC_PUTGET, "-Crav --delete");
		store.setDefault(PreferenceConstants.P_PAR_RSYNC_RUNDRY, "-Crnuv --delete");
		store.setDefault(PreferenceConstants.P_PAR_RSYNC_PUTGET_ONLY, "-Cauv");
		store.setDefault(PreferenceConstants.P_PAR_RSYNC_EXCLUSIONS,"--exclude='.DS_STORE'\n --exclude='.classpath'\n --exclude='.project'\n --exclude='.projectOptions'\n --exclude='.cache'\n --exclude='.settings'");
		
	}

}
