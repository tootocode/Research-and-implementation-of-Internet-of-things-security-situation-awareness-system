import socket
import sys
import os
def ProconosBanner(dst,filepath):
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
        0xcc,0x01,0x00,0x0b,0x40,0x02,0x00,0x00,0x47,0xee
    ]
    sock.send(bytes(Testfr))
    recv = sock.recv(8192)
    if recv:
        PLC=''.join(map(chr,recv[44:76]))
        project_name=''.join(map(chr,recv[76:88]))
        with open(filepath,'a+') as f:
            writeline="{\"ip\":\""+dst[0]+":20547\","+"\"Device\":\""+PLC+"\","+"\"DeviceType\":\"PLC\","+"\"Vendor\":\"Proconos\","+\
                      "\"Protocol\":\"Proconos\",\"Marked\":\"true\"}"
            f.write(writeline)
            f.close()
        sock.close()
    sock.close()
    print(recv[12:44])
    print(recv[44:76])
    print(recv[76:88])
    print(recv[88:100])

if __name__=='__main__':
    if len(sys.argv)>1:
        ProconosBanner((sys.argv[2],20547),sys.argv[1])