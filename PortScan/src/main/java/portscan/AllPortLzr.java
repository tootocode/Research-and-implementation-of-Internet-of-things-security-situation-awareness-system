package portscan;

import queue.ActivemqConfig;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
public class AllPortLzr {
    public static ActivemqConfig activemqConfig = new ActivemqConfig("/Config");
    public List<String> ScanPorts(String ip){
        //String cmd ="zmap  ";
        String cmd="cat services_list | /home/go/src/github.com/stanford-esrg/lzr/lzr --handshakes http,tls -sendSYNs -scanIP "+ip+" -sourceIP "+activemqConfig.ActiveMqIP ;
        Process p= null;
        List<String> result=new ArrayList<String>();
        try {
            p = Runtime.getRuntime().exec(cmd);
            System.out.println(cmd);
//            InputStream in=p.getInputStream();
//            BufferedReader br=new BufferedReader(new InputStreamReader(in));
//            String li = br.readLine();
//            while(li!=null) {
//                System.out.println(li);
//                li = br.readLine();
//            }

            cmd = "/home/go/test1";
            p = Runtime.getRuntime().exec(cmd);
            System.out.println(cmd);

            int status = p.waitFor();
            File file=new File("/home/go/src/github.com/stanford-esrg/lzr/");
            String line="";
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            while((line=br.readLine())!=null){
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
    public static void main(String[] args){
        PortScan portScan=new PortScan();
        List<String> result=portScan.ScanPorts("9600","192.168.1.11","100","/Users/ludam/Desktop/Project/portscan/9600Portresult.csv");
        System.out.println(result);
    }
}
