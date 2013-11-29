package rsync.actions;

import org.eclipse.jface.action.IAction;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import rsync.RsyncPlugin;



public class rsyncPutOnly extends Selection implements IWorkbenchWindowActionDelegate {
	/**
	 * The constructor.
	 */
	public rsyncPutOnly() {
	}

	/**
	 * The action has been activated. The argument of the
	 * method represents the 'real' action sitting
	 * in the workbench UI.
	 * @see IWorkbenchWindowActionDelegate#run
	 */
	public void run(IAction action) {
		// eseguo il comando di PUT -- carica in remoto ma non lo sincronizza con lo stato attuale che ho in locale
		if (m_selection != null && !m_selection.isEmpty()){
			RsyncPlugin.setProjectName(getProjectName());
		} else {
			RsyncPlugin.setProjectName("-1");
		}			
		RsyncPlugin.execRsyncCommand("Uploading to remote server...",6);
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