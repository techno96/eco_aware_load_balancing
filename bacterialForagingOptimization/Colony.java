package org.cloudbus.cloudsim.bacterialForagingOptimization;

// collection of Bacterium

import java.util.List;
import java.util.Random;
import org.cloudbus.cloudsim.ResCloudlet;
import org.cloudbus.cloudsim.Vm;


public class Colony {

    public Bacterium[] bacteria;
    public Random r;
    public int size; 

    public Colony(int size,List<? extends Vm> vm,List<ResCloudlet> cList) {
        r=new Random();
        this.size=size;
        this.bacteria = new Bacterium[size];
        for (int i = 0; i < size; ++i){
            // sets position and health but not cost
            bacteria[i] = new Bacterium(cList.get(i),vm.get(r.nextInt(vm.size())));
        }
    }

    public String toString() {
        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bacteria.length; ++i){
            sb.append("[").append(i).append("] : ").append(bacteria[i]).append("\n");
        }
        return sb.toString();
    }

    public Bacterium getBacteria(int i) {
        return bacteria[i];
    }
    
    public Bacterium[] getBacteria() {
        return bacteria;
    }

    public void setBacteria(Bacterium[] bacteria) {
        this.bacteria = bacteria;
    }
}
