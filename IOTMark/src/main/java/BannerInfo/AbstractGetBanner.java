package BannerInfo;

abstract public class AbstractGetBanner implements GetBanner{

    public String cmd="/home/ysf/go/src/github.com/zmap/zgrab2/zgrab2 ";
    public String input_file="/home/ysf/桌面/Project_service/input_file/";
    public String output_file="/home/ysf/桌面/Project_service/output_file/";
    public String cmd3="/home/ysf/go/src/github.com/zmap/zgrab/zgrab ";
    public String script_path="/home/ysf/桌面/Project_service/ScanScript/";

//    public String cmd="/home/go/src/github.com/zmap/zgrab2/zgrab2 ";
//    public String input_file="/input_file/";
//    public String output_file="/output_file/";
//    public String cmd3="/home/go/src/github.com/zmap/zgrab/zgrab ";
//    public String script_path="/ScanScript/";

    public String cmd2="python3 ";
    public String cmd4="python2 ";
//
//    public String input_file="/IOTMark/iotmarkIP/";
//    public String cmd="/gopath/src/github.com/zmap/zgrab2/zgrab2 ";
//    public String output_file="/IOTMark/bannerResult/";
//    public String script_path="/IOTMark/ScanScript/";
//    public String cmd3="/gopath/src/github.com/zmap/zgrab/zgrab ";

    public void getbanner(String port){
    }
}
