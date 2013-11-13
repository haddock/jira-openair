package se.valtech.jira.openair;

public class OpenAirCommunicationException extends Exception {
	
	public OpenAirCommunicationException(Exception e) {
		this.setStackTrace(e.getStackTrace());
	}

	private static final long serialVersionUID = -2012793220588191293L;

}
