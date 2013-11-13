package se.valtech.jira.openair;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.StaxDriver;

public abstract class Action {
	
	Task task;
	
	public String asXML() {
		XStream xstream = new XStream(new StaxDriver());
		xstream.aliasPackage("", "se.valtech.jira.openair");
		xstream.useAttributeFor(this.getClass(), "type");
		return xstream.toXML(this);
	}
}
