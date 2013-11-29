package rsync.actions;

import org.eclipse.jface.action.IAction;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import rsync.RsyncPlugin;


public class rsyncGetOnly extends Selection implements IWorkbenchWindowActionDelegate {
	/**
	 * The constructor.
	 */
	public rsyncGetOnly() {
	}

	/**
	 * The action has been activated. The argument of the
	 * method represents the 'real' action sitting
	 * in the workbench UI.
	 * @see IWorkbenchWindowActionDelegate#run
	 */
	public void run(IAction action) {
		// eseguo il comando di GET -- scarica in locale  ma non lo sincronizza con lo stato attuale che ho in remoto
		if (m_selection != null && !m_selection.isEmpty()){
			RsyncPlugin.setProjectName(getProjectName());
		} else {
			RsyncPlugin.setProjectName("-1");
		}			
		RsyncPlugin.execRsyncCommand("Downloading from remote server...",5);
	}

	/**
	 * We can use this method to dispose of any system
	 * resources we previously allocated.
	 * @see IWorkbenchWindowActionDelegate#dispose
	 */
	public void dispose() {
	}

	/**
	 * We will cache window object in order to
	 * be able to provide parent shell for the message dialog.
	 * @see IWorkbenchWindowActionDelegate#init
	 */
	public void init(IWorkbenchWindow window) {
	}
}