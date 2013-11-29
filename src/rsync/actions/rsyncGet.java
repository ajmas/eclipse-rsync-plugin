package rsync.actions;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import rsync.RsyncPlugin;

public class rsyncGet extends Selection implements IWorkbenchWindowActionDelegate {
	/**
	 * The constructor.
	 */
	public rsyncGet() {
		
	}

	/**
	 * The action has been activated. The argument of the
	 * method represents the 'real' action sitting
	 * in the workbench UI.
	 * @see IWorkbenchWindowActionDelegate#run
	 */
	public void run(IAction action) {
		// eseguo il comando di GET -- sincronizzando la copia locale con la remota
		if (m_selection != null && !m_selection.isEmpty()){
			RsyncPlugin.setProjectName(getProjectName());
		} else {
			RsyncPlugin.setProjectName("-1");
		}
		boolean b_sync = MessageDialog.openQuestion(RsyncPlugin.getShell(),"Syncronize","Are you sure to syncronize you local project?");
		if (b_sync) RsyncPlugin.execRsyncCommand("Sincronizing local project...",1);		
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