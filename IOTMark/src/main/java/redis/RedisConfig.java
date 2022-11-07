package redis;

import java.io.*;
import java.util.regex.Pattern;

public class RedisConfig {
    public String RedisIP;
    public RedisConfig(String path){
        File file=new File(path);
        try {
            //System.out.println(path);
            String line="";
            InputStreamReader inputStreamReader=new InputStreamReader(new FileInputStream(file),"UTF-8");
            BufferedReader bufferedReader=new BufferedReader(inputStreamReader);
            while((line=bufferedReader.readLine())!=null){
                String ipContent=".*RedisIP.*";
                if(Pattern.matches(ipContent,line)){
                    //System.out.println(line);
                    String[] temp=line.split(":");
                    this.RedisIP=temp[1];
                }
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //System.out.println(this.RedisIP);
    }
}