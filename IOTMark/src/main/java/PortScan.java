import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class PortScan {
    public List<String> ScanPorts(String port, String ip, String bandWith, String outputPath){
        String cmd ="sudo zmap  ";
        cmd=cmd+"-p "+port;
        cmd+=" "+ip+" -o "+outputPath+" -r " +bandWith;
        List<String> result=new ArrayList<String>();
        System.out.println(cmd);
        Process p= null;
        try {
            p = Runtime.getRuntime().exec(cmd);
            //System.out.println(cmd);
            int status = p.waitFor();
            File file=new File(outputPath);
            String line="";
            InputStreamReader inputStreamReader=new InputStreamReader(new FileInputStream(file),"UTF-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            while((line=bufferedReader.readLine())!=null){
                //System.out.println(line);
                result.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return result;
    }
}
