package se.valtech.jira.openair;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atlassian.crowd.embedded.api.User;
import com.atlassian.jira.component.ComponentAccessor;
import com.atlassian.jira.project.Project;
import com.atlassian.jira.user.UserPropertyManager;
import com.atlassian.sal.api.pluginsettings.PluginSettings;
import com.opensymphony.module.propertyset.PropertySet;

public class OpenAirManager {
	private static final Logger log = LoggerFactory.getLogger(OpenAirManager.class);
	private final String jiraPropertyKey = "openairid";
	private String companyId = "";
	private String userId = "";
	private String password = "";
	private static final String PLUGIN_SETTING_OPENAIR_COMPANY_ID = "se.valtech.jira.plugins.ConfigResource$Config.companyId";
	private static final String PLUGIN_SETTING_OPENAIR_USER_ID = "se.valtech.jira.plugins.ConfigResource$Config.userId";
	private static final String PLUGIN_SETTING_OPENAIR_PASSWORD = "se.valtech.jira.plugins.ConfigResource$Config.password";

	public OpenAirManager(PluginSettings pluginSettings) {
		setCompanyId((String)pluginSettings.get(PLUGIN_SETTING_OPENAIR_COMPANY_ID));
		setUserId((String)pluginSettings.get(PLUGIN_SETTING_OPENAIR_USER_ID));
		setPassword((String)pluginSettings.get(PLUGIN_SETTING_OPENAIR_PASSWORD));
	}

	public String getAssigneeOpenAirUserId(User user) throws OpenAirCommunicationException {
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
	
	public void enableTimeReportForIssue(String projectId, String assigneeUserId, String issueLabel) {
		log.info("---- project:" + projectId + " userid:" + assigneeUserId + " projecttask:" + issueLabel);
	}

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public String getUserId() {
		return userId;
	}
	
	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}

}
