package se.valtech.jira.openair;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.atlassian.jira.project.Project;
import com.atlassian.sal.api.pluginsettings.PluginSettings;

public class OpenAirManagerUnitTest {
	
	@Rule
	public ExpectedException exception = ExpectedException.none();
	
	private OpenAirManager openAirManager;
	private Project project;
	
	@Before
	public void initialize() {
		openAirManager = new OpenAirManager(mock(PluginSettings.class));
		project = mock(Project.class);
	}
	
	@Test
	public void testGetOpenAirIdFromJiraProject() throws OpenAirCommunicationException {
		when(project.getDescription()).thenReturn("Dummy description <!-- |openairid:520| -->");
		String expectedId = "520";
		String actualId = openAirManager.getOpenAirProjectId(project);
		assertEquals(expectedId, actualId);
	}
	
	@Test
	public void noOpenAirProjectIdOnJiraShouldThrowException() throws OpenAirCommunicationException {
		when(project.getDescription()).thenReturn("Dummy description");
		exception.expect(OpenAirCommunicationException.class);
		openAirManager.getOpenAirProjectId(project);
	}
}
