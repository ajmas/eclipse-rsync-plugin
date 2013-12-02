package rsync.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.MessageConsole;
import org.eclipse.ui.console.MessageConsoleStream;
/*
import org.eclipse.ui.console.IConsoleConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.text.IDocument;

import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
*/


public class ConsoleDisplayMgr
{
	private static ConsoleDisplayMgr Console = null;
	private MessageConsole m_MessageConsole = null;
	private String m_Title = null;	
	
		
	protected ConsoleDisplayMgr(String messageTitle){		
		m_Title = messageTitle;
		
		m_MessageConsole = new MessageConsole(m_Title, null); 
		m_MessageConsole.setTabWidth(4);
		IConsole[] c = new IConsole[]{ m_MessageConsole };
		ConsolePlugin.getDefault().getConsoleManager().addConsoles(c);		
	}

	public static ConsoleDisplayMgr getDefault(String consoleTitle){
		if(Console == null) Console = new ConsoleDisplayMgr(consoleTitle);
		
		return Console;
	}	
		
	public void println(String msg){		
		if( msg == null ) return;

		int swtColorId = SWT.COLOR_DARK_GREEN;
		
		MessageConsoleStream msgConsoleStream = m_MessageConsole.newMessageStream();	
		
		if (msgConsoleStream == null) {
			System.out.print(Messages.MESG_ERROR_GETTING_CONSOLE_STREAM);
		}else{
			msgConsoleStream.setColor(Display.getCurrent().getSystemColor(swtColorId));
		}
		
		/* display message on console */	
		msgConsoleStream.println(msg);
	}

}