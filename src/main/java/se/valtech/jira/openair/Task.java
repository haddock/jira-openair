package se.valtech.jira.openair;

import com.atlassian.jira.issue.Issue;

public class Task {
	String id;
	String created;
	String updated;
	String projecttask_typeid;
	String userid;
	String date;
	String decimal_hours;
	String cost_centerid;
	String slipid;
	String hours;
	String timetypeid;
	String minutes;
	String projectid;
	String description;
	String categoryid;
	String projecttaskid;
	String timesheetid;
	String notes;
	String customerid;
	String payroll_typeid;
	String job_codeid;
	String loaded_cost;
	String loaded_cost_2;
	String loaded_cost_3;
	String project_loaded_cost;
	String acct_date;
	String category_1id;
	String category_2id;
	String category_3id;
	String category_4id;
	String cateogry_5id;
	String thin_client_id;
	
	public Task(Issue issue) {
		this.projectid = issue.getProjectObject().getDescription();
		this.projecttask_typeid = "1";
		this.description = issue.getKey();
		this.userid = issue.getAssigneeId();
		this.created = issue.getCreated().toString();
		this.updated = issue.getUpdated().toString();
	}
}


