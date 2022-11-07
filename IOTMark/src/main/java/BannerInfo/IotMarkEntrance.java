package BannerInfo;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class IotMarkEntrance {
    public String  StartIotMark(String info){
        String port="";
        String ip="";
        String[] infos=info.split(":");
        ip=infos[0];
        port=infos[1];
//        System.out.println(ip);
//        System.out.println(port);
        GetBanner getBanner=GetBannerFactory.get(port);
        String MarkResult=getBanner.getbanner(ip,port);
        System.out.println(MarkResult);
//        System.out.println(getBanner.getClass());
        if(!MarkResult.equals("")){
            MarkResult=MarkResult.replace('\n',' ');

            JSONObject arr= (JSONObject) new JSONObject("{\"list\":"+MarkResult+"}").get("list");
            String isMarked= (String) arr.get("Marked");
            if(isMarked.equals("true")){
                String deviceType="";
                try{
                    deviceType=arr.getString("DeviceType");
                }catch (Exception e){
                    System.out.println("不是IOT设备");
                }

                List<String> ICSTypes=new ArrayList<String>(){{
                    add("PLC");
                }};
                String res="";
                if(!deviceType.equals("")){
                    if(ICSTypes.contains(deviceType)){
                        res="{\"iotMarkResult\":"+MarkResult+","+"\"Type\":\"ICS\"}";
                    }
                    else{
                        res="{\"iotMarkResult\":"+MarkResult+","+"\"Type\":\"Iot\"}";
                    }
                }
                System.out.println(res);

                return res;
            }else{
                String res="";
                res="{\"iotMarkResult\":"+MarkResult+","+"\"Type\":\"Protocol\"}";
                return res;
            }

        }else{
            return "";
        }
    }

    public static void main(String[] args){
        IotMarkEntrance test=new IotMarkEntrance();
        System.out.println(test.StartIotMark("192.168.1.11:9600"));
    }
}
