package JenkinsAuto.HealthMonitor;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Iterator;
import java.util.Map;

import com.offbytwo.jenkins.JenkinsServer;
import com.offbytwo.jenkins.model.Build;
import com.offbytwo.jenkins.model.Computer;
import com.offbytwo.jenkins.model.Job;
import com.offbytwo.jenkins.model.JobWithDetails;
import com.offbytwo.jenkins.model.View;

/**
 *This class tries to get server details along with builds configured.
 *
 */
public class JenkinsDetails 
{
	private JenkinsServer server=null;
	
	public JenkinsDetails() {
		try {
			System.out.println("*******************************");
			System.out.println("Connecting to the Server.....");
			server = new JenkinsServer(new URI("http://vdvs092:8080/"));
			System.out.println("Connected to the Server vdvs092");
			
			System.out.println("*******************************");
			System.out.println("");
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			System.out.println("Error in connecting to the Server vdvs092.");
			e.printStackTrace();
			
		}
	}
	public Map<String, Job> getAllJenkinsJob(){
		Map<String, Job> jobs = null;
		try {
				jobs=server.getJobs();
				//System.out.println("There are in total "+jobs.size()+" jobs listed on this server.");
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	
		return jobs;
	}
	
	public String getJenkinsVersion(){
		return server.getVersion().getLiteralVersion();
		
	}
	
	public Map<String, View> getAllViews() throws IOException{
		return server.getViews();
	}
	
	public boolean isServerUp(){
		return server.isRunning();
		
	}
public void getExecuters() throws IOException{
	System.out.println("Executers on this server are: ");
	for(Map.Entry<String, Computer>e:server.getComputers().entrySet()){
		System.out.println(e.getKey());
	}
}
	
	public JobWithDetails getJobDetails(String jobName) {
		try {
			return server.getJob(jobName);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
 
}
