import socket
import sys
import os
def iec_60870_5_104Banner(dst,filepath):
    if os.path.exists(filepath):
        os.remove(filepath)
    socket.setdefaulttimeout(5)
    sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    #sock.setsockopt(socket.SOL_SOCKET, socket.SO_RCVTIMEO, struct.pack('ii', int(2), 0))  # 2 sec timeout
    sock.connect(dst)

    Testfr = [
        # iec 104 apci layer
        0x68,  # start
        0x04,  # APDU len
        0x43,  # type 0100 0011
        0x00, 0x00, 0x00  # padding
    ]
    sock.send(bytes(Testfr))
    recv = sock.recv(8192)
    print(recv)
    if recv[0]==0x68 :
        if recv[2]==0x83:
            with open(filepath,'a+') as f:
                writeline="{\"ip\":\""+dst[0]+":2404\","+"\"DeviceType\":\"PLC\","+"\"Device\":\"iec_60870_5_104\","+\
                          "\"Protocol\":\"IEC 60870-5-104\",\"Marked\":\"true\"}\n"
                f.write(writeline)
                f.close()
            sock.close()
        else:
            sock.close()
    else:
        sock.close()
    #留待以后可以添加的内容
    # Testfr3=[
    #     # iec 104 apci layer
    #     0x68,  # start
    #     0x0e,  # apdu len
    #     0x00, 0x00,  # type + tx
    #     0x00, 0x00,  # rx
    #
    #     # iec 104 asdu layer
    #     0x64,  # type id: C_IC_NA_1, interrogation command
    #     0x01,  # numix
    #     0x06,  # some stuff
    #     0x00,  # OA
    #     0xff, 0xff,  # addr 65535
    #     0x00,  # IOA
    #     0x00, 0x00, 0x00# 0x14
    # ]
    # sock.send(bytes(Testfr3))
    # recv = sock.recv(8192)
    # count=0
    # for item in recv:
    #     print(item,count)
    #     count=count+1

if __name__=='__main__':
    if len(sys.argv)>1:
        iec_60870_5_104Banner((sys.argv[2],2404),sys.argv[1])
