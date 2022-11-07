package BannerInfo;

import org.json.JSONObject;
import org.springframework.util.ResourceUtils;

import java.io.*;

public class S7Banner extends AbstractGetBanner {
    @Override
    public String getbanner(String ip, String port) {
        String input_file=this.input_file+port+"Port.txt";
        String output_file=this.output_file+port+"Banner.txt";
        String tempcmd=this.cmd+"--input-file="+input_file+" --output-file="+output_file+" -s 10 siemens";
        String returnresult="";
        try{
            File file= ResourceUtils.getFile(input_file);
            if(!file.exists()){
                file.createNewFile();
            }
            FileWriter fileWritter = new FileWriter(input_file);
            fileWritter.write(ip);
            fileWritter.close();

            Process p=Runtime.getRuntime().exec(tempcmd);

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
            String result="";
            while((line=bufferedReader.readLine())!=null){
                result=line;
            }
            //System.out.println(result);
            JSONObject arr= (JSONObject) new JSONObject("{\"list\":"+result+"}").get("list");
            ip= (String) arr.get("ip");
            JSONObject temp=(JSONObject) new JSONObject("{\"list\":"+arr.get("data")+"}").get("list");
            temp=(JSONObject) new JSONObject("{\"list\":"+temp.get("siemens")+"}").get("list");
            String isSuccess=(String) temp.get("status");
            if(isSuccess.equals("success")){
                temp=(JSONObject) new JSONObject("{\"list\":"+temp.get("result")+"}").get("list");
                Boolean isFox= (Boolean) temp.get("is_s7");
                if(isFox){
                    String module= (String) temp.get("module_id");
                    ip=ip+":102";
                    returnresult="{\"ip\":\""+ip+"\","+"\"DeviceType\":\"PLC\","+"\"Vendor\":\"Siemens\","+"\"Device\":\""+module+"\","+
                            "\"Protocol\":\"Siemens S7\",\"Marked\":\"true\"}";
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return returnresult;
    }
    public static void main(String[] args){
        S7Banner s7Banner=new S7Banner();
        System.out.println(s7Banner.getbanner("192.168.1.19","102"));
    }
}
