package markDevice;

import database.DeviceQuery;
import database.GeneralQuery;
import database.Query;
import database.QueryFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.process.Tokenizer;
import edu.stanford.nlp.process.TokenizerFactory;
import edu.stanford.nlp.process.CoreLabelTokenFactory;
import edu.stanford.nlp.process.DocumentPreprocessor;
import edu.stanford.nlp.process.PTBTokenizer;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.ling.SentenceUtils;
import edu.stanford.nlp.trees.*;
import edu.stanford.nlp.parser.lexparser.LexicalizedParser;

import javax.naming.directory.SearchControls;

public class SearchBase {
    public List<String> markDevice(String infostr){
        String[] infos=infostr.split("\\s+");
        String Vendor="";
        String DeviceType="";
        String Device="";
        List<String> res=new ArrayList<>();

        for(String info:infos){
            info=info.replace(";","");
            info=info.replace(",","");
            info=info.replace("}","");
            info=info.replace("{","");
            info=info.replace(".","");
            info=info.replace(":"," : ");
            info=info.replace("\n"," ");
            info=info.replace("\r"," ");
            Query Vendorquery= QueryFactory.get(GeneralQuery.class);
            boolean vendorquery=Vendorquery.query(info,"Vendors","vendor");
            if(vendorquery){
                Vendor=info;
                System.out.println("--"+Vendor);
                break;
            }
            //logger.info(info);
        }

        for(String info:infos){
            info=info.replace(";","");
            info=info.replace(",","");
            info=info.replace("}","");
            info=info.replace("{","");
            info=info.replace(".","");
            info=info.replace(":"," : ");
            info=info.replace("\n"," ");
            info=info.replace("\r"," ");
            Query DeviceTypequery= QueryFactory.get(GeneralQuery.class);
            boolean deviceTypequery=DeviceTypequery.query(info,"DeviceType","DeviceType");
            if(deviceTypequery){
                DeviceType=info;
                System.out.println("--"+DeviceType);
                break;
            }
        }

        for(String info:infos){
            info=info.replace(";","");
            info=info.replace(",","");
            info=info.replace("}","");
            info=info.replace("{","");
            info=info.replace(".","");
            info=info.replace(":"," : ");
            info=info.replace("\n"," ");
            info=info.replace("\r"," ");
            //System.out.println(info);
            Query deviceQuery= QueryFactory.get(DeviceQuery.class);
            List<String> devicequery=deviceQuery.query(Vendor,DeviceType,info,"Device");
//            System.out.println(devicequery.size());
            if(devicequery.size()==3){
                res=devicequery;  //change，原来注释
                Vendor=devicequery.get(0);
                DeviceType=devicequery.get(1);
                Device=devicequery.get(2);
//                System.out.println("---Device identification succeeded---");
//                System.out.println("Vendor : "+Vendor);
//                System.out.println("DeviceType : "+DeviceType);
//                System.out.println("Device : "+Device);
                return res;  //changs 原来没有
            }
            //logger.info(info);
        }
        if(!Vendor.equals("")||!DeviceType.equals("")||!Device.equals("")){
            res.add(Vendor);
            res.add(DeviceType);
            res.add(Device);
        }


//        String parserModel = "edu/stanford/nlp/models/lexparser/englishPCFG.ser.gz";
//
//        LexicalizedParser lp = LexicalizedParser.loadModel(parserModel);
//
//        String[] infotemp=infostr.split(" ");
//        List<CoreLabel> rawWords = SentenceUtils.toCoreLabelList(infotemp);
//
//        Tree taggedWords = (Tree)lp.apply(rawWords);

//        String arestr="";
//        for(Tree tagres:taggedWords.getChildrenAsList()){
//            for(int i=0;i<tagres.size();i++){
//                try {
//                    Tree temp=tagres.getChild(i);
//                    String label=temp.label().value();
//                    String word=temp.yield().get(0).value();
//                    if(label.startsWith("N")||label.startsWith("J"))
//                        arestr=arestr+word+" ";
//                    System.out.println(label);
//                    System.out.println(word);
//                }catch (Exception e){
//                    System.out.println();
//                }
//            }
//        }
//
//        System.out.println(arestr);
        if(Vendor.equals("")||DeviceType.equals("")||Device.equals("")){       //处理之前三个为空的情况
            System.out.println("---搜索处理---");
            AreWay aretest=new AreWay();
            List<Rule> areResult=aretest.AreEntrance(infostr);
            Collections.sort(areResult, new Comparator<Rule>() {
                @Override
                public int compare(Rule o1, Rule o2) {
                    return o2.getNum()-o1.getNum();
                }
            });
            if(areResult.size()>0){
                for (int i=0;i<areResult.size();i++){
                    System.out.println(areResult.get(0).getVendor()+areResult.get(0).getDevicetype()+areResult.get(0).Device);
                }
                if(areResult.get(0).getNum()>3){
                    List<String> t=new ArrayList<>();
                    t.add(areResult.get(0).getVendor());
                    t.add(areResult.get(0).getDevicetype());
//                    if (areResult.get(0).Device.equals("ready")){
//                        t.add(areResult.get(3).Device);
//                    }
//                    else if (!areResult.get(0).Device.equals("ready")){
//
//                    }
                    t.add(areResult.get(0).Device);
                    if(!t.get(0).equals("")&&!t.get(1).equals("")&&!t.get(2).equals(""))
                        res=t;
                }
            }

        }
        return res;
    }
    public static void main(String[] args){
        SearchBase test=new SearchBase();
//        test.markDevice("@PJL INFO ID HP LaserJet M106w");
        test.markDevice("Linux, HTTP/1.1, DIR-850L Ver 1.14WW");
    }
}
