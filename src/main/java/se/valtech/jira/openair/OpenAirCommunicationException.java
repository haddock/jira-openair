package se.valtech.jira.openair;

public class OpenAirCommunicationException extends Exception {
	
	private static final long serialVersionUID = -2012793220588191293L;
	
	public OpenAirCommunicationException() {
		super();
	}
	
	public OpenAirCommunicationException(String msg) {
		super(msg);
	}
	
	public OpenAirCommunicationException(Exception e) {
		super(e);
	}


}
