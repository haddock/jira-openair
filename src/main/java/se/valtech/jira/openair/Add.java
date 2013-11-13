package se.valtech.jira.openair;

import com.atlassian.jira.issue.Issue;

public class Add extends Action {
	
	final String type = "Task";

	public Add(Issue issue) {
		task = new Task(issue);
	}
}
