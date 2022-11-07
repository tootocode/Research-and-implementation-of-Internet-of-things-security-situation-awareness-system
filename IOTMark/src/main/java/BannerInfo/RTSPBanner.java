package BannerInfo;

import markDevice.AreWay;
import markDevice.SearchBase;
import org.json.JSONObject;
import org.springframework.util.ResourceUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class RTSPBanner extends AbstractGetBanner {
    @Override
    public String getbanner(String ip, String port) {
        String output_file=this.output_file+port+"Banner.txt";
        String script_path=this.script_path+"RTSPBanner.py";
        String result="";
        SearchBase markDevice=new SearchBase();
        AreWay markDeviceAre=new AreWay();
        List<String> markres=new ArrayList<>();
        String raw="";
        String connectInfo="";
        try{
            String tempcmd=this.cmd2+script_path+" "+output_file+" "+ip;
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
            raw=result;
            String IP=(String) arr.get("ip");
            String Server=(String) arr.get("Server");
            String banner=(String) arr.get("Banner");
            String basic_realm=(String) arr.get("basic_realm");
            String digest_realm=(String) arr.get("digest_realm");
            List<List<String>> res=new ArrayList<>();
            if(!Server.equals("")){
                res.add(markDevice.markDevice(Server));
            }
            if(!banner.equals("")){
                res.add(markDevice.markDevice(banner));
            }
            if(!basic_realm.equals("")){
                res.add(markDevice.markDevice(basic_realm));
            }
            if(digest_realm.equals("")){
                res.add(markDevice.markDevice(digest_realm));
            }
            boolean getResult=false;
            for(List<String> Result:res){
                if(Result.size()==3){
                    getResult=true;
                    break;
                }
            }
            for(List<String> t:res){
                if(t.size()==3){
                    markres=t;
                    break;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        String jsonRes="";
        if(markres.size()==3){
            ip=ip+":554";
            jsonRes="{\"ip\":\""+ip+"\","+"\"Vendor\":\""+markres.get(0)+"\","+"\"DeviceType\":\""+markres.get(1)+"\","+
                    "\"Device\":\""+markres.get(2)+"\","+"\"Protocol\":\"rtsp"+"\",\"Marked\":\"true\"}";
        }else{

            ip=ip+":554";
            jsonRes="{\"ip\":\""+ip+"\","+"\"Protocol\":\"rtsp\","+"\"Marked\":\"false\",\"raw\":"+raw+"}";
        }
        return jsonRes;
    }
}
