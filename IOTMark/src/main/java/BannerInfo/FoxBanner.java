package BannerInfo;

import org.json.JSONObject;
import org.springframework.util.ResourceUtils;

import java.io.*;

public class FoxBanner extends AbstractGetBanner {

    @Override
    public String getbanner(String ip, String port) {
        String input_file=this.input_file+port+"Port.txt";
        String output_file=this.output_file+port+"Banner.txt";
        String tempcmd=this.cmd+" --input-file="+input_file+" --output-file="+output_file+" -s 10 fox";
        String returnresult="";
        try{
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
            JSONObject arr= (JSONObject) new JSONObject("{\"list\":"+result+"}").get("list");
            JSONObject temp=(JSONObject) new JSONObject("{\"list\":"+arr.get("data")+"}").get("list");
            temp=(JSONObject) new JSONObject("{\"list\":"+temp.get("fox")+"}").get("list");
            String isSuccess=(String) temp.get("status");
            if(isSuccess.equals("success")){
                temp=(JSONObject) new JSONObject("{\"list\":"+temp.get("result")+"}").get("list");
                Boolean isFox= (Boolean) temp.get("is_fox");
                String Station_name="";
                String vendor_name="";
                if(isFox){
                    try{
                        Station_name= (String) temp.get("station_name");
                        vendor_name=(String) temp.get("brand_id");
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                    ip=ip+":1911";
                    returnresult="{\"ip\":\""+ip+"\","+"\"Vendor\":\"Niagara\","+"\"DeviceType\":\"PLC\","+"\"Device\":\""+Station_name+"\","+
                            "\"Protocol\":\"Niagara Fox\",\"Marked\":\"true\"}";
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        System.out.println(returnresult);
        return returnresult;
    }
    public static void main(String[] args){
        FoxBanner test=new FoxBanner();
        test.getbanner("199.120.80.251","4911");
    }
}
