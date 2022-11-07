package com.example.demo.controller;

import com.example.demo.database.Query.*;
import com.example.demo.database.Record.DatabaseConfig;
import com.example.demo.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
@RestController
@CrossOrigin
public class datavController {
    @Autowired
    private DatabaseConfig databaseConfig;

    @GetMapping("/datav/digitalFlop")
    public List<datavdevicenum> getdatavdevicenum() throws SQLException {
        List<datavdevicenum> list=new ArrayList<>();
        String sql="select DeviceType,count(IOTMark.IP) as num from IOTMark group by DeviceType order by num desc";
        String mysqlPath="jdbc:mysql://"+databaseConfig.getMysqlip()+"/DeviceMark?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
        Connection connection= DriverManager.getConnection(mysqlPath,"root","123456");
        datavDevicenumQuery datavDevicenumQuery=new datavDevicenumQuery(connection);
        list=datavDevicenumQuery.queryRecord(sql);
        connection.close();
        return list;
    }

    @GetMapping("/datav/vendorRatio")
    public List<vendorratio> getdatavVendorratio() throws SQLException {
        List<vendorratio> list=new ArrayList<>();
        List<datavdevicenum> list1=new ArrayList<>();
        String sql1="select DeviceType,count(IOTMark.IP) as num from IOTMark group by DeviceType order by num desc";
        String sql2="select Vendor,count(IOTMark.IP) as num from IOTMark where DeviceType='";
        String mysqlPath="jdbc:mysql://"+databaseConfig.getMysqlip()+"/DeviceMark?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
        Connection connection= DriverManager.getConnection(mysqlPath,"root","123456");
        datavDevicenumQuery datavDevicenumQuery=new datavDevicenumQuery(connection);
        list1=datavDevicenumQuery.queryRecord(sql1);
        for(datavdevicenum datavdevicenum:list1){
            String tempsql=sql2+datavdevicenum.getDeviceType()+"' group by Vendor order by num desc";
            vendorRatioQuery vendorRatioQuery=new vendorRatioQuery(connection);
            list.add(vendorRatioQuery.queryRecord(tempsql,datavdevicenum.getDeviceType(),datavdevicenum.getNum()));
        }
        connection.close();
        //System.out.println(list);
        return list;
    }

    @GetMapping("/datav/countbugs")
    public List<listInfo> getBugRank() throws SQLException {
        List<listInfo> list=new ArrayList<>();
        List<String> tlsbugs=new ArrayList<>();
        List<bugInfo> temp=new ArrayList<>();
        tlsbugs.add("heartbleed");
        tlsbugs.add("css");
        tlsbugs.add("ticketbleed");
        tlsbugs.add("robot");
        tlsbugs.add("secure_renego");
        tlsbugs.add("crime");
        tlsbugs.add("breach");
        tlsbugs.add("sweet32");
        tlsbugs.add("freak");
        tlsbugs.add("logjam");
        tlsbugs.add("beast");
        tlsbugs.add("lucky13");
        tlsbugs.add("rc4");
        tlsbugs.add("poodle");
        tlsbugs.add("learn_long_session_live");
        tlsbugs.add("openssl_aes_ni");
        String sql="select count(*) as number from ALLVulTable where ";
        String mysqlPath="jdbc:mysql://"+databaseConfig.getMysqlip()+"/DeviceMark?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
        Connection connection= DriverManager.getConnection(mysqlPath,"root","123456");
        countNumberQuery countNumberQuery=new countNumberQuery(connection);
        for(String bug :tlsbugs){
            String tempsql=sql+bug+"='1'";
            int t=countNumberQuery.queryRecord(tempsql);
            bugInfo bugInfo=new bugInfo();
            bugInfo.setName(bug);
            bugInfo.setNum(t);
            temp.add(bugInfo);
        }

        Collections.sort(temp, new Comparator<bugInfo>() {
            @Override
            public int compare(bugInfo o1, bugInfo o2) {
                return o2.getNum()-o1.getNum();
            }
        });

        int i=0;

        while(i<temp.size()&&i<10){
            listInfo listInfo=new listInfo();
            listInfo.setRank(i+1);
            listInfo.setName(temp.get(i).getName());
            listInfo.setNum(temp.get(i).getNum());
            list.add(listInfo);
            i++;
        }
        //System.out.println(list);
        connection.close();
        return list;
    }

    @GetMapping("/datav/scrollboard")
    public List<devicetaskInfo> getInitlist() throws SQLException {
        List<devicetaskInfo> result=new ArrayList<>();
        String mysqlPath="jdbc:mysql://"+databaseConfig.getMysqlip()+"/DeviceMark?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
        Connection connection= DriverManager.getConnection(mysqlPath,"root","123456");
        scrollboardQuery scrollboardQuery=new scrollboardQuery(connection);
        result=scrollboardQuery.queryRecord();
        connection.close();
        return result;
    }

    @GetMapping("/datav/rosechart")
    public List<datavdevicenum> getRoschartData() throws SQLException {
        List<datavdevicenum> result=new ArrayList<>();
        String mysqlPath="jdbc:mysql://"+databaseConfig.getMysqlip()+"/DeviceMark?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
        Connection connection= DriverManager.getConnection(mysqlPath,"root","123456");
        datavdeviceQuery datavdeviceQuery=new datavdeviceQuery(connection);
        result=datavdeviceQuery.queryRecord();
        connection.close();
        return result;
    }

    @GetMapping("/datav/waterlevel")
    public datavwaterlevel getWaterLevelData() throws SQLException {
        int result=0;
        String mysqlPath="jdbc:mysql://"+databaseConfig.getMysqlip()+"/DeviceMark?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
        Connection connection= DriverManager.getConnection(mysqlPath,"root","123456");
        numQuery numQuery=new numQuery(connection);
        int num1=numQuery.queryRecord("select count(*) as num from ScanedIP");
        int num2=numQuery.queryRecord("select count(distinct(IP)) as num from IOTMark");
        //System.out.println(num1);
        //System.out.println(num2);
        datavwaterlevel datavwaterlevel=new datavwaterlevel();
        datavwaterlevel.setNum(num2);
        result=num2*100/num1;
        datavwaterlevel.setRatio(result);
        connection.close();
        return datavwaterlevel;
    }

}
