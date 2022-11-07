import socket
import json
import sys
import os
PJL_START = "\033%-12345X@PJL "
PJL_FINISH = "\033%-12345X\r\n"
PJL_USTATUS = "USTATUS DEVICE="
PJL_INFO_ID = "INFO ID\r\n"

EOF = PJL_START + PJL_USTATUS + "OFF\r\n" + PJL_FINISH
DEVICEID = PJL_START + PJL_INFO_ID + PJL_FINISH

def getBanner(dst,filepath):
    if os.path.exists(filepath):
        os.remove(filepath)
    sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM, 0)
    sock.settimeout(5)
    try:
        sock.connect(dst)
        #sock.send(EOF)
        sock.send(DEVICEID)
        device = sock.recv(1024)
        with open(filepath,'w') as f:
            device=device.replace("\n"," ")
            device=device.replace("\r"," ")
            device=device.replace("\"","")
            print(device)
            writeline="{\"ip\":\""+dst[0]+":9100\","+"\"raw\":\""+device+"\","+"\"Protocol\":\"Printer Job language\"}"
            f.write(writeline)
    except:
        print ("Can't connect")
    #print("Connect Refuse")
    sock.close()

if __name__=='__main__':
    if len(sys.argv)>1:
        getBanner((sys.argv[2],9100),sys.argv[1])