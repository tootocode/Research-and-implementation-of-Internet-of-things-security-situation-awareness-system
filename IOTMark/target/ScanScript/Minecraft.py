import socket
import sys
import os
def MinecraftBanner(dst,filepath):
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
        0x02,0x00,0x01,0x00
    ]
    try:
        sock.send(bytes(Testfr))
        recv = sock.recv(8192)
        if recv:
            with open(filepath,'a+') as f:
                writeline="{\"ip\":\""+dst[0]+":25565\","+"\"Vendor\":\"Minecraft\","+"\"DeviceType\":\"PLC\","+"\"Device\":\"\","+\
                          "\"Protocol\":\"Minecraft\",\"Marked\":\"true\"}"
                f.write(writeline)
                f.close()
        sock.close()
    except:
        sock.close()


if __name__ == '__main__':
    if len(sys.argv)>1:
        MinecraftBanner((sys.argv[2],25565),sys.argv[1])