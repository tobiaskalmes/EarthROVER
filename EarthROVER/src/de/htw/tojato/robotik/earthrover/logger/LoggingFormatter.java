/**
 * 
 */
package de.htw.tojato.robotik.earthrover.logger;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

/**
 * @author Tobias Kalmes
 * 
 */
public class LoggingFormatter extends Formatter
{
	public LoggingFormatter()
	{
		super();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.logging.Formatter#format(java.util.logging.LogRecord)
	 */
	@Override
	public String format(LogRecord record)
	{
		StringBuffer sb = new StringBuffer();
		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy - HH:mm:ss : ");
		sb.append(sdf.format(new Date()));
		sb.append(record.getLoggerName()).append(" : ");
		sb.append(record.getLevel().getName()).append(" : ");
		sb.append(formatMessage(record)).append("\r\n");
		Throwable exception = record.getThrown();
		if (exception != null)
		{
			sb.append("\t");
			sb.append(exception.getClass().getCanonicalName()).append(": ");
			sb.append(exception.getLocalizedMessage()).append("\r\n");
			for (StackTraceElement element : exception.getStackTrace())
			{
				sb.append("\t\tat ").append(element.getClassName()).append("(");
				sb.append(element.getFileName()).append(":");
				sb.append(element.getLineNumber()).append(")\r\n");
			}
		}

		return sb.toString();
	}
}
