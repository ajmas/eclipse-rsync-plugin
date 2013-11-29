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
	public static ConsoleDisplayMgr cons = ConsoleDisplayMgr.getDefault("Rsync console");
	private String m_projectName="";
    
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
		String putGet =  " "+store.getString(PreferenceConstants.P_PAR_RSYNC_PUTGET)+" ";
		String runDry =  " "+store.getString(PreferenceConstants.P_PAR_RSYNC_RUNDRY)+" ";
		String putGetOnly =  " "+store.getString(PreferenceConstants.P_PAR_RSYNC_PUTGET_ONLY)+" ";
		String excludeGlobal = " "+store.getString(PreferenceConstants.P_PAR_RSYNC_EXCLUSIONS)+" ";

		// elimino i ritorni a capo
		excludeGlobal = excludeGlobal.replace("\n"," ");
		excludeGlobal = excludeGlobal.replace("\r\n"," ");
		excludeGlobal = excludeGlobal.replace("\r"," ");		
		
		/**
		 * REFERENZIO IL PROGETTO...
		 */
		IWorkspace ws = ResourcesPlugin.getWorkspace(); // prendo l'area di lavoro 
		if (plugin.m_projectName!="-1")
		{
			IProject prj = ws.getRoot().getProject(plugin.m_projectName); // ricavatto dalla classe Selection.java
			
			/**
			 * RECUPERO LE PREFERENZE PER PROGETTO
			 */
			try {
				String serverName =""; // nome server remoto
				try {
					serverName= prj.getPersistentProperty(new QualifiedName("", IProperty.SERVERNAME));
				} catch (CoreException e) {
					serverName ="";
					RsyncPlugin.consolePrintln("Error retriving server name.");
				}
				String userName =""; // nome utente
				try {
					userName= prj.getPersistentProperty(new QualifiedName("", IProperty.USERNAME));
				} catch (CoreException e) {
					userName ="";
					RsyncPlugin.consolePrintln("Error retriving username.");
				}
				String remotePath =""; // percorso remoto
				try {
					remotePath= prj.getPersistentProperty(new QualifiedName("", IProperty.REMOTEPATH));
				} catch (CoreException e) {
					remotePath ="";
					RsyncPlugin.consolePrintln("Errore retriving remote path.");
				}
				String excludePrj =""; // elenco dei file o directory da escludere
				try {
					excludePrj= prj.getPersistentProperty(new QualifiedName("", IProperty.EXCLUDE));
					
					// elimino i ritorni a capo
					excludePrj = excludePrj.replace("\n"," ");
					excludePrj = excludePrj.replace("\r\n"," ");
					excludePrj = excludePrj.replace("\r"," ");	
					
				} catch (CoreException e) {
					excludePrj ="";
					RsyncPlugin.consolePrintln("Error retriving exclude dirs.");
				}					
				
				/**
				 * AGGIUSTO IL PERCORSO REMOTO... in questo modo mi assicuro di eliminare eventuali "/" finali
				 */
				String strArray[] = remotePath.split("/");
				remotePath="";
				for (int i_idx=0; i_idx < strArray.length; i_idx++)
				{
					// RsyncPlugin.consolePrintln("/ --- "+strArray[i_idx]+"\n"); // x DEBUG
					if (!strArray[i_idx].equals("")) remotePath = remotePath.concat("/"+strArray[i_idx]);
				}
				
				/**
				 * PREPARO IL COMANDO "rsync"
				 */			
				String CMD_bash="/bin/sh";
				String rsync_cmd="";		
				String rsync_path=pathRsync+"/rsync "; // dove si trova rsync
				String login_at_machine=" "+userName+"@"+serverName+":"; // nome login e host remoto
				String localPath = prj.getLocation().toPortableString(); // path locale del progetto
				String fullExclude = excludeGlobal + " " + excludePrj + " "; // elenco completo delle esclusioni
				if (remotePath.equals("") || userName.equals("") || serverName.equals(""))
				{
					/**
					 * cavolo... non ho impostato tutte le preferenze !!
					 */
					MessageDialog.openError(RsyncPlugin.getShell(),"Error","You need to setup the project preferences for rsync.");
				} else {
					// determino il tipo di azione da eseguire...
					switch (cmdRsyncType)
					{
						case 1: // GET -- scarica il local gli aggiornamenti e sincronizza la copia locale con quella remota (che comanda)				
							rsync_cmd= rsync_path + putGet + fullExclude + login_at_machine + remotePath + "/ " +  localPath;								
							break;
						case 2: // PUT -- carica il remoto gli aggiornamenti e sincronizza la copia remota con quella locale (che comanda)
							rsync_cmd= rsync_path + putGet + fullExclude + localPath + "/ " + login_at_machine + remotePath;
							break;
						case 3: // GET solo test di sincronizzazione
							rsync_cmd= rsync_path + runDry + fullExclude + login_at_machine + remotePath + "/ " + localPath;
							break;
						case 4: // PUT solo test di sincronizzazione
							rsync_cmd= rsync_path + runDry + fullExclude + localPath + "/ " + login_at_machine + remotePath;
							break;
						case 5: // GET -- scarica in locale gli aggiornamenti remoti -- non sincronziza
							rsync_cmd= rsync_path + putGetOnly + fullExclude + login_at_machine + remotePath + "/ " + localPath;
							break;
						case 6: // PUT -- carica in remoto gli aggiornamenti locali -- non sincronizza
							rsync_cmd= rsync_path + putGetOnly + fullExclude + localPath + "/ " + login_at_machine + remotePath;
							break;					
					}
					String[] strCmd= {CMD_bash,"-c",rsync_cmd};
					//RsyncPlugin.consolePrintln(msgConsole+": "+rsync_cmd); // x DEBUG
					
					/**
					 * ESEGUO IL COMANDO "rsync"
					 */		
					try {
						// scrivo in console cosa sto facendo
						RsyncPlugin.consolePrintln("\n");
						RsyncPlugin.consolePrintln("-----------------------------------------------------------------------------");
						RsyncPlugin.consolePrintln(msgConsole+": "+rsync_cmd);
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
				        
				        RsyncPlugin.consolePrintln("\n");
				        int exitVal =pr.waitFor();
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
				MessageDialog.openError(RsyncPlugin.getShell(),"Error","Can't find the project preference for rsync");
			}											
		} else {
			MessageDialog.openError(RsyncPlugin.getShell(),"Error","No selected project - Please select one from NAVIGATOR");			
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
		return AbstractUIPlugin.imageDescriptorFromPlugin("rsync", path);
	}
}
