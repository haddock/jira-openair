package se.valtech.jira.plugins;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import se.valtech.jira.openair.OpenAirManager;

import com.atlassian.event.api.EventListener;
import com.atlassian.event.api.EventPublisher;
import com.atlassian.jira.component.ComponentAccessor;
import com.atlassian.jira.event.issue.IssueEvent;
import com.atlassian.jira.event.type.EventType;
import com.atlassian.jira.issue.Issue;
import com.atlassian.sal.api.pluginsettings.PluginSettings;
import com.atlassian.sal.api.pluginsettings.PluginSettingsFactory;

public class IssueEventListener implements InitializingBean, DisposableBean{
	
	private static final Logger log = LoggerFactory.getLogger(IssueEventListener.class);
	private final EventPublisher eventPublisher;
	private final OpenAirManager openAirManager;
	private final PluginSettingsFactory pluginSettingsFactory;
	
	public IssueEventListener(EventPublisher eventPublisher, PluginSettingsFactory pluginSettingsFactory) {
		this.eventPublisher = eventPublisher;
		this.pluginSettingsFactory = pluginSettingsFactory;
		openAirManager = new OpenAirManager(pluginSettingsFactory.createGlobalSettings());
	}
	
	@Override
	public void destroy() throws Exception {
		eventPublisher.unregister(this);
	}
	
	@Override
	public void afterPropertiesSet() throws Exception {
		eventPublisher.register(this);
	}

	@EventListener
	public void onIssueEvent(IssueEvent issueEvent) {
		Long eventTypeId = issueEvent.getEventTypeId();		
		if (eventTypeId.equals(EventType.ISSUE_CREATED_ID) || eventTypeId.equals(EventType.ISSUE_ASSIGNED_ID)) {
			Issue issue = issueEvent.getIssue();
			try {
				String projectId = openAirManager.getOpenAirProjectId(issue.getProjectObject());
				String userId = openAirManager.getAssigneeOpenAirUserId(issue.getAssignee());
				openAirManager.enableTimeReportForIssue(projectId, userId, issue.getKey());
			} catch (Exception e) {
				log.error(e.getMessage(), e);
			}
		} 
	}
}