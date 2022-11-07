package BannerInfo;

import org.json.JSONObject;
import org.springframework.util.ResourceUtils;

import java.io.*;

public class BacnetBanner extends AbstractGetBanner {
    public String getbanner(String ip,String port){
        String input_file=this.input_file+port+"Port.txt";
        String output_file=this.output_file+port+"Banner.txt";
        String tempcmd=this.cmd+" --input-file="+input_file+" --output-file="+output_file+" -s 10 bacnet";
        String returnresult="";
        try{
            String result="";
            File file= ResourceUtils.getFile(input_file);
            if(!file.exists()) {
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
            String model_name="";
            String vendor_name="";
            JSONObject arr= (JSONObject) new JSONObject("{\"list\":"+result+"}").get("list");
            JSONObject temp=(JSONObject) new JSONObject("{\"list\":"+arr.get("data")+"}").get("list");
            temp=(JSONObject) new JSONObject("{\"list\":"+temp.get("bacnet")+"}").get("list");
            String isSuccess=(String) temp.get("status");

            //logger.info(temp.get("status"));
            temp=(JSONObject) new JSONObject("{\"list\":"+temp.get("result")+"}").get("list");
            Boolean isBacnet=(Boolean) temp.get("is_bacnet");
            if(isBacnet){
                try{
                    try{
                        model_name=(String)temp.get("model_name");
                    }catch (Exception e){e.printStackTrace();}
                    try{
                        vendor_name=(String) temp.get("vendor_name");
                    }catch (Exception e){e.printStackTrace();}
                    ip=ip+":47808";
                    returnresult="{\"ip\":\""+ip+"\","+"\"Vendor\":\""+vendor_name+"\","+"\"DeviceType\":\"PLC\","+"\"Device\":\""+model_name+"\","+
                            "\"Protocol\":\"BACnet\",\"Marked\":\"true\"}";
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        System.out.println(returnresult);
        return returnresult;
    }
    public static void main(String[] args){
        BacnetBanner test=new BacnetBanner();
        test.getbanner("64.124.9.134","47808");
    }
}
