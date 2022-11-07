package markDevice;

import database.*;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.omg.Messaging.SYNC_WITH_TRANSPORT;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AreWay {
    public List<Rule> AreEntrance(String info){
        List<String> removeInfo=new ArrayList<String>(){{
            add("comware");
            add("platform");
            add("software");
            add("version");
            add("release");
            add("copyright");
            add("new");
            add("technologies");
            add("co");
            add("ltd");
            add("all");
            add("rights");
            add("right");
            add("reserved");
            add("@pjl");
            add("laserjet");
            add("server");
        }
        };
        info=info.replace(";","");
        info=info.replace(",","");
        info=info.replace("}","");
        info=info.replace("{","");
        info=info.replace(".","");
        info=info.replace(":"," ");
        info=info.replace(".","");
        info=info.replace("\n"," ");
        System.out.println(info);
        String Vendor="";
        String DeviceType="";
        boolean getVendor=false;
        boolean getDeviceType=false;
        String[] infos=info.split("\\s+");             //根据多个空格拆分
        Query Vendorquery= QueryFactory.get(GeneralQuery.class);
        //System.out.println("789");
        for(String inform:infos){
            boolean vendorquery=Vendorquery.query(inform,"Vendors","vendor");
            if(vendorquery){
                Vendor=inform;
                getVendor=true;
            }
            System.out.println("areway"+getVendor);
        }
        Query generalQueryers=QueryFactory.get(GeneralQuerys.class);
        List<String> deviceTypes=generalQueryers.query("DeviceType","DeviceType");
        List<String> vendors=generalQueryers.query("Vendors","vendor");
        String KeyWord="";
        if(getVendor) KeyWord=Vendor;
        List<Rule> searchResult=new ArrayList<>();
        for(String inform:infos){
            KeyWord=Vendor;
            if(!inform.equals(Vendor)&&!removeInfo.contains(inform.toLowerCase())){
                KeyWord=KeyWord+"%20"+inform;
                System.out.println(KeyWord);
                //if(inform.equals("MSR36-10"))
                Rule t= SearchUrl(KeyWord,inform,vendors,deviceTypes);
                if(t.getNum()>3){
                    t.Device=inform;
                    searchResult.add(t);
                }

            }
        }
        return searchResult;
    }

    public Rule SearchUrl(String engineUrl,String device,List<String> vendors,
                                        List<String> deviceTypes){
        List<List<String>> result=new ArrayList<>();
        engineUrl="http://www.baidu.com/s?wd="+engineUrl;
        System.out.println("*********************************************************"+engineUrl);
        Rule rule=new Rule("","",0);
        try {
            System.out.println("*********************************************************"+engineUrl);
            HttpClient httpClient= HttpClients.createDefault();
            HttpGet httpGet=new HttpGet(engineUrl);
            httpGet.setHeader("Accept","application/json, text/plain, */*");
            httpGet.setHeader("Accept-Encoding", "gzip, deflate, sdch, br");
            httpGet.setHeader("Accept-Language", "zh-CN,zh;q=0.8");
            httpGet.setHeader("Cookie","BIDUPSID=417798EA8DC8E1C28571BF3AE0123FE4; PSTM=1579598757; BD_UPN=123253; BDORZ=B490B5EBF6F3CD402E515D22BCDA1598; BAIDUID=0D5AE9A68BB34C1745E9C4ED3D266072:FG=1; MCITY=-%3A; BDUSS=BVdkhoazgyaVVUVEVqREM1bjBvaXhsTllzU3FBfll1eHpRU2hzR2ZDZVVneHRmRVFBQUFBJCQAAAAAAAAAAAEAAAAZNrxDdXN0YnpmejAwOTkAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAJT2816U9vNeQz; BDSFRCVID=yx8OJeC62AzId8crWYsFhjKQ5XfAK7RTH6f3sKmn9eC9t38-pZcaEG0Pox8g0KubhobuogKKymOTHuFF_2uxOjjg8UtVJeC6EG0Ptf8g0f5; H_BDCLCKID_SF=tR4e_IK5JDt3fP36q6OfhP-yqxbXqM7g3eOZ0l8Ktf_aOt54MRK2bMnbMM6iQ6LH3D8O3fjmWIQthn4lWxvKMpjb3H7Ob45CJjb4KKJx-lCWeIJo5DcP2ftZhUJiBbcLBan7LlQIXKohJh7FM4tW3J0ZyxomtfQxtNRJ0DnjtpChbCD6ej0bjj5M-Uvy-40XbITX3b58bTrjDnCrhh5dXUI82h5y05JdL2utQDO_LUOsjlvhjfvSbfcWhRORXx74-RCHhT6C5KOFMhjKXRAVbxL1Db3J2-ox5TTdsR7yfp5oepvoD-Jc3MvByPjdJJQOBKQB0KnGbUQkeq8CQft20b0EeMtjW6LEJJKqoDPbJCvhDRTvhCcjh-FSMgTBKI62aKDs-nToBhcqEIL4jloo-4_qKRJK0fJ32e3I_RjjQfAWOxbSj4Qo5Tk_LlbmhTb9tT6ghq6dLl5nhMJFb67JDMPFqHoAqRQy523iXR6vQpnhslQ3DRoWXPIqbN7P-p5Z5mAqKl0MLPbtbb0xXj_0D6QyjH8tJ60sb5vfsJTEab7jKROvhjRo5b0gyxomtjjOBan75Rc-QbcFqRnFypLhXqtmLtteLUkqKCOChJrCLl6GO-LRh-nVebcWQttjQPThfIkja-5tMJOUqR7TyURBhf47yhol0q4Hb6b9BJcjfU5MSlcNLTjpQT8r5MDOK5OuJRLH_ID5JCLBbDv65nt_e5vbKh3JetJyaR38XInvWJ5TMCoq2xrfe-tX3nre0tvBWaCthnQpWK3CShPCb6b5yPuHefomqR0efPjjhJOq3l02Vb79e-t2ynLVMMna-4RMW238Wl7mWP5PsxA45J7cM4IseboJLfT-0bc4KKJxbnLWeIJIjj6jK4JKjG-qtfK; delPer=0; BD_CK_SAM=1; PSINO=1; BD_HOME=1; H_PS_PSSID=32216_1441_31254_32046_32231_32116_31322_26350_32261; bdindexid=dl5k2c9f91sc0rlmq1ul6ifjd4; COOKIE_SESSION=23_2_8_7_2_24_0_1_8_3_0_4_0_0_0_0_1594621357_1594621348_1595063960%7C9%23189899_282_1594621363%7C9; BDRCVFR[feWj1Vr5u3D]=mk3SLVN4HKm; RT=\"z=1&dm=baidu.com&si=s163v6bkv2l&ss=kcsg5jsd&sl=8&tt=8g4&bcn=https%3A%2F%2Ffclog.baidu.com%2Flog%2Fweirwood%3Ftype%3Dperf&ld=a6h7&ul=exoe&hd=ey43\"; sugstore=1; H_PS_645EC=34a9Cb0UbnZmtpfVrSaRejNRMLGdp2setHvYGZZu0IY%2FO59Lnq2FNZ0guazNcyW%2BJIK3");
            httpGet.setHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; rv:6.0.2) Gecko/20100101 Firefox/6.0.2");
            HttpResponse response = httpClient.execute(httpGet);

            //StringBuilder htmlString=new StringBuilder();
            String html="";
            if(response.getStatusLine().getStatusCode()==200){
                html= EntityUtils.toString(response.getEntity(),"gbk");
            }
            Document doc= Jsoup.parse(html);
            List<String> pageUrl=new ArrayList<>();
            List<String> visitUrl=new ArrayList<>();
            Elements pages=doc.select("div[class=page-inner]");
            for(Element t:pages){
                Elements temp=t.select("a");
                for(Element a:temp){
                    //System.out.println(a.attr("href"));
                    pageUrl.add(a.attr("href"));
                }
            }

//            for(String purl:pageUrl)
//                System.out.println(purl);
            AprioriRule aprioriRule=new AprioriRule();
            rule=aprioriRule.genRules(pageUrl,vendors,deviceTypes,device);
//            for(int i=0;i<3&&i<pageUrl.size();i++){
//                HttpGet httpGetTemp=new HttpGet("https://www.baidu.com"+pageUrl.get(i));
//                httpGetTemp.setHeader("Accept", "text/html,application/xhtml+xml," +
//                        "application/xml;q=0.9,image/webp,*/*;q=0.8");
//                httpGetTemp.setHeader("Accept-Encoding", "gzip, deflate, sdch, br");
//                httpGetTemp.setHeader("Accept-Language", "zh-CN,zh;q=0.8");
//                httpGetTemp.setHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; rv:6.0.2) Gecko/20100101 Firefox/6.0.2");
//                response=httpClient.execute(httpGetTemp);
//                String htmlTemp="";
//                if(response.getStatusLine().getStatusCode()==200){
//                    htmlTemp= EntityUtils.toString(response.getEntity(),"gbk");
//                }
//                //System.out.println(pageUrl.get(i));
//                if(!htmlTemp.equals("")){
//                    Document docTemp=Jsoup.parse(htmlTemp);
//                    Elements h3=doc.select("h3[class=t]");
//                    for(Element t:h3){
//                        Elements temp=t.select("a");
//                        visitUrl.add(temp.get(0).attr("href"));
//                        //System.out.println(temp.get(0).attr("href"));
//                    }
//                }
//            }
//            AprioriRule aprioriRule=new AprioriRule();
//            for(String url:visitUrl)
//                System.out.println(url);
//
//            aprioriRule.genRules(visitUrl,vendors,deviceTypes);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return rule;
    }
    public static void main(String[] args){
        AreWay test=new AreWay();
//        test.AreEntrance("@PJL INFO ID HP LaserJet M106w");
        test.AreEntrance("Linux, HTTP/1.1, DIR-850L Ver 1.14WW");
    }
}
