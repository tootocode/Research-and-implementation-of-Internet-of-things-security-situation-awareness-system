package queue;

import java.io.*;
import java.util.regex.Pattern;

public class ActivemqConfig {
    public String ActiveMqIP;
    public ActivemqConfig(String path){
        File file=new File(path);
        try {
            String line="";
            InputStreamReader inputStreamReader=new InputStreamReader(new FileInputStream(file),"UTF-8");
            BufferedReader bufferedReader=new BufferedReader(inputStreamReader);
            while((line=bufferedReader.readLine())!=null){
                String ipContent=".*Activemq.*";
                if(Pattern.matches(ipContent,line)){
                    String[] temp=line.split(":");
                    this.ActiveMqIP=temp[1];
                }
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
