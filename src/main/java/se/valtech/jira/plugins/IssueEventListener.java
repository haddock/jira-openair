package se.valtech.jira.plugins;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import se.valtech.jira.openair.OpenAirCommunicationException;

import com.atlassian.crowd.embedded.api.User;
import com.atlassian.event.api.EventListener;
import com.atlassian.event.api.EventPublisher;
import com.atlassian.jira.component.ComponentAccessor;
import com.atlassian.jira.event.issue.IssueEvent;
import com.atlassian.jira.event.type.EventType;
import com.atlassian.jira.issue.Issue;

public class IssueEventListener implements InitializingBean, DisposableBean{

	private static final Logger log = LoggerFactory.getLogger(IssueEventListener.class);
	private final EventPublisher eventPublisher;
	
	public IssueEventListener(EventPublisher eventPublisher) {
		this.eventPublisher = eventPublisher;
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
	public void onIssueEvent(IssueEvent issueEvent) throws OpenAirCommunicationException {
		Long eventTypeId = issueEvent.getEventTypeId();
		Issue issue = issueEvent.getIssue();
		
		if (eventTypeId.equals(EventType.ISSUE_CREATED_ID) || eventTypeId.equals(EventType.ISSUE_ASSIGNED_ID)) {
			String description = issue.getProjectObject().getDescription();
			int offset = description.indexOf("|openairid:") + "|openairid:".length();
			int stop = description.indexOf("|", offset);
			String openairprojectid = description.substring(offset, stop);
			log.info("project id: " + openairprojectid);
			User user = issue.getAssignee();
			String openairuserid = ComponentAccessor.getUserPropertyManager().getPropertySet(user).getString("jira.meta.openairid");
			log.info("user id: " + openairuserid);
		} 
	}
}