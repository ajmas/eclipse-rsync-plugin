package rsync;

import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.plugin.*;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.resource.ImageDescriptor;
import org.osgi.framework.BundleContext;

import rsync.ui.ConsoleDisplayMgr;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.QualifiedName;

import rsync.preferences.PreferenceConstants;
import rsync.properties.IProperty;

import org.eclipse.ui.IWorkbenchWindow;

import java.io.*;

/**
 * @class RsyncPlugin
 * @author Alfredo Cerutti
 * @date 29/08/2006
 * 
 * Questo plugin permette di sfruttare il tool rsync dentro eclipse per sincronizzare i progetti locali
 * con la copia remoto (e viceversa).
 * 
 * Il plugin non implementa la gestione della password di login -- occorre creare una chiave pubblica e
 * pubblicarla sul server affinche' non venga richiesta (potrebbe servire anche modificare alcune impostazioni
 * del server ssh)
 * 
 */
public class RsyncPlugin extends AbstractUIPlugin {

	//The shared instance.
	private static RsyncPlugin plugin;
	public static ConsoleDisplayMgr cons = ConsoleDisplayMgr.getDefault("Rsync console"); //$NON-NLS-1$
	private String m_projectName=""; //$NON-NLS-1$
    
	/**
	 * The constructor.
	 */
	public RsyncPlugin() {
		plugin = this;
	}
	
	/**
	 * This method is called upon plug-in activation
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
	}

	/**
	 * This method is called when the plug-in is stopped
	 */
	public void stop(BundleContext context) throws Exception {
		super.stop(context);
		plugin = null;
	}

	public static void setProjectName(String projectName)
	{
		plugin.m_projectName=projectName;
	}
	
	/**
	 * consolePrintln
	 * 
	 * scrive nella console il messaggio passato come argomento
	 * @param msg -- messaggio da scrivere in console
	 */
	public static void consolePrintln(String msg) {
		cons.println(msg);
	}
	
	public static IWorkbenchWindow getActiveWorkbenchWindow() {
		return getDefault().getWorkbench().getActiveWorkbenchWindow();
	}

	public static IWorkbenchPage getActivePage() {
		return RsyncPlugin.getActiveWorkbenchWindow().getActivePage();
	}	

	/**
	 * getShell
	 * 
	 * questo metodo ritorna la parent shell a cui poter aggangiare le message dialog
	 * 
	 * @return la shell
	 */
	public static Shell getShell() {
		IWorkbenchWindow window = RsyncPlugin.getActiveWorkbenchWindow();
		return window.getShell();
	}		
	
	/**
	 * execRsyncCommand
	 * 
	 * metodo che esegue il comando rsync associato al parametro passato come argomento
	 * 
	 * @param msgConsole -- messaggio da mostrare il console
	 * @param cmdRsyncType -- tipo di azione (1=GET,2=PUT,3=GET test,4=PUT test,5=GET ONLY,6=PUT ONLY)
	 * 
	 */
	public static void execRsyncCommand(String msgConsole, int cmdRsyncType)
	{			
		/**
		 * PREFERENZE GLOBALI... sempre leggibili
		 */
		IPreferenceStore store = RsyncPlugin.getDefault().getPreferenceStore();		
		String pathRsync = store.getString(PreferenceConstants.P_PATH_RSYNC);
		String putGet =  " "+store.getString(PreferenceConstants.P_PAR_RSYNC_PUTGET)+" "; //$NON-NLS-1$ //$NON-NLS-2$
		String runDry =  " "+store.getString(PreferenceConstants.P_PAR_RSYNC_RUNDRY)+" "; //$NON-NLS-1$ //$NON-NLS-2$
		String putGetOnly =  " "+store.getString(PreferenceConstants.P_PAR_RSYNC_PUTGET_ONLY)+" "; //$NON-NLS-1$ //$NON-NLS-2$
		String excludeGlobal = " "+store.getString(PreferenceConstants.P_PAR_RSYNC_EXCLUSIONS)+" "; //$NON-NLS-1$ //$NON-NLS-2$

		// elimino i ritorni a capo
		excludeGlobal = excludeGlobal.replace("\n"," "); //$NON-NLS-1$ //$NON-NLS-2$
		excludeGlobal = excludeGlobal.replace("\r\n"," "); //$NON-NLS-1$ //$NON-NLS-2$
		excludeGlobal = excludeGlobal.replace("\r"," ");		 //$NON-NLS-1$ //$NON-NLS-2$
		
		/**
		 * REFERENZIO IL PROGETTO...
		 */
		IWorkspace ws = ResourcesPlugin.getWorkspace(); // prendo l'area di lavoro 
		if (plugin.m_projectName!="-1") //$NON-NLS-1$
		{
			IProject prj = ws.getRoot().getProject(plugin.m_projectName); // ricavatto dalla classe Selection.java
			
			/**
			 * RECUPERO LE PREFERENZE PER PROGETTO
			 */
			try {
				String serverName =""; // nome server remoto //$NON-NLS-1$
				try {
					serverName= prj.getPersistentProperty(new QualifiedName("", IProperty.SERVERNAME)); //$NON-NLS-1$
				} catch (CoreException e) {
					serverName =""; //$NON-NLS-1$
					RsyncPlugin.consolePrintln(Messages.MESG_SERVER_ERROR_RETRIEVING_SERVER_NAME);
				}
				String userName =""; // nome utente //$NON-NLS-1$
				try {
					userName= prj.getPersistentProperty(new QualifiedName("", IProperty.USERNAME)); //$NON-NLS-1$
				} catch (CoreException e) {
					userName =""; //$NON-NLS-1$
					RsyncPlugin.consolePrintln(Messages.MESG_ERRROR_RETRIEVING_USERNAME);
				}
				String remotePath =""; // percorso remoto //$NON-NLS-1$
				try {
					remotePath= prj.getPersistentProperty(new QualifiedName("", IProperty.REMOTEPATH)); //$NON-NLS-1$
				} catch (CoreException e) {
					remotePath =""; //$NON-NLS-1$
					RsyncPlugin.consolePrintln(Messages.MESG_ERROR_RETRIEVING_REMOTE_PATH);
				}
				String excludePrj =""; // elenco dei file o directory da escludere //$NON-NLS-1$
				try {
					excludePrj= prj.getPersistentProperty(new QualifiedName("", IProperty.EXCLUDE)); //$NON-NLS-1$
					
					// elimino i ritorni a capo
					excludePrj = excludePrj.replace("\n"," "); //$NON-NLS-1$ //$NON-NLS-2$
					excludePrj = excludePrj.replace("\r\n"," "); //$NON-NLS-1$ //$NON-NLS-2$
					excludePrj = excludePrj.replace("\r"," ");	 //$NON-NLS-1$ //$NON-NLS-2$
					
				} catch (CoreException e) {
					excludePrj =""; //$NON-NLS-1$
					RsyncPlugin.consolePrintln(Messages.MESG_ERROR_RETRIEVING_EXCLUDED_DIRS);
				}					
				
				/**
				 * AGGIUSTO IL PERCORSO REMOTO... in questo modo mi assicuro di eliminare eventuali "/" finali
				 */
				String strArray[] = remotePath.split("/"); //$NON-NLS-1$
				remotePath=""; //$NON-NLS-1$
				for (int i_idx=0; i_idx < strArray.length; i_idx++)
				{
					// RsyncPlugin.consolePrintln("/ --- "+strArray[i_idx]+"\n"); // x DEBUG
					if (!strArray[i_idx].equals("")) remotePath = remotePath.concat("/"+strArray[i_idx]); //$NON-NLS-1$ //$NON-NLS-2$
				}
				
				/**
				 * PREPARO IL COMANDO "rsync"
				 */			
				String CMD_bash="/bin/sh"; //$NON-NLS-1$
				String rsync_cmd="";		 //$NON-NLS-1$
				String rsync_path=pathRsync+"/rsync "; // dove si trova rsync //$NON-NLS-1$
				String login_at_machine=" "+userName+"@"+serverName+":"; // nome login e host remoto //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				String localPath = prj.getLocation().toPortableString(); // path locale del progetto
				String fullExclude = excludeGlobal + " " + excludePrj + " "; // elenco completo delle esclusioni //$NON-NLS-1$ //$NON-NLS-2$
				if (remotePath.equals("") || userName.equals("") || serverName.equals("")) //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				{
					/**
					 * cavolo... non ho impostato tutte le preferenze !!
					 */
					MessageDialog.openError(RsyncPlugin.getShell(),Messages.MESG_ERROR,Messages.MESG_PROJECT_SETUP_REQUIRED);
				} else {
					// determino il tipo di azione da eseguire...
					switch (cmdRsyncType)
					{
						case 1: // GET -- scarica il local gli aggiornamenti e sincronizza la copia locale con quella remota (che comanda)				
							rsync_cmd= rsync_path + putGet + fullExclude + login_at_machine + remotePath + "/ " +  localPath;								 //$NON-NLS-1$
							break;
						case 2: // PUT -- carica il remoto gli aggiornamenti e sincronizza la copia remota con quella locale (che comanda)
							rsync_cmd= rsync_path + putGet + fullExclude + localPath + "/ " + login_at_machine + remotePath; //$NON-NLS-1$
							break;
						case 3: // GET solo test di sincronizzazione
							rsync_cmd= rsync_path + runDry + fullExclude + login_at_machine + remotePath + "/ " + localPath; //$NON-NLS-1$
							break;
						case 4: // PUT solo test di sincronizzazione
							rsync_cmd= rsync_path + runDry + fullExclude + localPath + "/ " + login_at_machine + remotePath; //$NON-NLS-1$
							break;
						case 5: // GET -- scarica in locale gli aggiornamenti remoti -- non sincronziza
							rsync_cmd= rsync_path + putGetOnly + fullExclude + login_at_machine + remotePath + "/ " + localPath; //$NON-NLS-1$
							break;
						case 6: // PUT -- carica in remoto gli aggiornamenti locali -- non sincronizza
							rsync_cmd= rsync_path + putGetOnly + fullExclude + localPath + "/ " + login_at_machine + remotePath; //$NON-NLS-1$
							break;					
					}
					String[] strCmd= {CMD_bash,"-c",rsync_cmd}; //$NON-NLS-1$
					//RsyncPlugin.consolePrintln(msgConsole+": "+rsync_cmd); // x DEBUG
					
					/**
					 * ESEGUO IL COMANDO "rsync"
					 */		
					try {
						// scrivo in console cosa sto facendo
						RsyncPlugin.consolePrintln("\n"); //$NON-NLS-1$
						RsyncPlugin.consolePrintln("-----------------------------------------------------------------------------"); //$NON-NLS-1$
						RsyncPlugin.consolePrintln(msgConsole+": "+rsync_cmd); //$NON-NLS-1$
						Runtime rt = Runtime.getRuntime();
						// eseguo il comando
				        	Process pr = rt.exec(strCmd);

				        	// prelevo l'output generato da rsync e lo scrivo in console
				        try {
					        InputStream isRsync = pr.getInputStream();
				            InputStreamReader isrRsync = new InputStreamReader(isRsync);
				            BufferedReader brRsync = new BufferedReader(isrRsync);
				            String lineRsync = null;
				            while ( (lineRsync= brRsync.readLine()) != null)
				            	RsyncPlugin.consolePrintln(lineRsync);
				        } catch (NullPointerException e) {
				        		e.printStackTrace();
				        } catch (IOException e) {
				        		e.printStackTrace();
				        }	  
				        
				        // prelevo l'output degli errori da rsync e lo scrivo in console
				        try {
					        InputStream isRsync = pr.getErrorStream();
				            InputStreamReader isrRsync = new InputStreamReader(isRsync);
				            BufferedReader brRsync = new BufferedReader(isrRsync);
				            String lineRsync = null;
				            while ( (lineRsync= brRsync.readLine()) != null)
				            	RsyncPlugin.consolePrintln(lineRsync);
				        } catch (NullPointerException e) {
				        		e.printStackTrace();
				        } catch (IOException e) {
				        		e.printStackTrace();
				        }	
				        
				        RsyncPlugin.consolePrintln("\n"); //$NON-NLS-1$
				        pr.waitFor();
				        pr.destroy(); // distruggo la referenza a rsync
				        rt= null;
				        pr=null;			
					} catch (InterruptedException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}					
				}
				
				try {
					prj.refreshLocal(IResource.DEPTH_INFINITE, null);
				} catch (CoreException e) {
					// TODO: handle exception
				}
				
			} catch (NullPointerException e) {
				MessageDialog.openError(RsyncPlugin.getShell(),Messages.MESG_ERROR,Messages.MESG_ERROR_MISSING_PROJECT_PREFERENCES);
			}											
		} else {
			MessageDialog.openError(RsyncPlugin.getShell(),Messages.MESG_ERROR,Messages.MESG_ERROR_NO_SELECTED_PROJECT);			
		}
	}
		
	/**
	 * Returns the shared instance.
	 */
	public static RsyncPlugin getDefault() {
		return plugin;
	}
	
	/**
	 * Returns an image descriptor for the image file at the given
	 * plug-in relative path.
	 *
	 * @param path the path
	 * @return the image descriptor
	 */
	public static ImageDescriptor getImageDescriptor(String path) {
		return AbstractUIPlugin.imageDescriptorFromPlugin("rsync", path); //$NON-NLS-1$
	}
}
