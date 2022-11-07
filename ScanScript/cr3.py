import socket
import sys
import os
def Cr3Banner(dst,filepath):
    if os.path.exists(filepath):
        os.remove(filepath)
    socket.setdefaulttimeout(5)
    sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    #sock.setsockopt(socket.SOL_SOCKET, socket.SO_RCVTIMEO, struct.pack('ii', int(2), 0))  # 2 sec timeout
    try:
        sock.connect(dst)
    except:
        return '', -1
    Testfr = [
        0x00,0x04,0x01,0x2b,0x1b,0x00
    ]
    sock.send(bytes(Testfr))
    recv = sock.recv(8192)
    Vendor=''.join(map(chr,recv[6:-1]))
    Model=[
        0x00,0x04,0x01,0x2a,0x1a,0x00
    ]
    sock.send(bytes(Model))
    recv = sock.recv(8192)
    Model=''.join(map(chr,recv[6:-1]))
    if(len(Vendor)!=0):
        print(Vendor)
        print(Model)
        with open(filepath,'a+') as f:
            writeline="{\"ip\":\""+dst[0]+":789\","+"\"DeviceType\":\"PLC\","+"\"Vendor\":\""+Vendor+"\","+"\"Device\":\""+\
                    Model+"\",\"Protocol\":\"Crimson V3"+"\",\"Marked\":\"true\"}"+"\n"
            f.write(writeline)
            f.close()
    sock.close()

if __name__=='__main__':
    if len(sys.argv) > 1:
        Cr3Banner((sys.argv[2],789),sys.argv[1])