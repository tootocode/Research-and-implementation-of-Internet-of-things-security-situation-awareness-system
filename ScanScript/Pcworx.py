import socket
import sys
import os
def PcworxBanner(dst,filepath):
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
        0x01,0x01,0x00,0x1a,0x00,0x00,0x00,0x00,0x78,0x80,0x00,0x03,0x00,0x0c,
        0x49,0x42,0x45,0x54,0x48,0x30,0x31,0x4e,0x30,0x5f,0x4d,0x00
    ]
    temp = ''.join(map(chr, Testfr))
    sock.send(bytes(Testfr))
    recv = sock.recv(8192)
    sid=recv[17]

    init_comms2=[
        0x01,0x05,0x00,0x16,0x00,0x01,0x00,0x00,0x78,0x80,0x00,sid,
        0x00,0x00,0x00,0x06,0x00,0x04,0x02,0x95,0x00,0x00
    ]

    sock.send(bytes(init_comms2))
    recv=sock.recv(8192)
    #print(recv)

    req_info=[
        0x01,0x06,0x00,0x0e,0x00,0x02,0x00,0x00,0x00,0x00,0x00,sid,0x04,0x00
    ]

    sock.send(bytes(req_info))
    recv=sock.recv(8192)
    if recv:
        PLC_Type=''.join(map(chr,recv[30:65]))
        Fireware_version=''.join(map(chr,recv[66:78]))
        print(PLC_Type)
        print(Fireware_version)
        with open(filepath,'a+') as f:
            writeline="{\"ip\":\""+dst[0]+":1962\","+"\"DeviceType\":\"PLC\","+"\"Vendor\":\"Pcworx\","+"\"Device\":\""+PLC_Type.strip(chr(0))+"\","+\
                      "\"Protocol\":\"Pcworx\",\"Marked\":\"true\"}"
            f.write(writeline)
            f.close()
            print(writeline)
        sock.close()
    sock.close()

    # count=0
    # for item in recv:
    #     print(item.to_bytes(length=2,byteorder='big',signed=True),count)
    #     count=count+1

if __name__=='__main__':
    if len(sys.argv)>1:
        PcworxBanner((sys.argv[2],1962),sys.argv[1])