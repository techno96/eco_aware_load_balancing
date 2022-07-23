package org.cloudbus.cloudsim.bacterialForagingOptimization;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.math.*;
import org.cloudbus.cloudsim.Datacenter;
import org.cloudbus.cloudsim.DatacenterBroker;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.ResCloudlet;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.core.CloudSimTags;
import org.cloudbus.cloudsim.core.SimEvent;

public class BFOA {
    public static final String SPACE = " ";
    private static Random random = new Random();
    
    
    public List<Vm> vm;
    public int colonySize ;
    public List<ResCloudlet> cList;
    public List<Vm> ovm;
    public List<Vm> lvm;
    public List<Vm> vm_findAll;
    public static double threshold=0.8;
    public Colony colony,colony1; 
    public double old_variance;
    public double new_variance;
    public DatacenterBroker broker;
    
    public BFOA(List<? extends Vm> list,DatacenterBroker x)
    {
        vm=new ArrayList<Vm>();
        Log.printLine(list.size());
        vm.addAll(list);
        this.broker=x;
    }
    
    public boolean check_overload()
    {
    double total_load=0.0;
    double total_capacity=0.0;
    
    for(int i=0;i<vm.size();i++)
    {
     total_load+=vm.get(i).getLoad();
     total_capacity+=vm.get(i).getCapacity();
    }
    
    if(total_load>total_capacity)
    {
        Log.printLine("Load Balancing not possible");
        return false;
    }
    ovm=new ArrayList<Vm>();
    lvm=new ArrayList<Vm>();
        for(int i=0;i<vm.size();i++)
        {
        if(loadOfVm(vm.get(i))==1)
        {
            ovm.add(vm.get(i));
            
        }
        else if(loadOfVm(vm.get(i))==2)
        {
            
            lvm.add(vm.get(i));
            
        }
        else
            continue;
    }
        
        /*for(int i=0;i<lvm.size();i++)
        {
            for(int j=0;j<lvm.size()-1;j++)
            {
                if(lvm[j].getLoad()>lvm[j+1].getLoad())
                {
                    Vm g=lvm[j];
                    lvm[j]=lvm[j+1];
                    lvm[j+1]=g;
                }
            }
        }*/
        return true;
    }
    
    public int loadOfVm(Vm v)
    {
        if(v.getTotalUtilizationOfCpu(CloudSim.clock())>0.5)
            return 1;
        else if(v.getTotalUtilizationOfCpu(CloudSim.clock())<0.25)
            return 2;
        else 
            return 3;

    }
    
    public int overloadedVMCount()
    {
        int count=0;
        for(int i=0;i<vm.size();i++)
        {
         if(vm.get(i).getTotalUtilizationOfCpu(CloudSim.clock())>0.25)
           count++;
        }
        return count;
    }
            
    public void setData()
    {
       cList=new ArrayList<ResCloudlet>();
       for(int i=0;i<ovm.size();i++)
       {
           cList.addAll(ovm.get(i).getCloudletScheduler().getAllSchedulerCloudlets());
       }
       colonySize=cList.size();
    }

    public int bac_forg_optim() {
        
        //final int dim = 2;                    //problem dimension ("p")
        //final double minValue = -5.12;
        //final double maxValue = 5.12;
        
        final int nc = 20;                    //chemotactic steps, index = j
        final int ns = 5;                     //maximum swim steps, index = m
        //final int nre = 8;                    //reproduction steps, index = k
        final int ned = 6;                    //dispersal steps, index = l ('el')
        final double ped = 0.25;              //probability of dispersal
        final double ci = 0.05;               //basic bacteria movement increment size (for all bacteria)
        
        
        if(check_sd())//find standard deviation
        {
            Log.printLine("System is balanced");
            return 0;
        }
        if(!check_overload())
        {
            
            return 0;
        }
        
        setData();
        // initialize a colony of bacteria at random positions (but not the costs)
        colony = new Colony(colonySize,lvm,cList);
        colony1 = new Colony(colonySize,lvm,cList);
        colony1=colony;
        
        System.out.println("Computing the cost value for each bacterium");
        for (int i = 0; i < colonySize; ++i) {// costs
            double cost = cost(colony.getBacteria(i),colony); // compute cost
            colony.getBacteria(i).setCost(cost);
            
            colony1.getBacteria(i).setCost(cost);            
        }
        
        old_variance=new_variance=costVariation();


        // time counter
        int t = 0;
        // eliminate-disperse loop
        for (int l = 0; l < ned; ++l) {
                // chemotactic loop; the lifespan of each bacterium
                for (int j = 0; j < nc; ++j) {
                    // each bacterium
                    for (int i = 0; i < colonySize; ++i) {
                        // tumble (point in a new direction)
                        colony.getBacteria(i).setVm(lvm.get(random.nextInt(lvm.size())));

                        colony.getBacteria(i).setCost(cost(colony.getBacteria(i),colony));
                        
                    }
                    new_variance=costVariation();//compute variation of costs
                    if(new_variance<old_variance)
                    {
                        colony1=colony;
                        old_variance=new_variance;
                    }
                    for(int i=0;i < colonySize; ++i)
                    {
                        // swim or not based on prev and curr costs
                        int m = 0;
                        int x=0;
                        
                        // we are improving
                        Datacenter d=colony.getBacteria(i).getVm().getHost().getDatacenter();
                        findAll(d,colony.getBacteria(i).getVm());
                        while (m!=(vm_findAll.size()-1)&&old_variance==new_variance&&vm_findAll.size()!=0)//also include variation of costs 
                        {
                            
                        colony.getBacteria(i).setVm(vm_findAll.get(m));
                        m++;
                        colony.getBacteria(i).setCost(cost(colony.getBacteria(i),colony));
                        new_variance=costVariation();
                        
                        if(new_variance<old_variance)
                        {
                         colony1=colony;
                         old_variance=new_variance;
                        }
                        
                        }// while improving 
                        
                    }
                    // i, each bacterium in the chemotactic loop
                    ++t;   // increment the time counter
                } // j, chemotactic loop

            // eliminate-disperse
            for (int i = 0; i < colonySize; ++i) {
                double prob = random.nextDouble();
                if (prob < ped) {// disperse this bacterium to a random position
                    colony.getBacteria(i).setVm(lvm.get(random.nextInt(lvm.size())));
                    // update costs
                    // compute
                    double cost = cost(colony.getBacteria(i),colony);
                    colony.getBacteria()[i].setCost(cost);
                    new_variance=costVariation();

                    // new best by pure luck?
                    if (new_variance<old_variance) {
                        colony1=colony;
                        old_variance=new_variance;
                    }
                }
            }
        } // l, elimination-dispersal loop, end processing
        Log.printLine(ovm.size()+"\t"+colony.size+"\t"+colony1.size+"\t"+cList.size());
        for(int i=0;i<ovm.size();i++)
        {
            double lowest_cost=0;
            int index=0;
            
            boolean flag=false;
            for(int j=0;j<colony1.size;j++)
            {
               if(colony1.getBacteria(j).cloudlet.getCloudlet().vmId==ovm.get(i).getId()&&flag==false)
               {
                   lowest_cost=colony1.getBacteria(j).getCost();
                   index=j;
                   flag=true;
               }
               else if(colony1.getBacteria(j).cloudlet.getCloudlet().vmId==ovm.get(i).getId()&&flag==true&&colony1.getBacteria(j).getCost()<lowest_cost)
               {
                   lowest_cost=colony1.getBacteria(j).getCost();
                   index=j;
               }
            }
            if(colony1.size!=0)
            {
            int arr[]=new int[5];
            arr[0]=colony1.getBacteria(index).cloudlet.getCloudletId();
            colony1.getBacteria(index).cloudlet.getCloudlet().already_migrated=true;
            arr[1]=broker.getId();
            arr[2]=colony1.getBacteria(index).cloudlet.getCloudlet().vmId;
            arr[3]=colony1.getBacteria(index).vm.getId();
            arr[4]=colony1.getBacteria(index).vm.getHost().getDatacenter().getId();
            SimEvent ev=new SimEvent(CloudSimTags.CLOUDLET_MOVE,0.2,CloudSimTags.CLOUDLET_MOVE,arr.clone());
            ovm.get(i).getHost().getDatacenter().processEvent(ev);
            Log.printLine("Cloudlet "+arr[0]+" is migrated from VM "+arr[2]+" to VM "+arr[3]);
            }
        }
        
            Log.printLine("Load Balancing done");
            return ovm.size();
    }


    /**
     * the cost function we are trying to minimize
     */
    private static double cost(Bacterium b,Colony c){
        
        double cost=0.0;
        cost=b.vm.getLoad()-b.vm.getRemainingCapacity()-b.vm.getHost().getDatacenter().solar-b.vm.getHost().getDatacenter().wind;
        for(int i=0;i<c.size;i++)
        {
            if(c.getBacteria(i)!=b&&c.getBacteria(i).vm==b.vm)
            {
                cost+=c.getBacteria(i).cloudlet.getCloudletLength();
            }
        }
        return cost;
         
    }
    
    private void findAll(Datacenter d,Vm v)
    {
        int ss=0;
        vm_findAll=new ArrayList<Vm>();
        for(int i=0;i<lvm.size();i++)
        {
        if(v!=lvm.get(i)&&d.getVmList().contains(lvm.get(i)))
        {
            vm_findAll.add(lvm.get(i));
        }
        }
    }
    
    public boolean check_sd()
    {
    double total_load=0.0;
    double total_capacity=0.0;
    double total_process_time=0.0;
    double standard_deviation=0.0,x;
    for(int i=0;i<vm.size();i++)
    {
     total_load+=vm.get(i).getLoad();
     total_capacity+=vm.get(i).getCapacity();
    }
    
    total_process_time=total_load/total_capacity;
    for(int i=0;i<vm.size();i++)
    {
     x=((vm.get(i).getLoad()/vm.get(i).getCapacity())-total_process_time);
     x=x*x;
     standard_deviation+=x;
    }
    standard_deviation/=vm.size();
    standard_deviation=Math.sqrt(standard_deviation);
    //standard_deviation*=10;
    Log.printLine("The total load is "+total_load+"\nThe total_capacity is "+total_capacity);
    Log.printLine("The standard deviation is "+standard_deviation);
    if(standard_deviation*10<=threshold)
    {
     return true;   
    }
    else
        return false;
    }
    
    public double costVariation()
    {
        double total_cost=0.0,x,variance=0.0;
        for(int i=0;i<colonySize;i++)
        {
            total_cost+=colony.getBacteria(i).getCost();
        }
        total_cost/=colonySize;
        for(int i=0;i<colonySize;i++)
        {
            x=colony.getBacteria(i).getCost()-total_cost;
            x=x*x;
            variance+=x;
        }
        variance/=colonySize;
        return variance;
    }
}
