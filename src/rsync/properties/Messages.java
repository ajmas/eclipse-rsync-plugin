package rsync.properties;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "rsync.properties.messages"; //$NON-NLS-1$
	public static String MESG_DO_NOT_SYNC_SELECTED_DIRS;
	public static String MESG_REMOTE_PATH;
	public static String MESG_SERVER_NAME;
	public static String MESG_USER_NAME;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
