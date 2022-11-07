import socket
import os
import array
import sys

def ModBus(dst,filepath):
    if os.path.exists(filepath):
        os.remove(filepath)
    senddata=[0x00,0x00,0x00,0x00,0x00,0x02,0x00,0x00]
    s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    s.settimeout(5)
    s.connect(dst)

    for sid in range(0,16):
        senddata[6]=sid
        s.send(bytes(senddata))
        data=s.recv(1024)
        print(data)
        if data:
            resp = array.array('B')
            resp.frombytes(data)
            with open(filepath,'a+') as f:
                writeline="{\"ip\":\""+dst[0]+":502\","+"\"DeviceType\":\"PLC\","+"\"Vendor\":\"Modbus\","+"\"Device\":\"\","+\
                          "\"Protocol\":\"Modbus\",\"Marked\":\"true\"}"
                f.write(writeline)
                f.close()
            s.close()
            break

if __name__=='__main__':
    # ModBus(("129.2.27.68",502),"")
    if len(sys.argv)>1:
        ModBus((sys.argv[2],502),sys.argv[1])



