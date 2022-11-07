package BannerInfo;

import markDevice.AreWay;
import markDevice.SearchBase;
import org.json.JSONObject;
import org.springframework.util.ResourceUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class SSHBanner extends AbstractGetBanner {
    @Override
    public String getbanner(String ip, String port) {
        String input_file=this.input_file+port+"Port.txt";
        String output_file=this.output_file+port+"Banner.txt";
        String tempcmd=this.cmd+"--input-file="+input_file+" --output-file="+output_file+" -s 100 ssh";
        List<String> markres=new ArrayList<>();
        String Raw="";
        String connectInfo="";
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
            System.out.println(result);
            JSONObject arr= (JSONObject) new JSONObject("{\"list\":"+result+"}").get("list");
            JSONObject temp=(JSONObject) new JSONObject("{\"list\":"+arr.get("data")+"}").get("list");
            Raw=temp.get("ssh").toString();
            temp=(JSONObject) new JSONObject("{\"list\":"+temp.get("ssh")+"}").get("list");

            String isSuccess=(String) temp.get("status");
            connectInfo=isSuccess;
            if(isSuccess.equals("success")||isSuccess.equals("application-error")){
                temp=(JSONObject) new JSONObject("{\"list\":"+temp.get("result")+"}").get("list");
                JSONObject server_id=(JSONObject) new JSONObject("{\"list\":"+temp.get("server_id")+"}").get("list");
                String raw=server_id.getString("raw");
                SearchBase markDevice=new SearchBase();
                AreWay markDeviceAre=new AreWay();
                markres=markDevice.markDevice(raw);
//                if(!(markres.size()==3)){
//                    List<List<String>> tempAre=markDeviceAre.AreEntrance(raw);
//                    for(List<String> t:tempAre){
//                        if(t.size()==3){
//                            markres=t;
//                            break;
//                        }
//                    }
//                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        String jsonRes="";
        if(markres.size()==3){
            ip=ip+":22";
            jsonRes="{\"ip\":\""+ip+"\","+"\"Vendor\":\""+markres.get(0)+"\","+"\"DeviceType\":\""+markres.get(1)+"\","+
                    "\"Device\":\""+markres.get(2)+"\","+"\"Protocol\":\"ssh"+"\",\"Marked\":\"true\"}";
        }else{
            if(connectInfo.equals("success")||connectInfo.equals("application-error")){
                ip=ip+":22";
                jsonRes="{\"ip\":\""+ip+"\","+"\"Protocol\":\"ssh\","+"\"Marked\":\"false\"}";
            }

        }
        return jsonRes;
    }
}
