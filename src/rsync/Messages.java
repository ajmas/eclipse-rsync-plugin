package rsync;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "rsync.messages"; //$NON-NLS-1$
	public static String MESG_ERROR;
	public static String MESG_ERROR_MISSING_PROJECT_PREFERENCES;
	public static String MESG_ERROR_NO_SELECTED_PROJECT;
	public static String MESG_ERROR_RETRIEVING_EXCLUDED_DIRS;
	public static String MESG_ERROR_RETRIEVING_REMOTE_PATH;
	public static String MESG_ERRROR_RETRIEVING_USERNAME;
	public static String MESG_PROJECT_SETUP_REQUIRED;
	public static String MESG_SERVER_ERROR_RETRIEVING_SERVER_NAME;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
