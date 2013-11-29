package rsync.preferences;

/**
 * Constant definitions for plug-in preferences
 */
public class PreferenceConstants {

	public static final String P_PATH_RSYNC = "pathRsync";
	public static final String P_PAR_RSYNC_PUTGET = "paramRsyncPutGet";	 // sincronizzazione GET e PUT (locale-remoto)
	public static final String P_PAR_RSYNC_RUNDRY = "paramRsyncRunDry"; // simulazione di sincronizzazione (locale-remoto)
	public static final String P_PAR_RSYNC_PUTGET_ONLY = "paramRsyncPutOnly"; // carica solo gli aggiornamenti locali in remoto
	public static final String P_PAR_RSYNC_EXCLUSIONS= "paramRsyncExclude"; // elenco delle directory o file da escludere in modo generico

}
