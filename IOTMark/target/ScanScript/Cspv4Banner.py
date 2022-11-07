import socket
import sys
import os
def Cspv4Banner(dst,filepath):
    if os.path.exists(filepath):
        os.remove(filepath)
    socket.setdefaulttimeout(5)
    sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)

    try:
        sock.connect(dst)
    except:
        return '', -1

    Testfr=[
        0x01,0x01,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,
        0x04,0x00,0x05,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,
        0x00,0x00
    ]
    sock.send(bytes(Testfr))
    recv=sock.recv(8192)
    print(recv)
    if recv[0]==0x02:
        with open(filepath,'a+') as f:
            writeline="{\"ip\":\""+dst[0]+":2222\","+"\"Device\":\"Cspv4\","+"\"Vendor\":\"\","+"\"DeviceType\":\"PLC\","+\
                "\"Protocol\":\"Cspv4\",\"Marked\":\"true\"}\n"
            f.write(writeline)
            f.close()
        sock.close()
    else:
        sock.close()

if __name__=="__main__":
    if len(sys.argv) > 1:
        Cspv4Banner((sys.argv[2],2222),sys.argv[1])