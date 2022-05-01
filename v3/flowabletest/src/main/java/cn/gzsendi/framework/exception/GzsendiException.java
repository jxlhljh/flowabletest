package cn.gzsendi.framework.exception;

import org.slf4j.helpers.MessageFormatter;

public class GzsendiException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;
	
    public GzsendiException(String messagePattern, Object... msgParameters) {
        super(MessageFormatter.arrayFormat(messagePattern, msgParameters).getMessage());
    }

	public GzsendiException(String message){
		super(message);
	}
	
	public GzsendiException(Throwable cause)
	{
		super(cause);
	}
	
	public GzsendiException(String message,Throwable cause)
	{
		super(message,cause);
	}

}
