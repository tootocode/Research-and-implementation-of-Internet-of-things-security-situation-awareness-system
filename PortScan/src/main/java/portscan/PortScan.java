package portscan;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PortScan {
    public List<String> ScanPorts(String port, String ip, String bandWith, String outputPath){
        //String cmd ="zmap  ";
        String cmd="zmap ";
        cmd=cmd+"-p "+port+" -i ens160 ";
        cmd+=" "+ip+" -o "+outputPath+" -r " +bandWith;
        if(port.equals("3702")||port.equals("47808")||port.equals("161"))
            cmd+=" -M icmp_echoscan";
        List<String> result=new ArrayList<String>();
       // System.out.println(cmd);

        try {
            final Process p = Runtime.getRuntime().exec(cmd);
            System.out.println(cmd);

            new Thread(() -> {
                BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
                String line = null;

                try {
                    while ((line = in.readLine()) != null) {
                        System.out.println("output: " + line);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();

            new Thread(() -> {
                BufferedReader err = new BufferedReader(new InputStreamReader(p.getErrorStream()));
                String line = null;

                try {
                    while ((line = err.readLine()) != null) {
                        System.out.println("err: " + line);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        err.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();

            p.waitFor();
            System.out.println("执行完毕");
            File file=new File(outputPath);
            String line="";
            InputStreamReader inputStreamReader=new InputStreamReader(new FileInputStream(file),"UTF-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            while((line=bufferedReader.readLine())!=null){
                System.out.println(line);
                result.add(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    public static void main(String[] args){
        PortScan portScan=new PortScan();
        List<String> result=portScan.ScanPorts("80","129.226.10.238","100","/home/9600Portresult.csv");
        System.out.println(result);
    }
}
