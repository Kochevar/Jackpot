import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Random;


/**
 * Created with IntelliJ IDEA.
 * User: Matej Kočevar
 * Date: 19.9.2014
 * Time: 20:33
 */



public class gambler {
    static double[]machineProbability;

    public static void main(String args[]){
        //String adress = args[0]; //args[0]=http://celtra-jackpot.com/3
        String adress = "http://celtra-jackpot.com/3";
        int eps=10; //Epsilon

        int machines = getHTMLresponse(adress + "/machines");
        int pulls = getHTMLresponse(adress + "/pulls");
        System.out.println("O vrli gambler, na voljo imaš "+machines+" avtomatov in "+pulls+" potegov.");

        int[][] machineStats = new int[machines][2];   //0= uspešni potegi; 1= vsi potegi
        for (int i=0;i<machineStats.length;i++){for (int j=0;j<machineStats[i].length;j++){machineStats[i][j]=0;}}
        machineProbability = new double[machines];

        int pullNo=1;
        int reward=0; //bo enak kot vsota machineStats[machineCurrent][0]
        int machineCurrent=random(machines)-1, machineLast=-1;
        int odg;

        while (true){
            if (pullNo==pulls+1)break;

            //poteg ročice
            System.out.print(pullNo+". ");
            odg=getHTMLresponse(adress+"/"+(machineCurrent+1)+"/"+pullNo);
            machineStats[machineCurrent][1]++;
            if (odg==1){
                reward++;
                machineStats[machineCurrent][0]++;
            }
            pullNo++;
            for (int i=0;i<machineStats.length;i++) {
                machineProbability[i] = (machineStats[i][1])==0? 0 : machineStats[i][0] / (double)(machineStats[i][1]);
                System.out.print(machineStats[i][0] + ":" + (machineStats[i][1]));
                System.out.printf("{%.2f} ",machineProbability[i]*100);
            }

            //izbira naslednjega avtomata
            machineCurrent=chooseMachine(eps, machineStats, machineCurrent);
            if (machineLast != machineCurrent) {
                machineLast = machineCurrent;
                System.out.println(" ["+(machineCurrent+1)+"]");
            }
            else System.out.println();
        }

        //Statistika
        double max =0 ;
        for (double aMachineProbability : machineProbability)
            if (aMachineProbability > max)
                max = aMachineProbability;
        System.out.println("Dobiček: "+reward+"/"+pulls);
        System.out.printf("Neučinkovitost: %.2f",(max-(reward/(double)pulls))*100);
        System.out.println("%");
        System.exit(reward);
    }

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

    static int random(int max){
        Random rand = new Random();
        return rand.nextInt(max) + 1;
    }
    static int random(int exclude, int max){
        Random rand = new Random();
        int r = exclude+1;
        while (r==exclude+1)
            r=rand.nextInt(max) + 1;
        return r;
    }

    static int chooseMachine(int eps, int[][]stats, int machineCurrent){
        if (random(100)<=eps){
        //exploration
            int r = random(machineCurrent, stats.length)-1;
            System.out.print(" !"+(r+1)+"! ");
            return r;
        }

        else {
        //exploitation
            int index=0;
            double max = machineProbability[index];
            int min=0;
            for (int i=0; i<machineProbability.length; i++) {
                if (machineProbability[i] > max) {
                    max = machineProbability[i];
                    index=i;
                }
                else if (machineProbability[i] == max){
                    for (int j=0; j<stats.length; j++){
                        if (stats[j][1]<min) {
                            min = stats[j][i];
                            index = j;
                        }
                    }
                }
            }
            return index;
        }
    }
}