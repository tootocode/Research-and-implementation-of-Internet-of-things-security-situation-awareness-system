import socket
import sys
from lxml import etree
import re
import os
def OnvifBanner(dst,filepath):
    if os.path.exists(filepath):
        os.remove(filepath)
    socket.setdefaulttimeout(20)
    sock = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
    try:
        data2 = '<?xml version="1.0" encoding="utf-8"?><Envelope xmlns:dn="http://www.onvif.org/ver10/network/wsdl" xmlns="http://www.w3.org/2003/05/soap-envelope"><Header><wsa:MessageID xmlns:wsa="http://schemas.xmlsoap.org/ws/2004/08/addressing">uuid:7e2f5505-97e5-4941-a726-987a7ebeaa60</wsa:MessageID><wsa:To xmlns:wsa="http://schemas.xmlsoap.org/ws/2004/08/addressing">urn:schemas-xmlsoap-org:ws:2005:04:discovery</wsa:To><wsa:Action xmlns:wsa="http://schemas.xmlsoap.org/ws/2004/08/addressing">http://schemas.xmlsoap.org/ws/2005/04/discovery/Probe</wsa:Action></Header><Body><Probe xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:xsd="http://www.w3.org/2001/XMLSchema" xmlns="http://schemas.xmlsoap.org/ws/2005/04/discovery"><Types>dn:NetworkVideoTransmitter</Types><Scopes /></Probe></Body></Envelope>'
        data1 = '<?xml version="1.0" encoding="utf-8"?><Envelope xmlns:tds="http://www.onvif.org/ver10/device/wsdl" xmlns="http://www.w3.org/2003/05/soap-envelope"><Header><wsa:MessageID xmlns:wsa="http://schemas.xmlsoap.org/ws/2004/08/addressing">uuid:1a77e761-9ca0-499a-a853-4c4b6c9528dd</wsa:MessageID><wsa:To xmlns:wsa="http://schemas.xmlsoap.org/ws/2004/08/addressing">urn:schemas-xmlsoap-org:ws:2005:04:discovery</wsa:To><wsa:Action xmlns:wsa="http://schemas.xmlsoap.org/ws/2004/08/addressing">http://schemas.xmlsoap.org/ws/2005/04/discovery/Probe<;/wsa:Action></Header><Body><Probe xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:xsd="http://www.w3.org/2001/XMLSchema" xmlns="http://schemas.xmlsoap.org/ws/2005/04/discovery"><Types>tds:Device</Types><Scopes /></Probe></Body></Envelope>'
        sock.sendto(data1.encode('utf-8'), dst)
        sock.sendto(data2.encode('utf-8'), dst)
        result = sock.recv(8192)
        print(result)
        xml1 = etree.XML(result)
        xmlres = etree.tostring(xml1, pretty_print=True)
        xmlres = xmlres.split("\n".encode())
        vendor=""
        device=""
        devicetype=""
        for line in xmlres:
            keys = re.findall("/name/".encode(), line)
            keys = str(keys)
            if len(keys) > 3:
                vendor_info = re.findall(r"\bname\S(.+?)onvif\b".encode(), line)
                vendor_info = vendor_info[0].decode()
                vendor=vendor_info
                product_info = re.findall(r"\bhardware\S(.+?)onvif\b".encode(), line)
                product_info =product_info[0].decode()
                device=product_info
                App_info = re.findall(r"\btype\S(.+?)onvif\b".encode(), line)
                App_info = App_info[0].decode()
                devicetype=App_info
                if len(App_info) < 4:
                    App_info = product_info
        writeline="{\"ip\":\""+dst[0]+":3702\","+"\"Vendor\":\""+vendor+"\","+"\"Device\":\""+device+"\","+\
                    "\"DeviceType\":\""+devicetype+"\","+"\"Protocol\":\"onvif"+"\",\"Marked\":\"true\"}"
        print(writeline)
        with open(filepath,'w') as f:
            f.write(writeline)
            f.close()
            sock.close()
    except Exception as e:
        sock.close()
        print(e)

if __name__=='__main__':
    if len(sys.argv) > 1:
        OnvifBanner((sys.argv[2],3702),sys.argv[1])
    #OnvifBanner(("218.72.250.146",3702),"")