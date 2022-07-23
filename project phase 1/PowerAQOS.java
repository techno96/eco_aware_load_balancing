
package org.cloudbus.cloudsim.project;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.CloudletSchedulerTimeShared;
import org.cloudbus.cloudsim.Datacenter;
import org.cloudbus.cloudsim.DatacenterBroker;
import org.cloudbus.cloudsim.DatacenterCharacteristics;
import org.cloudbus.cloudsim.Host;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.Pe;
import org.cloudbus.cloudsim.Storage;
import org.cloudbus.cloudsim.UtilizationModel;
import org.cloudbus.cloudsim.UtilizationModelFull;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.VmAllocationPolicySimple;
import org.cloudbus.cloudsim.VmSchedulerSpaceShared;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.provisioners.BwProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.PeProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.RamProvisionerSimple;


public class PowerAQOS {

	private static List<Cloudlet> cloudletList;

	private static List<Vm> vmlist;
        public static int broker_id=0;
        
        
        
        public static void main(String[] args) {
        try 
        {      
                        int num_user = 1;   // number of cloud users
	            	Calendar calendar = Calendar.getInstance();
	            	boolean trace_flag = true;  // mean trace events
                        
                        CloudSim.init(num_user, calendar, trace_flag);
                        
                        int m=10;
                        double tot_power=0.0;
                        Datacenter datacenter[]=new Datacenter[m];
                        
                                              
                        double solar[]=new double[m];
                        double wind[]=new double[m];
                        double grid[]=new double[m];
  
                        int dtdis[]=new int[m];
                        
                        double datacenter_power_cost[]=new double[m];
                        Scanner sc=new Scanner(new FileInputStream("F:\\fyp\\Final Year Proj\\Dataset\\Grid Power.txt"));
                        
                        Scanner sc1=new Scanner(new FileInputStream("F:\\fyp\\Final Year Proj\\Dataset\\Cost of Power.txt"));
                        Random r1=new Random();
                        
                        for(int i=0;i<m;i++)
                        {
                            String g="F:\\fyp\\Final Year Proj\\Dataset\\";
                         String x="Wind"+Integer.toString(i)+".txt";
                         String b=g;
                         g=g+x;
                         
                        Scanner sc3=new Scanner(new FileInputStream(g));
                        x="Solar"+Integer.toString(i)+".txt";
                        b=b+x;
                        Scanner sc4=new Scanner(new FileInputStream(b));
                        
                            datacenter[i]=createDatacenter(Integer.toString(i));
                            String a=sc.next();
                            a=sc.next();
                            a=sc.next();
                            a=sc.next();
                            
                            grid[i]=Double.parseDouble(sc.next());
                            datacenter_power_cost[i]=Double.parseDouble(sc1.next());
                            a=sc3.next();
                            a=sc3.next();
                            tot_power=tot_power+Double.parseDouble(a);
                            a=sc3.next();
                            tot_power=tot_power+Double.parseDouble(a);
                            a=sc3.next();
                            tot_power=tot_power+Double.parseDouble(a);
                            a=sc3.next();
                            tot_power=tot_power+Double.parseDouble(a);
                            a=sc3.next();
                            tot_power=tot_power+Double.parseDouble(a);
                            tot_power=tot_power/5;
                            wind[i]=0.40*0.5*3.14*45*45*1.225*tot_power*tot_power*tot_power*200*0.67/(1000*3600);
                            a=sc4.next();
                            solar[i]=Double.parseDouble(sc4.next())*200*0.70*100/(1000*3600);
                            dtdis[i]=r1.nextInt(2000)*1000;
                        }
                        
                        
                        
                        DatacenterBroker broker = createBroker(broker_id);
	            	int brokerId = broker.getId();
                        
                        Vm vm[]=new Vm[500];
                        
                        
	            	int mips = 250;
	            	long size = 10000; //image size (MB)
	            	int ram = 512; //vm memory (MB)
	            	long bw = 1000;
	            	int pesNumber = 1; //number of cpus
	            	String vmm = "Xen"; //VMM name
                        vmlist=new ArrayList<Vm>();

                        for(int i=0;i<m;i++)
                        {   
	            	vm[i]= new Vm(i, brokerId, mips, pesNumber, ram, bw, size, vmm, new CloudletSchedulerTimeShared());
                        vmlist.add(vm[i]);
                        }
                        broker.submitVmList(vmlist);
                        
                        
                        Scanner sc2=new Scanner(new FileInputStream("F:\\fyp\\Final Year Proj\\Dataset\\Trace.txt"));
                        sc2.useDelimiter("\t");
                        Cloudlet cloudlet[]=new Cloudlet[500];
                        
                        
	            	int pesNo=1;
	            	long fileSize = 300;
	            	long outputSize = 300;
	            	UtilizationModel utilizationModel = new UtilizationModelFull();
                        
                        cloudletList=new ArrayList<Cloudlet>();
                       
                        for(int i=0;i<500;i++)
                        {
                        String a=sc2.next();
                        String b=sc2.next();
                        String c=sc2.next();
                        c=sc2.next();
                        c=sc2.next();
                        c=sc2.next();
                        c=sc2.next();
                        c=sc2.next();
                        c=sc2.next();
                                
	            	cloudlet[i]= new Cloudlet(i, Integer.parseInt(c), pesNo, fileSize, outputSize, utilizationModel, utilizationModel, utilizationModel,Double.parseDouble(b));
	            	cloudlet[i].setUserId(brokerId);
                        
                        if(i<=100)
                            cloudlet[i].setVmId(1);
                        cloudletList.add(cloudlet[i]);
                        c=sc2.next();
                        c=sc2.next();
                        c=sc2.next();
                        c=sc2.next();
                        c=sc2.next();
                        c=sc2.next();
                        c=sc2.next();
                        c=sc2.next();
                        c=sc2.next();
                        
                        }
                        
                        broker.submitCloudletList(cloudletList);
                        
                        broker.scheduleEventforNextSlot();
                        CloudSim.startSimulation();
	            	
	            	CloudSim.stopSimulation();
                        // Final step: Print results when simulation is over
	            	List<Cloudlet> newList = broker.getCloudletReceivedList();


	            	printCloudletList(newList,datacenter_power_cost,solar,wind,grid,m);
                        
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        
    }
    private static Datacenter createDatacenter(String name){

		List<Host> hostList = new ArrayList<Host>();
		List<Pe> peList = new ArrayList<Pe>();

		int mips=1000;

		peList.add(new Pe(0, new PeProvisionerSimple(mips))); // need to store Pe id and MIPS Rating

                int hostId=0;
		int ram = 2048; //host memory (MB)
		long storage = 1000000; //host storage
		int bw = 10000;

                for(int i=0;i<20;i++)
                {
		hostList.add(
    			new Host(
    				hostId,
    				new RamProvisionerSimple(ram),
    				new BwProvisionerSimple(bw),
    				storage,
    				peList,
    				new VmSchedulerSpaceShared(peList)
    			)
    		); // This is our first machine
                }

		String arch = "x86";     
		String os = "Linux";     
		String vmm = "Xen";
		double time_zone = 10.0; 	
                double cost = 3.0;              
		double costPerMem = 0.05;	
		double costPerStorage = 0.001;	
                double costPerBw = 0.0;			// the cost of using bw in this resource
		LinkedList<Storage> storageList = new LinkedList<Storage>();	//we are not adding SAN devices by now

		DatacenterCharacteristics characteristics = new DatacenterCharacteristics(
                arch, os, vmm, hostList, time_zone, cost, costPerMem, costPerStorage, costPerBw);


		Datacenter datacenter = null;
		try {
			datacenter = new Datacenter(name, characteristics, new VmAllocationPolicySimple(hostList), storageList, 0);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return datacenter;
	}

	private static DatacenterBroker createBroker(int id){

		DatacenterBroker broker = null;
		try {
			broker = new DatacenterBroker("Broker"+id);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return broker;
	}private static void printCloudletList(List<Cloudlet> list,double cost[],double solar[],double wind[],double grid[],int m) {
	        int size = list.size();
	        Cloudlet cloudlet;
	        Log.printLine();
	        Log.printLine("========== OUTPUT ==========");
	        Log.printLine("Cloudlet ID" + "\t" + "STATUS" + "\t\t" +
	                "Data center ID" + "\t" + "Submit Time\tWait Time\tExec Time" + "\t" + "Start Time" + "\t" + "Finish Time\tEnergy Type\tTot.Energy  Cost\n");
try
{
                FileOutputStream fs=new FileOutputStream(new File("F:\\fyp\\xx\\BFOA.txt"));
       
                double renewable[]=new double[m];
                for(int i=0;i<m;i++)
                {
                    renewable[i]=solar[i]+wind[i];
                }
	        DecimalFormat dft = new DecimalFormat("###.##");
	        for (int i = 0; i < size; i++) {
	            cloudlet = list.get(i);
	            Log.print(cloudlet.getCloudletId() + "\t\t");
                    
                    int x=cloudlet.getCloudletId();
                    double time=cloudlet.getActualCPUTime();
                    double power_consumed=time*0.1020833/1000;
                    double cloudlet_cost=0.0;
                    if(renewable[cloudlet.getResourceId()-2]>power_consumed)
                    {
                        cloudlet_cost=0.0;
                        renewable[cloudlet.getResourceId()-2]-=power_consumed;
                    } 
                    else
                    {
                        cloudlet_cost=power_consumed*cost[cloudlet.getResourceId()-2];
                        renewable[cloudlet.getResourceId()-2]-=power_consumed;
                    }

	            

	            	Log.print( "SUCCESS\t\t\t" + /*cloudlet_datacenter[cloudlet.getCloudletId()]*/(cloudlet.getAllResourceId().length) + "\t\t"+ dft.format(cloudlet.getSubmissionTime())+"\t\t"+dft.format(cloudlet.getFinishTime()-cloudlet.getSubmissionTime()-cloudlet.getActualCPUTime())+"\t\t"+dft.format(cloudlet.getActualCPUTime()) + "\t\t"+ dft.format(cloudlet.getExecStartTime())+
                             "\t\t"+ dft.format(cloudlet.getFinishTime())+"\t\t");
                        
                        if(cloudlet_cost==0.0)
                        {
                               Log.print("RB\t");
                        }
                        else
                            Log.print("GRID\t");
                        
                        Log.print(dft.format(power_consumed)+"\t\t"+dft.format(cloudlet_cost));
                        Log.print("\n");
                    fs.write(Integer.toString(x).getBytes());
                    String a="\t";
                    fs.write(a.getBytes());
                    fs.write(dft.format(cloudlet.getSubmissionTime()).getBytes());
                    a="\t";
                    fs.write(a.getBytes());
                    fs.write(dft.format(cloudlet.getExecStartTime()).getBytes());
                    a="\t";
                    fs.write(a.getBytes());
                    fs.write(dft.format(cloudlet.getFinishTime()).getBytes());
                    a="\t";
                    fs.write(a.getBytes());
                    fs.write(dft.format(power_consumed).getBytes());
                    a="\t";
                    fs.write(a.getBytes());
                    fs.write(dft.format(cloudlet_cost).getBytes());
                    a="\r\n";
                    fs.write(a.getBytes());
                    
                    
                        
	            }
	        }
catch(Exception e)
{
    
}
        }
    
        }
   
