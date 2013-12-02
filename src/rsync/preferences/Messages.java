package rsync.preferences;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "rsync.preferences.messages"; //$NON-NLS-1$
	public static String MESG_FILES_FOLDER_TO_EXCLUDE;
	public static String MESG_RSYNC_LOCATION;
	public static String MESG_RSYNC_PREFERENCES;
	public static String MESG_SYNC_PARAMETERS;
	public static String MESG_SYNC_SIMULATION_PARAMETERS;
	public static String MESG_UPLOAD_DOWNLOAD_PARAMETERS;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
