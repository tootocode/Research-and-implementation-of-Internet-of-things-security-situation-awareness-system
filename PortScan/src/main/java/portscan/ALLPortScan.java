package portscan;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ALLPortScan {
    public List<String> ScanPorts(String port, String ip, String bandWith, String outputPath){
        //String cmd ="zmap  ";
        String cmd="zmap ";
        cmd=cmd+"-p "+port+" -i ens160 ";
        cmd+=" -o "+outputPath;
        List<String> result=new ArrayList<String>();
        // System.out.println(cmd);
//        Process p= null;
//        try {
////            p = Runtime.getRuntime().exec(cmd);
////            System.out.println(cmd);
////            InputStream in=p.getInputStream();
////            BufferedReader br=new BufferedReader(new InputStreamReader(in));
////            String li = br.readLine();
////            while(li!=null) {
////                System.out.println(li);
////                li = br.readLine();
////            }
//
//            int status = p.waitFor();
//            File file=new File(outputPath);
//            String line="";
//            InputStreamReader inputStreamReader=new InputStreamReader(new FileInputStream(file),"UTF-8");
//            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
//            while((line=bufferedReader.readLine())!=null){
//                //System.out.println(line);
//                result.add(line);
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
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
}
