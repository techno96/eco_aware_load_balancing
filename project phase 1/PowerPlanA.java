
package org.cloudbus.cloudsim.project;

import java.io.FileInputStream;
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

class DatacenterPower
{    
    double solar;
    double wind;
    double grid;
    DatacenterPower()
    {
        solar=0.0;
        wind=0.0;
        grid=0.0;
    }
};
public class PowerPlanA {

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
                        
                        //DatacenterPower datacenter_power[]=new DatacenterPower[m];
                        
                        final double solar[]=new double[m];
                        final double wind[]=new double[m];
                        final double grid[]=new double[m];
                        
                        final double datacenter_power_cost[]=new double[m];
                        
                        Scanner sc=new Scanner(new FileInputStream("F:\\Final Year Proj\\Dataset\\Grid Power.txt"));
                        Scanner sc1=new Scanner(new FileInputStream("F:\\Final Year Proj\\Dataset\\Cost of Power.txt"));
                        Scanner sc3=new Scanner(new FileInputStream("F:\\Final Year Proj\\Dataset\\Wind.txt"));
                        
                        Scanner sc4=new Scanner(new FileInputStream("F:\\Final Year Proj\\Dataset\\Solar.txt"));
                        
                        for(int i=0;i<m;i++)
                        {
                            datacenter[i]=createDatacenter(Integer.toString(i));
                            String a=sc.next();
                            a=sc.next();
                            a=sc.next();
                            a=sc.next();
                            a=sc.next();
                            
                            grid[i]=Double.parseDouble(a);
                            
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
                            wind[i]=tot_power*0.5*tot_power*tot_power*200*0.40*1.225*3.14*45*45*1609.34*1609.34*1609.34/(1000*1000*1000*100);
                            a=sc4.next();
                            solar[i]=Double.parseDouble(sc4.next())*200*0.40*100/1000;
                            System.out.println(solar[i]+"\t"+wind[i]+"\t"+grid[i]);
                        }
                        
                        
                        
                        final DatacenterBroker broker = createBroker(broker_id);
	            	int brokerId = broker.getId();
                        
                        Vm vm[]=new Vm[10];
                        
                        
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
                        
                        
                        
                        Scanner sc2=new Scanner(new FileInputStream("F:\\Final Year Proj\\Dataset\\Trace.txt"));
                        sc2.useDelimiter("\t");
                        Cloudlet cloudlet[]=new Cloudlet[70];
                        
                        
	            	int pesNo=1;
	            	long fileSize = 300;
	            	long outputSize = 300;
	            	UtilizationModel utilizationModel = new UtilizationModelFull();
                        Random r=new Random();
                        cloudletList=new ArrayList<Cloudlet>();

                        for(int i=0;i<70;i++)
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
                        
                        Runnable monitor = new Runnable() {
				@Override
				public void run() {
					CloudSim.pauseSimulation(200);
					while (true) {
						if (CloudSim.isPaused()) {
							break;
						}
						try {
							Thread.sleep(100);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}

					Log.printLine("\n\n\n" + CloudSim.clock() + ": The simulation is paused for 5 sec \n\n");

					try {
						Thread.sleep(5000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
                                        
                                        printCloudletList(broker.getCloudletReceivedList(),datacenter_power_cost,solar,wind,grid);
					CloudSim.resumeSimulation();
				}
			};

			new Thread(monitor).start();
			Thread.sleep(100);
  
	            	CloudSim.startSimulation();
	            	
	            	CloudSim.stopSimulation();
                        // Final step: Print results when simulation is over
	            	List<Cloudlet> newList = broker.getCloudletReceivedList();


	            	printCloudletList(newList,datacenter_power_cost,solar,wind,grid);
                        
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
	}
        private static void printCloudletList(List<Cloudlet> list,double cost[],double solar[],double wind[],double grid[]) {
	        int size = list.size();
	        Cloudlet cloudlet;

	        
	        Log.printLine();
	        Log.printLine("========== OUTPUT ==========");
	        Log.printLine("Cloudlet ID" + "\t" + "STATUS" + "\t\t" +
	                "Data center ID" + "\t\t" + "VM ID" + "\t\t" + "Time" + "\t\t" + "Start Time" + "\t\t" + "Finish Time");

	        DecimalFormat dft = new DecimalFormat("###.##");
	        for (int i = 0; i < size; i++) {
	            cloudlet = list.get(i);
	            Log.print(cloudlet.getCloudletId() + "\t\t");

	            if (cloudlet.getCloudletStatus() == Cloudlet.SUCCESS){
	                Log.print("SUCCESS");

	            	Log.printLine( "\t\t\t" + cloudlet.getResourceId() + "\t\t"+ cloudlet.getVmId() +
	                     "\t\t"+ dft.format(cloudlet.getActualCPUTime()) + "\t\t"+ dft.format(cloudlet.getExecStartTime())+
                             "\t\t"+ dft.format(cloudlet.getFinishTime()));
	            }
	        }
        }
    
}
