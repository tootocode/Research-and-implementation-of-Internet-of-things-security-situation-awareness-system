package BannerInfo;

import markDevice.AreWay;
import markDevice.SearchBase;
import org.json.JSONObject;
import org.springframework.util.ResourceUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class PJLBanner extends AbstractGetBanner {
    @Override
    public String getbanner(String ip, String port) {
        String output_file=this.output_file+port+"Banner.txt";
        String script_path=this.script_path+"PJLBanner.py";
        String result="";
        String Raw="";
        SearchBase markDevice=new SearchBase();
        AreWay markDeviceAre=new AreWay();
        List<String> markres=new ArrayList<>();
        try{
            String tempcmd=this.cmd4+script_path+" "+output_file+" "+ip;
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
            Raw=result;
            JSONObject arr= (JSONObject) new JSONObject("{\"list\":"+result+"}").get("list");
            String raw=(String) arr.get("raw");
            List<List<String>> res=new ArrayList<>();
            if(!raw.equals("")){
                res.add(markDevice.markDevice(raw));
            }
            boolean getResult=false;
            for(List<String> Result:res){
                if(Result.size()==3){
                    getResult=true;
                    break;
                }
            }
//            if(!getResult){
//                if(!raw.equals("")){
//                    List<List<String>> t=markDeviceAre.AreEntrance(raw);
//                    res.addAll(t);
//                }
//            }
            for(List<String> t:res){
                if(t.size()==3){
                    markres=t;
                    break;
                }
            }
        }catch ( Exception e){
            e.printStackTrace();
        }
        String jsonRes="";
        if(markres.size()==3){
            jsonRes="{\"ip\":\""+ip+"\","+"\"Vendor\":\""+markres.get(0)+"\","+"\"DeviceType\":\""+markres.get(1)+"\","+
                    "\"Device\":\""+markres.get(2)+"\","+"\"Protocol\":\"PJL"+"\",\"Marked\":\"true\"}";
        }else{
            jsonRes="{\"ip\":\""+ip+"\","+"\"Protocol\":\"PJL\","+"\"Marked\":\"false\",\"raw\":"+Raw+"}";
        }
        System.out.println(jsonRes);
        return jsonRes;
    }
    public static void main(String[] args){
        PJLBanner test=new PJLBanner();
        test.getbanner("109.89.46.81","9100");
    }
}
