from easysnmp import snmp_walk
import sys
import os
def getDeviceName(deviceIP,snmpCommunity,oid,filepath):
    if os.path.exists(filepath):
        os.remove(filepath)
    try:
        res = snmp_walk(oid, hostname=deviceIP, community=snmpCommunity, version=2)
        result=""
        for each in res:
            result=result+" "+each.value
            print(each.value)
        writeline = "{\"ip\":\"" + deviceIP + "\"," + "\"raw\":\"" + result + "\","+"\"Protocol\":\"Snmp\"}"
        print(writeline)
        with open(filepath,'w') as f:
            f.write(writeline)
            f.close()
    except Exception:
        print(Exception)



if __name__=='__main__':
    if len(sys.argv) > 1:
        getDeviceName(sys.argv[2],"public","sysDescr",sys.argv[1])
    #getDeviceName("216.24.174.190", "public", "sysDescr", "")


