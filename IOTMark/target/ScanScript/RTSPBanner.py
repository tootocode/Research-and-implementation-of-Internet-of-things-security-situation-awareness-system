import socket
import sys
import time
import select
import re

DESCRIBEPACKET = ""
OPTIONSPACKET = ""
TIMEOUT = 10

import os
def create_describe_packet(IP):
    global DESCRIBEPACKET
    if len(DESCRIBEPACKET) <= 0:
        DESCRIBEPACKET = 'DESCRIBE rtsp://%s RTSP/1.0\r\n' % IP
        DESCRIBEPACKET += 'CSeq: 2\r\n'
    return DESCRIBEPACKET


def create_test_packet1(IP):
    return create_describe_packet(IP) + "\r\n"


def test_describe(dst, filepath):
    if os.path.exists(filepath):
        os.remove(filepath)
    pkt = create_test_packet1(dst[0])
    try:
        s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        s.settimeout(TIMEOUT)
        s.connect(dst)
        s.sendall(str.encode(pkt))
        data = s.recv(1024)
        data = ''.join(map(chr, data))
        Server = ''
        CSeq = ''
        digest_realm = ''
        basic_realm=''
        banner = []
        raw = ''
        print(data)
        for info in data.split("\n"):
            if info !="\n":
                if re.search("Server:", info) != None:
                    Server = info.split(":")[1].replace("\n","")
                    Server = info.split(":")[1].replace("\r", "")

                elif re.search("Digest realm", info) != None:
                    start=None
                    firstQuo=False
                    for m in re.finditer("realm",info):
                        start=m
                        break
                    endpos=None
                    for i in range(start.end()+1,len(info)):
                        if info[i]=="\"":
                            if firstQuo==False:
                                firstQuo=True
                            else:
                                endpos=i
                                break
                    digest_realm=info[start.end()+1:endpos+1]
                elif re.search("CSeq:", info) != None:
                    CSeq = info.split(":")[1]
                elif re.search("Basic realm",info)!=None:
                    start = None
                    firstQuo = False
                    for m in re.finditer("realm", info):
                        start = m
                        break
                    endpos = None
                    for i in range(start.end() + 1, len(info)):
                        if info[i] == "\"":
                            if firstQuo == False:
                                firstQuo = True
                            else:
                                endpos = i
                                break
                    basic_realm = info[start.end() + 1:endpos + 1]
                elif re.search("RTSP",info):
                    temp=info.split(" ")
                    for i in range(0,len(temp)-1):
                        raw=raw+temp[i]+" "
                    len2=len(temp[len(temp)-1])
                    raw=raw+temp[len(temp)-1][0:len2-1]
        with open(filepath,"w") as f:
            writeline = "{\"ip\":\"" + dst[
                0] + "\"," + "\"Server\":\"" + Server + "\"," + "\"digest_realm\":\"" + digest_realm + "\"," + "\"Banner\":\"" + \
                        raw + "\"," + "\"basic_realm\":" + basic_realm +","+\
                        "\"Protocol\":\"RTSP\",\"Marked\":\"true\"}"
            print(writeline)
            f.write(writeline)
            f.close()
    except Exception:
        print(Exception)

if __name__ == '__main__':
    #test_describe(("191.23.125.162", 554), "./result.txt")
    if len(sys.argv)>1:
        test_describe((sys.argv[2],554),sys.argv[1])
