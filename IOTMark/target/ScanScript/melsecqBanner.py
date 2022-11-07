import socket
import sys
import os
def melsecqBanner(dst,filepath):
    if os.path.exists(filepath):
        os.remove(filepath)
    socket.setdefaulttimeout(5)
    sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    # sock.setsockopt(socket.SOL_SOCKET, socket.SO_RCVTIMEO, struct.pack('ii', int(2), 0))  # 2 sec timeout
    try:
        sock.connect(dst)
    except:
        return '', -1
    Testfr = [
        0x57,0x00,0x00,0x00,0x00,0x11,0x11,0x07,0x00,0x00,0xff,0xff,0x03,0x00,0x00,0xfe,0x03,
        0x00,0x00,0x14,0x00,0x1c,0x08,0x0a,0x08,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x04,0x01,0x01,
        0x01,0x00,0x00,0x00,0x00,0x01
    ]
    sock.send(bytes(Testfr))
    recv = sock.recv(8192)
    if recv[0]==0xd7 :
        cpuinfo=''.join(map(chr,recv[41:57]))
        #print(ord(cpuinfo[15]))

        with open(filepath,'a+') as f:
            writeline="{\"ip\":\""+dst[0]+":5007\","+"\"DeviceType\":\"PLC\","+"\"Vendor\":\"Melsec-q\","+"\"Device\":\"Melsec-q\","+"\"cpuinfo\":\""+cpuinfo.replace(chr(32),"")+\
                "\","+"\"Protocol\":\"MELSEC-Q\",\"Marked\":\"true\"}\n"
            f.write(writeline)
            f.close()
        sock.close()

if __name__ == '__main__':
    if len(sys.argv) > 1:
        melsecqBanner((sys.argv[2], 5007),sys.argv[1])