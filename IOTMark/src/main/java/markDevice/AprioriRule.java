package markDevice;

import database.Query;
import database.QueryFactory;
import database.RuleInsert;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import javax.swing.text.Element;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AprioriRule {
    public Rule genRules(List<String> visitUrl,List<String> vendors,List<String> deviceTypes,String inform){
        List<Rule> rules=new ArrayList<>();
        HttpClient httpClient= HttpClients.createDefault();
        int count=0;
        for(String url:visitUrl){
            count++;
            HttpGet httpGet=new HttpGet("https://www.baidu.com"+url);
            httpGet.setHeader("Accept", "text/html,application/xhtml+xml," +
                    "application/xml;q=0.9,image/webp,*/*;q=0.8");
            httpGet.setHeader("Accept-Encoding", "gzip, deflate, sdch, br");
            httpGet.setHeader("Accept-Language", "zh-CN,zh;q=0.8");
            httpGet.setHeader("Cookie","BIDUPSID=417798EA8DC8E1C28571BF3AE0123FE4; PSTM=1579598757; BD_UPN=123253; BDORZ=B490B5EBF6F3CD402E515D22BCDA1598; BAIDUID=0D5AE9A68BB34C1745E9C4ED3D266072:FG=1; MCITY=-%3A; BDUSS=BVdkhoazgyaVVUVEVqREM1bjBvaXhsTllzU3FBfll1eHpRU2hzR2ZDZVVneHRmRVFBQUFBJCQAAAAAAAAAAAEAAAAZNrxDdXN0YnpmejAwOTkAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAJT2816U9vNeQz; BDSFRCVID=yx8OJeC62AzId8crWYsFhjKQ5XfAK7RTH6f3sKmn9eC9t38-pZcaEG0Pox8g0KubhobuogKKymOTHuFF_2uxOjjg8UtVJeC6EG0Ptf8g0f5; H_BDCLCKID_SF=tR4e_IK5JDt3fP36q6OfhP-yqxbXqM7g3eOZ0l8Ktf_aOt54MRK2bMnbMM6iQ6LH3D8O3fjmWIQthn4lWxvKMpjb3H7Ob45CJjb4KKJx-lCWeIJo5DcP2ftZhUJiBbcLBan7LlQIXKohJh7FM4tW3J0ZyxomtfQxtNRJ0DnjtpChbCD6ej0bjj5M-Uvy-40XbITX3b58bTrjDnCrhh5dXUI82h5y05JdL2utQDO_LUOsjlvhjfvSbfcWhRORXx74-RCHhT6C5KOFMhjKXRAVbxL1Db3J2-ox5TTdsR7yfp5oepvoD-Jc3MvByPjdJJQOBKQB0KnGbUQkeq8CQft20b0EeMtjW6LEJJKqoDPbJCvhDRTvhCcjh-FSMgTBKI62aKDs-nToBhcqEIL4jloo-4_qKRJK0fJ32e3I_RjjQfAWOxbSj4Qo5Tk_LlbmhTb9tT6ghq6dLl5nhMJFb67JDMPFqHoAqRQy523iXR6vQpnhslQ3DRoWXPIqbN7P-p5Z5mAqKl0MLPbtbb0xXj_0D6QyjH8tJ60sb5vfsJTEab7jKROvhjRo5b0gyxomtjjOBan75Rc-QbcFqRnFypLhXqtmLtteLUkqKCOChJrCLl6GO-LRh-nVebcWQttjQPThfIkja-5tMJOUqR7TyURBhf47yhol0q4Hb6b9BJcjfU5MSlcNLTjpQT8r5MDOK5OuJRLH_ID5JCLBbDv65nt_e5vbKh3JetJyaR38XInvWJ5TMCoq2xrfe-tX3nre0tvBWaCthnQpWK3CShPCb6b5yPuHefomqR0efPjjhJOq3l02Vb79e-t2ynLVMMna-4RMW238Wl7mWP5PsxA45J7cM4IseboJLfT-0bc4KKJxbnLWeIJIjj6jK4JKjG-qtfK; delPer=0; BD_CK_SAM=1; PSINO=1; BD_HOME=1; H_PS_PSSID=32216_1441_31254_32046_32231_32116_31322_26350_32261; bdindexid=dl5k2c9f91sc0rlmq1ul6ifjd4; COOKIE_SESSION=23_2_8_7_2_24_0_1_8_3_0_4_0_0_0_0_1594621357_1594621348_1595063960%7C9%23189899_282_1594621363%7C9; BDRCVFR[feWj1Vr5u3D]=mk3SLVN4HKm; RT=\"z=1&dm=baidu.com&si=s163v6bkv2l&ss=kcsg5jsd&sl=8&tt=8g4&bcn=https%3A%2F%2Ffclog.baidu.com%2Flog%2Fweirwood%3Ftype%3Dperf&ld=a6h7&ul=exoe&hd=ey43\"; sugstore=1; H_PS_645EC=34a9Cb0UbnZmtpfVrSaRejNRMLGdp2setHvYGZZu0IY%2FO59Lnq2FNZ0guazNcyW%2BJIK3");
            httpGet.setHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; rv:6.0.2) Gecko/20100101 Firefox/6.0.2");
            HttpResponse response = null;
            try {
                //System.out.println("https://www.baidu.com"+url);
                response = httpClient.execute(httpGet);
                String html="";
                if(response.getStatusLine().getStatusCode()==200){
                    html= EntityUtils.toString(response.getEntity(),"UTF-8");
                    //System.out.println(html);
                }
                if(!html.equals("")){
                    Document doc= Jsoup.parse(html);
                    Elements h3=doc.select("h3[class=t]");
                    Elements div=doc.select("div[class=c-abstract]");
                    for(int i=0;i<h3.size();i++){
//                        for(Rule r:rules){
//                            System.out.println(r.getDevicetype());
//                            System.out.println(r.getVendor());
//                            System.out.println(r.getNum());
////                            System.out.println(h3.size());
//                        }
//                        System.out.println(h3.get(i).text());
//                        System.out.println(div.get(i).text());
                        List<String> searchVendor=new ArrayList<>();
                        List<String> searchDevicetype=new ArrayList<>();
                        for(String vendor:vendors){
                            Pattern pattern=Pattern.compile(".*"+vendor+".*");
                            Matcher matcher=pattern.matcher(h3.get(i).text());
                            if(matcher.find()){
                                //System.out.println(vendor);
                               searchVendor.add(vendor);
                            }else{
                                if(i<div.size()){
                                    matcher=pattern.matcher(div.get(i).text());
                                    if(matcher.find())
                                        searchVendor.add(vendor);
                                }
                            }
                        }
                        for(String deviceType:deviceTypes){
                            //System.out.println(deviceType);
                            Pattern pattern=Pattern.compile(".*"+deviceType+".*");
                            Matcher matcher=pattern.matcher(h3.get(i).text());
                            if(matcher.find()){
                                //System.out.println(deviceType);
                                searchDevicetype.add(deviceType);
                            }else{
                                if(i<div.size()){
                                    matcher=pattern.matcher(div.get(i).text());
                                    if(matcher.find())
                                        searchDevicetype.add(deviceType);
                                }
                            }
                        }
//                        System.out.println();
//                        System.out.println(searchDevicetype);
//                        System.out.println(searchVendor);
//                        System.out.println();
                        for(String vendor:searchVendor){
                            for(String devicetype:searchDevicetype){
//                                System.out.println(vendor);
//                                System.out.println(devicetype);
                                Rule rule=new Rule(vendor,devicetype,1);
                                boolean judge=false;
                                for(Rule r:rules){
//                                    System.out.println();
//                                    System.out.println(r.getDevicetype());
//                                    System.out.println(r.getVendor());
//                                    System.out.println(r.getNum());
//                                    System.out.println(vendor);
//                                    System.out.println(devicetype);
//                                    System.out.println();
                                    if(r.equals(rule)){
                                        judge=true;
//                                        System.out.println(r.getDevicetype());
//                                        System.out.println(r.getVendor());
                                    }
                                }
                                if(judge){
                                    for(Rule r:rules){
                                        if(r.equals(rule))
                                            r.addNum();
                                    }
                                }else
                                    rules.add(rule);
                            }
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(count==2)
                break;
        }
        rules.sort(new Comparator<Rule>() {
            @Override
            public int compare(Rule o1, Rule o2) {
                return o2.getNum() - o1.getNum();
            }
        });
        if(rules.size()>0){
            if(rules.get(0).getNum()>3){
//                System.out.println(rules.get(0).getVendor());
//                System.out.println(rules.get(0).getDevicetype());
                Query addRule= QueryFactory.get(RuleInsert.class);
                rules.get(0).Device=inform;
                addRule.addRecord(rules.get(0).getVendor(),rules.get(0).getDevicetype(),rules.get(0).Device);
                //System.out.println(rules.get(0).Device);
                return rules.get(0);
            }else
                return new Rule("","",0);
        } else
            return new Rule("","",0);
    }
}
