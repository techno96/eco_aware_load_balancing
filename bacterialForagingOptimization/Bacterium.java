package org.cloudbus.cloudsim.bacterialForagingOptimization;

import java.util.Random;
import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.ResCloudlet;
import org.cloudbus.cloudsim.Vm;

public class Bacterium implements Comparable<Bacterium> {

    public static final String SPACE = " ";

    // value we are trying to minimize
    
    private double cost;

    // cost from intra-bacteria proximity
    //public double swarmCost;
    //public double prevSwarmCost;

    // accumulated measure per swim
    private double health;
    public ResCloudlet cloudlet;
    public Vm vm;
    public int original_vmid;

    private static Random random = new Random();

    public Bacterium(ResCloudlet c,Vm vm) {

        // random position bacterium
        cloudlet=c;
        this.vm=vm;

        // costs are due to environment and must be computed externally
        this.health = 0;
        original_vmid=c.getCloudlet().vmId;
    }

    public String toString() {
        final StringBuilder sb = new StringBuilder("[ ");
        
        sb.append(vm.getId()).append(SPACE);
        
        sb.append("] ");
        sb.append("cost = ").append(this.cost);
        sb.append(" health = ").append(this.health);
        return sb.toString();
    }

    public int compareTo(Bacterium other){
        if (this.health < other.health){
            return -1;
        } else if (this.health > other.health){
            return 1;
        } else {
            return 0;
        }
    }

    public double getHealth() {
        return health;
    }

    public void setHealth(double health) {
        this.health = health;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }


    public Vm getVm() {
        return vm;
    }

    public void setVm(Vm v) {
        this.vm=v;
    }
}
