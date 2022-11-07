package BannerInfo;

import org.springframework.util.ResourceUtils;

import java.io.*;

public class PcworxBanner extends AbstractGetBanner {
    @Override
    public String getbanner(String ip, String port) {
        String output_file=this.output_file+port+"Banner.txt";
        String script_path=this.script_path+"Pcworx.py";
        String result="";
        try{
            String tempcmd=this.cmd2+script_path+" "+output_file+" "+ip;
            System.out.println(tempcmd);
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

        }catch (Exception e){
            e.printStackTrace();
        }
        System.out.println(result);
        return result;
    }
    public static void main(String[] args){
        PcworxBanner pcworxBanner=new PcworxBanner();
        pcworxBanner.getbanner("95.194.95.98","1962");
    }
}
