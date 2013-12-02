package rsync.ui;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "rsync.ui.messages"; //$NON-NLS-1$
	public static String MESG_ERROR_GETTING_CONSOLE_STREAM;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
