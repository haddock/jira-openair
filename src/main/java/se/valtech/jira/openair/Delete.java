package se.valtech.jira.openair;

import com.atlassian.jira.issue.Issue;

public class Delete extends Action {
	
	final String type = "Task";

	public Delete(Issue issue) {
		task = new Task(issue);
	}
	
}
