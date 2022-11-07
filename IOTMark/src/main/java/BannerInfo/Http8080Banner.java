package BannerInfo;

import markDevice.AreWay;
import markDevice.SearchBase;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.util.ResourceUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Http8080Banner extends AbstractGetBanner {
    @Override
    public String getbanner(String ip, String port) {
        String input_file=this.input_file+port+"Port.txt";
        String output_file=this.output_file+port+"Banner.txt";
        String tempcmd=this.cmd+"--input-file="+input_file+" --output-file="+output_file+" -s 100 http";
        List<String> markres=new ArrayList<>();
        String raw="";
        String connectInfo="";
        try {

            File file= ResourceUtils.getFile(input_file);
            if(!file.exists()){
                file.createNewFile();
            }
            FileWriter fileWritter = new FileWriter(input_file);
            fileWritter.write(ip+":8080");
            fileWritter.close();

            Process p=Runtime.getRuntime().exec(tempcmd);
            System.out.println(tempcmd);
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
            SearchBase markDevice=new SearchBase();
            AreWay markDeviceAre=new AreWay();
            //System.out.println(result);
            JSONObject arr= (JSONObject) new JSONObject("{\"list\":"+result+"}").get("list");
            JSONObject temp=(JSONObject) new JSONObject("{\"list\":"+arr.get("data")+"}").get("list");
            temp=(JSONObject) new JSONObject("{\"list\":"+temp.get("http")+"}").get("list");
            raw= arr.get("data").toString();
            String isSuccess=(String) temp.get("status");
            connectInfo=isSuccess;
            if(isSuccess.equals("success")||isSuccess.equals("application-error")){
                temp=(JSONObject) new JSONObject("{\"list\":"+temp.get("result")+"}").get("list");
                JSONObject response=(JSONObject) new JSONObject("{\"list\":"+temp.get("response")+"}").get("list");
                JSONObject headers=(JSONObject) new JSONObject("{\"list\":"+response.get("headers")+"}").get("list");
                String server="";
                String www_authenticate="";
                String title="";
                String body="";
                try{
                    server=headers.get("server").toString();
                    System.out.println(server);
                }catch(Exception e){
                    e.printStackTrace();
                }
                try{
                    www_authenticate=headers.get("www_authenticate").toString();
                    System.out.println(www_authenticate);
                }catch(Exception e){
                    e.printStackTrace();
                }
                try{
                    body=response.get("body").toString();
                    Document doc= Jsoup.parse(body);
                    Elements Title=doc.select("title");
                    for(Element item:Title){
                        title=item.toString();
                    }
                    System.out.println(title);
                }catch(Exception e){
                    e.printStackTrace();
                }
                List<List<String>> res=new ArrayList<>();
                if(!server.equals("")){
                    String serverInfo="";
                    serverInfo=server.substring(2,server.length()-2);
                    res.add(markDevice.markDevice(serverInfo));
                    //System.out.println(serverInfo);
                }
                if(!www_authenticate.equals("")){
                    Pattern pattern=Pattern.compile("\\\\\"(.*?)\\\\\"");
                    Matcher matcher=pattern.matcher(www_authenticate);
                    if(matcher.find()){
                        String info=matcher.group(1);
                        //logger.info(matcher.group(1));
                        res.add(markDevice.markDevice(info));
                    }

                }
                if(!title.equals("")){
                    Pattern pattern=Pattern.compile("<title>(.*?)</title>");
                    Matcher matcher=pattern.matcher(title);
                    if(matcher.find()){
                        String info=matcher.group(1);
                        res.add(markDevice.markDevice(info));
                        //logger.info(matcher.group(1));
                    }
                }
                boolean getResult=false;
                for(List<String> Result:res){
                    if(Result.size()==3){
                        getResult=true;
                        break;
                    }
                }
//                if(!getResult){
//                    if(!server.equals("")){
//                        String serverInfo="";
//                        serverInfo=server.substring(2,server.length()-2);
//                        List<List<String>> t=markDeviceAre.AreEntrance(serverInfo);
//                        res.addAll(t);
//                    }
//                    if(!www_authenticate.equals("")){
//                        Pattern pattern=Pattern.compile("\\\\\"(.*?)\\\\\"");
//                        Matcher matcher=pattern.matcher(www_authenticate);
//                        if(matcher.find()){
//                            String info=matcher.group(1);
//                            List<List<String>> t=markDeviceAre.AreEntrance(info);
//                            res.addAll(t);
//                        }
//
//                    }
//                    if(!title.equals("")){
//                        Pattern pattern=Pattern.compile("<title>(.*?)</title>");
//                        Matcher matcher=pattern.matcher(title);
//                        if(matcher.find()){
//                            String info=matcher.group(1);
//                            List<List<String>> t=markDeviceAre.AreEntrance(info);
//                            res.addAll(t);
//                        }
//                    }
//                }
                for(List<String> t:res){
                    if(t.size()==3){
                        markres=t;
                        break;
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        String jsonRes="";
        if(markres.size()==3){

            jsonRes="{\"ip\":\""+ip+"\","+"\"Vendor\":\""+markres.get(0)+"\","+"\"DeviceType\":\""+markres.get(1)+"\","+
                    "\"Device\":\""+markres.get(2)+"\","+"\"Protocol\":\"http"+"\",\"Marked\":\"true\"}";
        }else{
            if(connectInfo.equals("success")||connectInfo.equals("application-error")){
                raw=raw.replace("\\n","");
                raw=raw.replace("\\t","");
                raw=raw.replace("\\r","");
                jsonRes="{\"ip\":\""+ip+":8080\","+"\"Protocol\":\"http\","+"\"Marked\":\"false\",\"raw\":\""+"\"}";
            }

        }
        return jsonRes;
    }
}
