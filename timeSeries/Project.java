package org.cloudbus.cloudsim.timeSeries;

import java.io.*;
import java.text.DecimalFormat;
import java.util.Scanner;



public class Project {
    public static void main(String [] args) {
        int m=0,a=0,b=200;
        int x,y;
        double[] count=new double[3115];
        double[] frd=new double[10];
        try 
        {
            Scanner sc = new Scanner(new File("G:\\Final Year Proj\\TimeSeriesAnalysis2\\src\\Trace1.txt"));    
            FileOutputStream fos=new FileOutputStream("G:\\Final Year Proj\\Dataset\\Count.txt");
            
                while (sc.hasNext())
                {
                    x=sc.nextInt();
                    y=sc.nextInt();
                    if(y>=a&&y<=b)
                    {
                        count[m]+=1;
                    }
                    else
                    {
                       m++;
                       a+=200;
                       b+=200;
                       count[m]+=1;
                        
                    }
                           
                }
            double mean=0.0;
            
            for(int l=0;l<3115;l++)
            {
            mean=mean+count[l];    
            }
            mean=mean/3115;
            DecimalFormat dft = new DecimalFormat("###.##");
            for(int l=1;l<3116;l++)
            {
                String vv=Integer.toString(l);
                fos.write(vv.getBytes());
                vv="\t";
                fos.write(vv.getBytes());
                Log.printLine(count[l-1]);
                vv=Double.toString(count[l-1]);
                fos.write(vv.getBytes());
                vv="\t";
                fos.write(vv.getBytes());
                for(int g=1;g<30;g++)
                {
                    if(l<=g)
                    {
                        vv=Double.toString(0.0);
                        fos.write(vv.getBytes());
                        vv="\t";
                        fos.write(vv.getBytes());
                    }
                    else
                    {
                        vv=dft.format((count[l-1]-mean)*(count[l-g-1]-mean));
                        fos.write(vv.getBytes());
                        vv="\t";
                        fos.write(vv.getBytes());
                    }
                }
                
                vv="\r\n";
                fos.write(vv.getBytes());
            }
            fos.flush();
        }
    
    catch(Exception e) { // TODO Auto-generated catch block e.printStackTrace(); } }
             Log.printLine(e);
    }
        /*ArimaParams ap=new ArimaParams(2,1,5,0,0,0,0);
        ArimaSolver as;
        as = new ArimaSolver();
        Arima aa;
        aa = new Arima();
        ArimaModel am;
        
        am = new ArimaModel(ap,count,3000);
        
        ForecastResult fr=am.forecast(115);
        
        frd=fr.getForecast();
        
        for(int k=0;k<frd.length;k++)
        {
            Log.printLine(frd[k]);
        }
        Log.printLine(am.getRMSE());*/
        new ArimaTest().commonTestLogic("", count, 0.01, 2, 1, 5, 0, 0, 0, 0);
        
    }
}