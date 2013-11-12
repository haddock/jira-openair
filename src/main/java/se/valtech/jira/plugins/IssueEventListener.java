package se.valtech.jira.plugins;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import se.valtech.jira.openair.Task;

import com.atlassian.event.api.EventListener;
import com.atlassian.event.api.EventPublisher;
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
	public void onIssueEvent(IssueEvent issueEvent) {
		Long eventTypeId = issueEvent.getEventTypeId();
		Issue issue = issueEvent.getIssue();
		
		log.info(new Task(issue).asXML());
		
		log.info("Issue event ID: {} project: {}", issueEvent.getEventTypeId(), issue.getProjectObject().getDescription());
		if (eventTypeId.equals(EventType.ISSUE_CREATED_ID)) {
			log.info("Issue {} has been created by {}.", issue.getKey(), issue.getReporter().getDisplayName());
		} else if (eventTypeId.equals(EventType.ISSUE_ASSIGNED_ID)) {
			log.info("Issue {} has been assigned to {}.", issue.getKey(), issue.getAssignee().getDisplayName());
		} else if (eventTypeId.equals(EventType.ISSUE_DELETED_ID)) {
			log.info("Issue {} has been deleted.", issue.getKey());
		}
	}
}