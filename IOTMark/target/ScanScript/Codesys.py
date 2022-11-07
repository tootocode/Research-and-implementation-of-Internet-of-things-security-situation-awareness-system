import socket
import sys
import os
def CodesysBanner(dst,filepath):
    if os.path.exists(filepath):
        os.remove(filepath)
    socket.setdefaulttimeout(5)
    sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    try:
        sock.connect(dst)
    except:
        return '', -1
    Testfr = [
        0xbb,0xbb,0x01,0x00,0x00,0x00,0x01
    ]
    sock.send(bytes(Testfr))
    recv = sock.recv(8192)
    if(recv[0]==0xbb):
        os_name=''.join(map(chr,recv[64:95]))
        os_type=''.join(map(chr,recv[96:127]))
        product_type=''.join(map(chr,recv[128:167]))
        print(os_name)
        print(os_type)
        print(type(product_type))

        with open(filepath,'a+') as f:
            writeLine="{\"ip\":\""+dst[0]+":2455\","+"\"DeviceType\":\"PLC\","+"\"Vendor\":\"Codesys\","+"\"Device\":\""+product_type.replace(chr(0),"")+"\","+"\"Protocol\":\"CodeSys\",\"Marked\":\"true\"}"+"\n"
            print(writeLine)
            f.write(writeLine)
            f.close()
    else:
        sock.close()

if __name__=='__main__':
    if len(sys.argv)>1:
        CodesysBanner((sys.argv[2],2455),sys.argv[1])
    #CodesysBanner(("78.138.143.102",2455),"/Users/ludam/Desktop/BannerResult/2455Banner.txt")