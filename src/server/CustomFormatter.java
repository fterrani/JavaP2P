package server;

import java.text.SimpleDateFormat;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class CustomFormatter extends Formatter
{
	private static SimpleDateFormat logTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
	
	public CustomFormatter()
	{
		super();
	}
	
	public String format( LogRecord record )
	{
		StringBuffer sb = new StringBuffer("");
		
		// Appends datetime
		sb.append( logTimeFormat.format( record.getMillis() ) );
		
		// Appends class and method names
		// sb.append( "; " );
		// sb.append( record.getSourceClassName() );
		// sb.append( "." );
		// sb.append( record.getSourceMethodName() );
		// sb.append( "(); " );
		
		// Appends log level
		sb.append( " " );
		sb.append( record.getLevel().getName() );
		
		// Appends message
		sb.append( " " );
		sb.append( record.getMessage() );
		
		sb.append("\r\n");
		
		return sb.toString();
	}
}
