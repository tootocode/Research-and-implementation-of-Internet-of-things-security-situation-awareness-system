import socket
import sys
import os
def MoxaBanner(dst,filepath):
    if os.path.exists(filepath):
        os.remove(filepath)
    server=''
    status=''
    device=''
    socket.setdefaulttimeout(5)
    sock = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
    try:
        sock.connect(dst)
    except:
        return '', -1
    Testfr = [
        0x01, 0x00, 0x00, 0x08, 0x00, 0x00, 0x00, 0x00
    ]
    sock.send(bytes(Testfr))
    recv = sock.recv(8192)
    print(recv)
    print(len(recv))
    if recv[0] == 0x81 and recv[1] == 0x00:
        temp = recv[4:19]
        Testfr2 = [
            0x16, 0x00, 0x00, 0x14
        ]
        Testfr3 = [
            0x10, 0x00, 0x00, 0x14
        ]
        Testfr2.extend(temp)
        Testfr3.extend(temp)
        print(Testfr3)
        try:
            sock.send(bytes(Testfr2))
            recv = sock.recv(8192)
            print(recv)
            if len(recv) > 0:
                if recv[0] == 0x96:
                    sock.send(bytes(Testfr3))
                    recv = sock.recv(8192)
                    print(recv)
                    if len(recv)>21:
                        server=''.join(map(chr,recv[20:30]))
                        print(server)
                        if recv[32]==0x00:
                            status="Fixed"
                        elif recv[32]==0x01:
                            status="Locked Fixed"
                    device='Moxa Device'
                    with open(filepath,'a+') as f:
                        writeline="{\"ip\":\""+dst[0]+":4800\","+"\"Device\":\""+server+"\","+\
                                "\"DeviceType\":\"PLC\","+"\"Vendor\":\"MOXA\","+\
                                  "\"Protocol\":\"MOXA\",\"Marked\":\"true\"}\n"
                        f.write(writeline)
                        f.close()
            sock.close()
        except:
            sock.close()
            return '',-1

if __name__=="__main__":
    MoxaBanner(("165.124.175.202",4800),"")
    # if len(sys.argv)>1:
    #     MoxaBanner((sys.argv[2],4800),sys.argv[1])