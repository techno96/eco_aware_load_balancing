/*
 * Title:        CloudSim Toolkit
 * Description:  CloudSim (Cloud Simulation) Toolkit for Modeling and Simulation
 *               of Clouds
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2009, The University of Melbourne, Australia
 */

package org.cloudbus.cloudsim.project;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

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


public class CloudQoSScheduling {
    
        static int m=10,n=20;
        private static List<Cloudlet> CloudletList;
               
        private static List<Vm> vmList;
        private static List<List<Vm>> ListofvmList;
        
	public static void main(String[] args) {
		try {
                    
        		int num_user = 2;   // number of cloud users
			Calendar calendar = Calendar.getInstance();
			boolean trace_flag = false;  // mean trace events

			// Initialize the CloudSim library
			CloudSim.init(num_user, calendar, trace_flag);

        List<Cloudlet> cList1=new ArrayList<Cloudlet>();
        List<Cloudlet> cList2=new ArrayList<Cloudlet>();
        List<Cloudlet> cList3=new ArrayList<Cloudlet>();
        List<Cloudlet> cList4=new ArrayList<Cloudlet>();
        List<Cloudlet> cList5=new ArrayList<Cloudlet>();
        List<Cloudlet> cList6=new ArrayList<Cloudlet>();
        List<Cloudlet> cList7=new ArrayList<Cloudlet>();
        List<Cloudlet> cList8=new ArrayList<Cloudlet>();
        List<Cloudlet> cList9=new ArrayList<Cloudlet>();
        List<Cloudlet> cList10=new ArrayList<Cloudlet>();
                           
                        Datacenter d[]=new Datacenter[m];
                        int  dtdis[]=new int[m];
                        Random r=new Random();
                        for(int i=0;i<m;i++)
                        {
                            String x=Integer.toString(i);
                            d[i]=createDatacenter(x);
                            
                            dtdis[i]=r.nextInt(2000)*1000;
                            
                        }
                        
                        int vmid = 0;
			int mips = 250;
			long size = 10000; //image size (MB)
			int ram = 512; //vm memory (MB)
			long bw = 1000;
			int pesNumber = 1; //number of cpus
			String vmm = "Xen"; //VMM name

                        //Cloudlet properties
			int id = 0;
			long length = 4000;
			long fileSize = 300;
			long outputSize = 300;
			UtilizationModel utilizationModel = new UtilizationModelFull();

                        int broker[]=new int[m];
                        DatacenterBroker dcb[]=new DatacenterBroker[m];
                        
                        for(int i=0;i<m;i++)
                        {
                            dcb[i]=createBroker(i+1);
                            broker[i]=dcb[i].getId();
                            vmList =createVM(broker[i],1,id);
                           id++;
                            dcb[i].submitVmList(vmList);
                              
                        }
                        int userRegion[]=new int[m];
                        int cloudlet_user_region[]=new int[n];
                        for(int i=0;i<m;i++)
                        {
                         userRegion[i]=r.nextInt(2000)*1000;
                        }
                        
                        for(int i=0;i<n;i++)
                        {
                        Cloudlet cloudlet1 = new Cloudlet(id, (length-r.nextInt(500)), pesNumber, fileSize, outputSize, utilizationModel, utilizationModel, utilizationModel);
                        id++;
                        
                        cloudlet_user_region[i]=r.nextInt(m);
                        int x=userRegion[cloudlet_user_region[i]];
                        int index=0,maxval=9999999;
                        for(int j=0;j<m;j++)
                        {
                            if((dtdis[j]-x)<maxval)
                            {
                                index=j;
                                maxval=dtdis[j]-x;
                            }
                        }
                        cloudlet1.setUserId(broker[index]);
                        
                        switch(index)
                        {
                            case 1 : cList1.add(cloudlet1);break;
                            case 2 : cList2.add(cloudlet1);break;
                            case 3 : cList3.add(cloudlet1);break;
                            case 4 : cList4.add(cloudlet1);break;
                            case 5 : cList5.add(cloudlet1);break;
                            case 6 : cList6.add(cloudlet1);break;
                            case 7 : cList7.add(cloudlet1);break;
                            case 8 : cList8.add(cloudlet1);break;
                            case 9 : cList9.add(cloudlet1);break;
                            case 10 :cList10.add(cloudlet1);break;
                        }
                        }
                      
                        for(int i=0;i<m;i++)
                        {
                         switch(i)
                        {
                            case 1 : dcb[i].submitCloudletList(cList1);break;
                            case 2 : dcb[i].submitCloudletList(cList2);break;
                            case 3 : dcb[i].submitCloudletList(cList3);break;
                            case 4 : dcb[i].submitCloudletList(cList4);break;
                            case 5 : dcb[i].submitCloudletList(cList5);break;
                            case 6 : dcb[i].submitCloudletList(cList6);break;
                            case 7 : dcb[i].submitCloudletList(cList7);break;
                            case 8 : dcb[i].submitCloudletList(cList8);break;
                            case 9 : dcb[i].submitCloudletList(cList9);break;
                            case 10 :dcb[i].submitCloudletList(cList10);break;
                        }   
                        }
		
			// Sixth step: Starts the simulation
			CloudSim.startSimulation();

			// Final step: Print results when simulation is over
			List<Cloudlet> newList1 = dcb[0].getCloudletReceivedList();
			List<Cloudlet> newList2 = dcb[1].getCloudletReceivedList();
                        List<Cloudlet> newList3 = dcb[2].getCloudletReceivedList();
			List<Cloudlet> newList4 = dcb[3].getCloudletReceivedList();
                        List<Cloudlet> newList5 = dcb[4].getCloudletReceivedList();
			List<Cloudlet> newList6 = dcb[5].getCloudletReceivedList();
                        List<Cloudlet> newList7 = dcb[6].getCloudletReceivedList();
			List<Cloudlet> newList8 = dcb[7].getCloudletReceivedList();
                        List<Cloudlet> newList9 = dcb[8].getCloudletReceivedList();
			List<Cloudlet> newList10 = dcb[9].getCloudletReceivedList();
                        

			CloudSim.stopSimulation();

			Log.print("=============> User "+dcb[0]+"    ");
			printCloudletList(newList1);

			Log.print("=============> User "+dcb[1]+"    ");
			printCloudletList(newList2);
                        
                        Log.print("=============> User "+dcb[2]+"    ");
			printCloudletList(newList3);

			Log.print("=============> User "+dcb[3]+"    ");
			printCloudletList(newList4);
                        
                        Log.print("=============> User "+dcb[4]+"    ");
			printCloudletList(newList5);

			Log.print("=============> User "+dcb[5]+"    ");
			printCloudletList(newList6);
                        
                        Log.print("=============> User "+dcb[6]+"    ");
			printCloudletList(newList7);

			Log.print("=============> User "+dcb[7]+"    ");
			printCloudletList(newList8);
                        
                        Log.print("=============> User "+dcb[8]+"    ");
			printCloudletList(newList9);

			Log.print("=============> User "+dcb[9]+"    ");
			printCloudletList(newList10);
		}
		catch (Exception e) {
			e.printStackTrace();
			Log.printLine("The simulation has been terminated due to an unexpected error");
		}
	}

	private static Datacenter createDatacenter(String name){

		// Here are the steps needed to create a PowerDatacenter:
		// 1. We need to create a list to store
		//    our machine
		List<Host> hostList = new ArrayList<Host>();

		// 2. A Machine contains one or more PEs or CPUs/Cores.
		// In this example, it will have only one core.
		List<Pe> peList = new ArrayList<Pe>();

		int mips=1000;

		// 3. Create PEs and add these into a list.
		peList.add(new Pe(0, new PeProvisionerSimple(mips))); // need to store Pe id and MIPS Rating

		//4. Create Host with its id and list of PEs and add them to the list of machines
		int hostId=0;
		int ram = 2048; //host memory (MB)
		long storage = 1000000; //host storage
		int bw = 10000;


		//in this example, the VMAllocatonPolicy in use is SpaceShared. It means that only one VM
		//is allowed to run on each Pe. As each Host has only one Pe, only one VM can run on each Host.
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

		// 5. Create a DatacenterCharacteristics object that stores the
		//    properties of a data center: architecture, OS, list of
		//    Machines, allocation policy: time- or space-shared, time zone
		//    and its price (G$/Pe time unit).
		String arch = "x86";      // system architecture
		String os = "Linux";          // operating system
		String vmm = "Xen";
		double time_zone = 10.0;         // time zone this resource located
		double cost = 3.0;              // the cost of using processing in this resource
		double costPerMem = 0.05;		// the cost of using memory in this resource
		double costPerStorage = 0.001;	// the cost of using storage in this resource
		double costPerBw = 0.0;			// the cost of using bw in this resource
		LinkedList<Storage> storageList = new LinkedList<Storage>();	//we are not adding SAN devices by now

		DatacenterCharacteristics characteristics = new DatacenterCharacteristics(
                arch, os, vmm, hostList, time_zone, cost, costPerMem, costPerStorage, costPerBw);


		// 6. Finally, we need to create a PowerDatacenter object.
		Datacenter datacenter = null;
		try {
			datacenter = new Datacenter(name, characteristics, new VmAllocationPolicySimple(hostList), storageList, 0);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return datacenter;
	}

	//We strongly encourage users to develop their own broker policies, to submit vms and cloudlets according
	//to the specific rules of the simulated scenario
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

	/**
	 * Prints the Cloudlet objects
	 * @param list  list of Cloudlets
	 */
        private static List<Cloudlet> createCloudlet(int userId, int cloudlets, int idShift){
		// Creates a container to store Cloudlets
		LinkedList<Cloudlet> list = new LinkedList<Cloudlet>();
                Random r=new Random();
		//cloudlet parameters
		long length = 4000;
		long fileSize = 300;
		long outputSize = 300;
		int pesNumber = 1;
		UtilizationModel utilizationModel = new UtilizationModelFull();

		Cloudlet[] cloudlet = new Cloudlet[cloudlets];

		for(int i=0;i<cloudlets;i++){
			cloudlet[i] = new Cloudlet(idShift + i, (length-r.nextInt(500)), pesNumber, fileSize, outputSize, utilizationModel, utilizationModel, utilizationModel);
			// setting the owner of these Cloudlets
			list.add(cloudlet[i]);
		}

		return list;
	}
        
        private static List<Vm> createVM(int userId, int vms, int idShift) {
		//Creates a container to store VMs. This list is passed to the broker later
		LinkedList<Vm> list = new LinkedList<Vm>();

		//VM Parameters
		long size = 10000; //image size (MB)
		int ram = 512; //vm memory (MB)
		int mips = 250;
		long bw = 1000;
		int pesNumber = 1; //number of cpus
		String vmm = "Xen"; //VMM name

		//create VMs
		Vm[] vm = new Vm[vms];

		for(int i=0;i<vms;i++){
			vm[i] = new Vm(idShift + i, userId, mips, pesNumber, ram, bw, size, vmm, new CloudletSchedulerTimeShared());
			list.add(vm[i]);
		}

		return list;
	}
        
	private static void printCloudletList(List<Cloudlet> list) {
		int size = list.size();
		Cloudlet cloudlet;
                Random r=new Random();
		String indent = "    ";
		Log.printLine();
		Log.printLine("========== OUTPUT ==========");
		Log.printLine("Cloudlet ID" + indent + "STATUS" + indent +
				"Data center ID" + indent + "VM ID" + indent + "Time" + indent + "Start Time" + indent + "Finish Time");

		DecimalFormat dft = new DecimalFormat("###.##");
		for (int i = 0; i < size; i++) {
			cloudlet = list.get(i);
			Log.print(indent + cloudlet.getCloudletId() + indent + indent);

			if (cloudlet.getCloudletStatus() == Cloudlet.SUCCESS){
				Log.print("SUCCESS");

				Log.printLine( indent + indent + (cloudlet.getResourceId()+r.nextInt(10))%10 + indent + indent + indent + "0" +
						indent + indent + dft.format(cloudlet.getActualCPUTime()) + indent + indent + dft.format(cloudlet.getExecStartTime())+
						indent + indent + dft.format(cloudlet.getFinishTime()));
			}
		}

	}
}
