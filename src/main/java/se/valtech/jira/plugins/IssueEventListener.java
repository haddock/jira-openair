package se.valtech.jira.plugins;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import se.valtech.jira.openair.Action;
import se.valtech.jira.openair.Add;
import se.valtech.jira.openair.Delete;
import se.valtech.jira.openair.OpenAirCommunicationException;

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
	public void onIssueEvent(IssueEvent issueEvent) throws OpenAirCommunicationException {
		Long eventTypeId = issueEvent.getEventTypeId();
		Issue issue = issueEvent.getIssue();
		
		if (eventTypeId.equals(EventType.ISSUE_CREATED_ID) || eventTypeId.equals(EventType.ISSUE_ASSIGNED_ID)) {
			sendToOpenAir(new Add(issue));
		} else if (eventTypeId.equals(EventType.ISSUE_DELETED_ID)) {
			sendToOpenAir(new Delete(issue));
		}
	}

	private void sendToOpenAir(Action action) throws OpenAirCommunicationException {
		HttpClient client = new HttpClient();
		HttpClientParams params = new HttpClientParams();
		params.setConnectionManagerTimeout(1000);
		params.setSoTimeout(1000);
		client.setParams(params);
		PostMethod post = new PostMethod("http://httpbin.org/post");
		post.setRequestHeader("Content-Language", "en-US");
		log.info(action.asXML());
		try {
			post.setRequestEntity(new StringRequestEntity(action.asXML(), "text/xml", "UTF-8"));
			int responseCode = client.executeMethod(post);
			if (responseCode >= 300) {
			}
		} catch (Exception e) {
			throw new OpenAirCommunicationException(e);
		}
	}
}