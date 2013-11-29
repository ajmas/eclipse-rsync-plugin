package rsync.actions;

import rsync.RsyncPlugin;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.IActionDelegate;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;


abstract public class Selection implements IObjectActionDelegate {
	
	/**
	 * The selection
	 */
    protected IStructuredSelection m_selection;
	
	public Selection() {
		super();
	}
	
	/**
	 * @see IObjectActionDelegate#setActivePart(IAction, IWorkbenchPart)
	 */
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {}
	
	public abstract void run(IAction action);

	/**
	 * getProjectName
	 * 
	 * metodo che si preoccupa di recuperare il nome del progetto attualmente selezionato
	 * nell'editor
	 * 
	 * @return la stringa del progetto (vuota se non lo trova)
	 */
	protected String getProjectName()
	{
		Object res[] = m_selection.toArray();
		String projectName="";
		for(int a=0;a<res.length;a++)
		{
			if (res[a] instanceof IResource) 
			{
				IResource r = (IResource)res[a];
				IProject p = r.getProject();
				if (p==null) 
				{
					projectName ="";				
				} else {
					projectName = p.getName();
				}
			}
		}
		return projectName;
	}
	
	/**
	 * @see IActionDelegate#selectionChanged(IAction, ISelection)
	 */	
 	public void selectionChanged(IAction action, ISelection s) {
 		if (s != null) {
 			if (s instanceof IStructuredSelection){
 				m_selection = (IStructuredSelection)s;
 			}
 			
 			 if (s instanceof ITextSelection){
 				IEditorPart part = RsyncPlugin.getActivePage().getActiveEditor();
 				if (part != null) {
 					IEditorInput input = part.getEditorInput();
 					IResource r = (IResource) input.getAdapter(IResource.class);
 					if (r != null) {
 						switch(r.getType()){
 							case IResource.FILE:
 								m_selection = new StructuredSelection(r);								
 							break;
 						}
 					}	//	set selection to current editor file;
 				}
 			}else{			
 			}
 		}	
 	}		
}
