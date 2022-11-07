package BannerInfo;

import org.json.JSONObject;
import org.springframework.util.ResourceUtils;

import java.io.*;

public class GeBanner extends AbstractGetBanner{
    @Override
    public String getbanner(String ip, String port) {

        String input_file=this.input_file+port+"Port";
        String output_file=this.output_file+port+"Banner.txt";
        String send_data="/home/go/src/github.com/zmap/zgrab/GePlc.txt";
        String temp_cmd=this.cmd3+" --port 18245 "+
                "-senders=100 -data "+send_data+" --output-file="+output_file+" --input-file="+input_file;
        String returnresult="";
        System.out.println(temp_cmd);
        try{
            String result="";
            File file= ResourceUtils.getFile(input_file);
            if(!file.exists()){
                file.createNewFile();
            }
            FileWriter fileWritter = new FileWriter(input_file);
            fileWritter.write(ip);
            fileWritter.close();

            Process p=Runtime.getRuntime().exec(temp_cmd);
            int status = p.waitFor();
            InputStream in = p.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String li = br.readLine();
            while(li!=null) {
                System.out.println(li);
                li = br.readLine();
            }
            String line="";
            File f= ResourceUtils.getFile(output_file);
            InputStreamReader inputStreamReader=new InputStreamReader(new FileInputStream(f),"UTF-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            while((line=bufferedReader.readLine())!=null){
                result=line;
            }
            System.out.println(result);
            JSONObject arr= (JSONObject) new JSONObject("{\"list\":"+result+"}").get("list");
            JSONObject temp=(JSONObject) new JSONObject("{\"list\":"+arr.get("data")+"}").get("list");
            String read=temp.getString("read");
            byte[] b=read.getBytes("utf-8");
            if(b[8]==15){
                ip=ip+":18245";
                String resTemp="{\"ip\":\""+ip+"\","+"\"DeviceType\":\"PLC\","+"\"Vendor\":\"General Electric\","+"\"Device\":\"Ge-SRTP\","+
                        "\"Protocol\":\"GE-SRTP\",\"Marked\":\"true\"}";
                returnresult=resTemp;
            }
            System.out.println(b[8]);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return returnresult;
    }
    public static void main(String[] args){
        GeBanner geBanner=new GeBanner();
        System.out.println(geBanner.getbanner("192.168.1.18","18245"));
    }
}
