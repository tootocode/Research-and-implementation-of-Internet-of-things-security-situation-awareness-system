package BannerInfo;

import java.util.HashMap;
import java.util.Map;

public class GetBannerFactory {
    private static Map<String, GetBanner> RecordMap=new HashMap<String, GetBanner>();
    static {
        try {
            RecordMap.put("47808",new BacnetBanner());
            RecordMap.put("2455",new CodesysBanner());
            RecordMap.put("789",new Cr3Banner());
            RecordMap.put("2222",new Cspv4Banner());
            RecordMap.put("20000",new Dnp3Banner());
            RecordMap.put("44818",new EnipBanner());
            RecordMap.put("1911",new FoxBanner());
            RecordMap.put("21",new FTPBanner());
            RecordMap.put("18245",new GeBanner());
            RecordMap.put("80",new Http80Banner());
            RecordMap.put("2404",new Iec_5_104Banner());
            RecordMap.put("5007",new MelsecqBanner());
            RecordMap.put("25565",new MinecraftBanner());
            //RecordMap.put("102",new MmsBanner());
            RecordMap.put("4800",new MoxaBanner());
            RecordMap.put("1962",new PcworxBanner());
            RecordMap.put("20547",new ProconosBanner());
            RecordMap.put("102",new S7Banner());
            RecordMap.put("23",new TelnetBanner());
            RecordMap.put("22",new SSHBanner());
            RecordMap.put("554",new RTSPBanner());
            RecordMap.put("161",new SnmpBanner());
            RecordMap.put("3702",new OnvifBanner());
            RecordMap.put("8080",new Http8080Banner());
            RecordMap.put("443",new Http443Banner());
            RecordMap.put("9100",new PJLBanner());
            RecordMap.put("8443",new Http8443Banner());
            RecordMap.put("9600",new OmronBanner());
            RecordMap.put("502",new ModbusBanner());
            RecordMap.put("4911",new FoxBanner());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static GetBanner get(String port){return RecordMap.get(port);}
}
