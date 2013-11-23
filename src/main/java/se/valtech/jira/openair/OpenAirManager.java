package se.valtech.jira.openair;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atlassian.crowd.embedded.api.User;
import com.atlassian.jira.component.ComponentAccessor;
import com.atlassian.jira.project.Project;
import com.atlassian.jira.user.UserPropertyManager;
import com.opensymphony.module.propertyset.PropertySet;

public class OpenAirManager {
	private static final Logger log = LoggerFactory.getLogger(OpenAirManager.class);
	private final String jiraPropertyKey = "openairid";

	public String getOpenAirUserId(User user) throws OpenAirCommunicationException {
		UserPropertyManager propertyManager = ComponentAccessor.getUserPropertyManager();
		PropertySet properties = propertyManager.getPropertySet(user);
		String key = "jira.meta." + jiraPropertyKey;
		if (properties == null || !properties.exists(key)) {
			throw new OpenAirCommunicationException("User " + user.getDisplayName() + " does not have an openairid property.");
		}
		return properties.getString(key);
	}
	
	public String getOpenAirProjectId(Project project) throws OpenAirCommunicationException {		
		String id = "";
		try {
			String description = project.getDescription();
			String key = "|" + jiraPropertyKey + ":";
			int offset = description.indexOf(key) + key.length();
			int stop = description.indexOf("|", offset);
			id = description.substring(offset, stop);
		} catch(Exception e) {
			throw new OpenAirCommunicationException("Project " + project.getName() + " do not have an openairid set in the description."); 
		}
		return id;
	}

	public void enableTimeReportForIssue(String projectId, String userId, String issueLabel) {
		log.info("---- project:" + projectId + " userid:" + userId + " projecttask:" + issueLabel);
	}

}
