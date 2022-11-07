import BannerInfo.IotMarkEntrance;
import org.springframework.util.ResourceUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class AreTestMain {
    public static void main(String[] args) throws IOException {
        File f= ResourceUtils.getFile("/home/ysf/桌面/Project_service/IOTMark/src/main/java/InputIp");
        InputStreamReader inputStreamReader=new InputStreamReader(new FileInputStream(f),"UTF-8");
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        String line="";
        List<String> ports=new ArrayList<String>();
 //         ports.add("80");
//        ports.add("47808");
//        ports.add("2455");
//        ports.add("789");
//        ports.add("2222");
//        ports.add("20000");
//        ports.add("44818");
//        ports.add("1911");
//        ports.add("21");
//        ports.add("18245");
//        ports.add("2404");
//        ports.add("5007");
//        ports.add("4800");
//        ports.add("1962");
//        ports.add("20547");
//        ports.add("102");
//          ports.add("23");
//        ports.add("22");
//        ports.add("554");
 //         ports.add("161");
//        ports.add("3702");
         // ports.add("9100");
//        ports.add("8080");
          //ports.add("443");
          ports.add("8443");
        PortScan portScan=new PortScan();
        IotMarkEntrance iotMarkEntrance=new IotMarkEntrance();
        while((line=bufferedReader.readLine())!=null){
            String ip=line;
            for(String port:ports){
                List<String> result=portScan.ScanPorts(port,ip,"100","/Users/ludam/Desktop/AreTestEnv/PortScan/"+port+"Portresult.csv");
                for(String IP:result){
                    //System.out.println(ip+" "+port);
                    iotMarkEntrance.StartIotMark(ip+":"+port);
                }
            }
        }
    }
}
