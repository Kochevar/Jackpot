import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Scanner;


/**
 * Created with IntelliJ IDEA.
 * User: Matej Kočevar
 * Date: 19.9.2014
 * Time: 20:33
 */


public class gambler {

    static int getHTMLresponse (String path) {
        try {
            URL url = new URL(path);
            BufferedReader in = new BufferedReader( new InputStreamReader(url.openStream()));
            String value = in.readLine();
            try {
                return Integer.parseInt(value);
            } catch (Exception e){
                System.out.println("Unexpected respond from server: "+value);
                return -1;
            }
        } catch (Exception e){
            System.out.println("Error: "+e);
            System.out.close();
            return -1;
        }
    }

    static int returnMaxMachine(){
        double max=machineProbability[0];
        int index=0;
        for (int i=0;i<machines;i++){
            if(machineProbability[i]>max){
                max=machineProbability[i];
                index=i;
            }
        }
        return index;
    }
     /*
    static void deleteTemp(){
        for (int i=0; i<machines; i++){
            machineTemp[i]=0;
        }

    }  */

    static double[] machineProbability;
    static int machines;
    static int[] machineTemp;

    public static void main(String args[]){
        Scanner sc = new Scanner(System.in);
        System.out.print("Vnesite naslov in številko primera (privzeto: http://celtra-jackpot.com/3): ");
        String input =  sc.nextLine();
        String adress = input.equals("") ? "http://celtra-jackpot.com/3" :  input;

        machines = getHTMLresponse(adress + "/machines");
        int pulls = getHTMLresponse(adress + "/pulls");

        System.out.println("O vrli ver0.gambler, na voljo imaš "+machines+" avtomatov in "+pulls+" potegov.");

        double magic = 10; //ker še ne znam določiti smiselnega števila iz 2. koraka, naj bo 10
        int pullNo=1;

        machineProbability = new double[machines];
        machineTemp = new int[machines];
        for (int i=0; i<machines; i++){
            machineProbability[i]=0;
            machineTemp[i]=0;
        }
        int gain=0;
        int odg;


        //SMART START
        for (int i=0;i<magic;i++){
            for (int m=0; m<machines; m++){
                odg=getHTMLresponse(adress+"/"+(m+1)+"/"+pullNo);
                pullNo++;
                if (odg==1) {
                    machineTemp[m]++;
                    gain++;
                }
                //System.out.println((m+1)+": "+odg);

            }
        }

        System.out.println("*Smart start*");
        for (int i=0;i<machines;i++){
            machineProbability[i]=machineTemp[i]/magic;
            System.out.println(" "+(i+1)+": "+machineProbability[i]);
        }
        System.out.println();


        //GAINING
        int m=-1;
        int count=1;
        int old;
        //deleteTemp();
        while (pullNo<pulls){


            odg=getHTMLresponse(adress+"/"+(m+1)+"/"+pullNo);

            System.out.println(pullNo+" ");
            if (odg==1) {
                machineTemp[m]++;
                gain++;
            }

            for (int i=0;i<machines;i++){
                machineProbability[i]+=machineTemp[i]/count;
                System.out.print(" "+(i+1)+": "+machineProbability[i]);
            }
            count++;
            old =m;
            m=returnMaxMachine();
            System.out.println("M: "+m);
            if (m != old){
                count=1;
                System.out.println("Izbran avtomat številka: "+(m+1));
            }

            pullNo++;
        }

        System.out.println("Dobiček: "+gain+"/"+pulls);

        /*
        int m=3;
        for (int i=1;i<=pulls;i++){

            odg=getHTMLresponse(adress+"/"+(m)+"/"+(i));
            if (odg==1) {
                machineTemp[m-1]++;
                gain++;

            }
            System.out.println((machineTemp[m-1]/((double)i))*100);

        }
        System.out.println("Dobiček: "+gain+"/"+pulls);
        */
    }
}
