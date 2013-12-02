package rsync.actions;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "rsync.actions.messages"; //$NON-NLS-1$
	public static String MESG_MESG_CONFIRM_SYNCHRONIZE_TEXT;
	public static String MESG_SYNCHRONIZING_REMOTE_PROJECT;
	public static String MESG_TITLE_SYNCHRONIZE;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
