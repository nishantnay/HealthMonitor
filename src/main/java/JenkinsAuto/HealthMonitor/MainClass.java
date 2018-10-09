package JenkinsAuto.HealthMonitor;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.offbytwo.jenkins.model.Build;
import com.offbytwo.jenkins.model.Job;
import com.offbytwo.jenkins.model.JobWithDetails;
import com.offbytwo.jenkins.model.View;

public class MainClass {

	private final static String jobName="Delphi.fdc_R31ci";//"SeedDataUtility-v4.2-_MasterThree-02-Extract";

	public void showJobDetails(String jobName, JenkinsDetails jd) throws IOException {
		List<Build>allRuns=null;
		Map<String, Integer>sucMap=null;
	
		JobWithDetails jobDetails= jd.getJobDetails(jobName);



		System.out.println("Here are some of the details for "+jobName+" :");



		//System.out.println(jobDetails.details().getLastStableBuild().details().getChangeSet().getKind());
		{
		System.out.println("Is the current Job scheduled? In Queue? :"+jobDetails.isInQueue());
		System.out.print("Its last failed build ID: ");
		String lastUnc=jobDetails.getLastFailedBuild().details().getDisplayName();
		System.out.println((lastUnc==null)? "No build has failed since inception.":lastUnc);
		System.out.print("Its last succesfull build ID: ");
		System.out.println(jobDetails.getLastSuccessfulBuild().details().getDisplayName());
		System.out.print("Its last unsuccesfull build ID: ");
		System.out.println(jobDetails.getLastUnsuccessfulBuild().details().getDisplayName());
		}
		

		allRuns = jobDetails.getAllBuilds();

		sucMap=getSuccessMap(allRuns);


		System.out.println("Total runs for this job so far:  "+allRuns.size());
		System.out.println("Out of "+allRuns.size()+" runs, ");
		for(Map.Entry<String,Integer> mE: sucMap.entrySet()){
			System.out.println(mE.getValue()+" were "+mE.getKey()+".");
		}
		System.out.println("There by making a "+getSuccessRate(sucMap)+"% of success rate ignoring ABORTED states.");
	}



	private String getSuccessRate(Map<String, Integer> sucMap) {

		String toRemove="ABORTED";
		sucMap.remove(toRemove);
		int sucVal=sucMap.get("SUCCESS");
		int failVal=sucMap.get("FAILURE");
		DecimalFormat df= new DecimalFormat("##.##");
		return df.format(((double)sucVal/(sucVal+failVal))*100 );
	}



	private Map<String, Integer> getSuccessMap(List<Build> allRuns) throws IOException {
		Iterator<Build> bIte=allRuns.iterator();
		Map<String, Integer> successMap= new HashMap<>();
		String tempName="";
		while(bIte.hasNext()){
			tempName=bIte.next().details().getResult().name();
			if(successMap.containsKey(tempName))
				successMap.put(tempName,successMap.get(tempName)+1);
			else successMap.put(tempName,1);

		}
		return successMap;
	}



	public static void main(String[] args) throws IOException {

		JenkinsDetails jd= new JenkinsDetails();// mAIN tHREAD;

		System.out.println(jd.isServerUp()? "The server is Up and running with version "+jd.getJenkinsVersion():"Server is down.");
		jd.getExecuters();
		System.out.println("*********************");
		Map<String, View> getAllViews= jd.getAllViews();
		//


		MainClass mClass= new MainClass();
		Thread t1= new Thread(()-> 
		{
			try {
				System.out.println("*********************");
				mClass.showJobDetails(jobName, jd);
				System.out.println("*********************");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		});
		
		Thread t2= new Thread(()-> 
		{
			System.out.println("*********************");
			Map<String, Job> allJobs=jd.getAllJenkinsJob();
			System.out.println("*********************");

		});
		
		t1.start();
		t2.start();
		//System.out.println("There are "+allJobs.size()+" jobs and "+getAllViews.size()+" views created on this Jenkins server.");


	}

}
