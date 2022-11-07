import java.io.*;
import java.util.regex.Pattern;

public class scanConfig {
    public boolean MetasploitTest;
    public boolean TLSTest;
    public scanConfig(String path){
        File file=new File(path);
        try {
            String line="";
            InputStreamReader inputStreamReader=new InputStreamReader(new FileInputStream(file),"UTF-8");
            BufferedReader bufferedReader=new BufferedReader(inputStreamReader);
            while((line=bufferedReader.readLine())!=null){
                String ipContent=".*Metasploit.*";
                if(Pattern.matches(ipContent,line)){
                    String[] temp=line.split(":");
                    if(temp[1].equals("true"))
                        MetasploitTest=true;
                    else
                        MetasploitTest=false;
                }
                String tlsContent=".*TLSTest.*";
                if(Pattern.matches(tlsContent,line)){
                    String[] temp=line.split(":");
                    if(temp[1].equals("true"))
                        TLSTest=true;
                    else
                        TLSTest=false;
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
    public static void main(String[] argv){
        scanConfig scanconfig=new scanConfig("./Config");
        System.out.println(scanconfig.MetasploitTest);
        System.out.println(scanconfig.TLSTest);
    }
}
