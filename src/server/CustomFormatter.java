package server;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class CustomFormatter extends Formatter
{
	private SimpleDateFormat logTimeFormat;
	
	public CustomFormatter()
	{
		logTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
	}
	
	public String format( LogRecord record )
	{
		// Objet pour assembler des chaînes de caractères
		StringBuffer sb = new StringBuffer("");
		
		// Ajout de la date au log
		sb.append( logTimeFormat.format( record.getMillis() ) );
		
		// Ajout de la classe et de la méthode qui a demandé le log
		// sb.append( "; " );
		// sb.append( record.getSourceClassName() );
		// sb.append( "." );
		// sb.append( record.getSourceMethodName() );
		// sb.append( "(); " );
		
		// Ajout du niveau de sévérité de l'événement
		sb.append( " " );
		sb.append( record.getLevel().getName() );
		
		// Ajout du message de l'événement
		sb.append( " " );
		sb.append( record.getMessage() );
		
		sb.append("\r\n");
		
		return sb.toString();
	}
}
