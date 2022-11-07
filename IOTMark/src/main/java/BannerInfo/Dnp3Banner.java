package BannerInfo;

import org.json.JSONObject;
import org.springframework.util.ResourceUtils;

import java.io.*;

public class Dnp3Banner extends AbstractGetBanner {
    @Override
    public String getbanner(String ip, String port) {
        String input_file=this.input_file+port+"Port.txt";
        String output_file=this.output_file+port+"Banner.txt";
        String tempcmd=this.cmd+" --input-file="+input_file+" --output-file="+output_file+" -s 10 dnp3";
        String returnresult="";
        try{
            System.out.println(tempcmd);
            String result="";
            File file= ResourceUtils.getFile(input_file);
            if(!file.exists()){
                file.createNewFile();
            }
            FileWriter fileWritter = new FileWriter(input_file);
            fileWritter.write(ip);
            fileWritter.close();

            Process p=Runtime.getRuntime().exec(tempcmd);
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
            ip= (String) arr.get("ip");
            JSONObject temp=(JSONObject) new JSONObject("{\"list\":"+arr.get("data")+"}").get("list");
            temp=(JSONObject) new JSONObject("{\"list\":"+temp.get("dnp3")+"}").get("list");
            String isSuccess=(String) temp.get("status");
            if(isSuccess.equals("success")){
                temp=(JSONObject) new JSONObject("{\"list\":"+temp.get("result")+"}").get("list");
                Boolean isDnp3=(Boolean) temp.get("is_dnp3");
                if(isDnp3){
                    ip=ip+":20000";
                    returnresult="{\"ip\":\""+ip+"\","+"\"Vendor\":\"Dnp3\","+"\"DeviceType\":\"PLC\","+"\"Device\":\"\","+
                            "\"Protocol\":\"Dnp3\",\"Marked\":\"true\"}";
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        System.out.println(returnresult);
        return returnresult;
    }
    public static void main(String[] args){
        Dnp3Banner test=new Dnp3Banner();
        test.getbanner("52.3.225.114","20000");
    }
}
