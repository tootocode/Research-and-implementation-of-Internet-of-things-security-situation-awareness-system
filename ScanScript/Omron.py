import socket
import sys
import os
def Omron(dst,filepath):
    if os.path.exists(filepath):
        os.remove(filepath)
    socket.setdefaulttimeout(10)
    sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    try:
        sock.connect(dst)
    except:
        print("无法连接")
        return '', -1
    req_addr=[
        0x46,0x49,0x4e,0x53,0x00,0x00,0x00,0x0c,
        0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,
        0x00,0x00,0x00,0x00
    ]
    sock.send(bytes(req_addr))
    recv=sock.recv(8192)

    controller_data_read=[
        0x46,0x49,0x4e,0x53,0x00,0x00,0x00,
        0x15,0x00,0x00,0x00,0x02,0x00,0x00,
        0x00,0x00,0x80,0x00,0x02,0x00
    ]

    controller_data_read2=[
        0x00,0x00,0x00,0xef,0x05,0x05,0x01
    ]

    if recv[0]==0x46:
        address=recv[23]
        send_data=[]
        send_data.extend(controller_data_read)
        send_data.append(address)
        send_data.extend(controller_data_read2)
        send_data.append(0x00)
        sock.send(bytes(send_data))
        recv=sock.recv(8192)
        Model=''.join(map(chr,recv[30:55]))
        Model=Model.replace(chr(0),"")
        Version=''.join(map(chr,recv[60:65]))
        #print(Model)

        with open(filepath,'a+') as f:
            writeline="{\"ip\":\""+dst[0]+":9600\","+"\"Device\":\""+Model+"\","+"\"DeviceType\":\"PLC\","+"\"Vendor\":\"Omron\","+\
                      "\"Protocol\":\"Omron\","+"\"Marked\":\"true\"}"
            f.write(writeline)
            f.close()
        sock.close()
        print(writeline)
    sock.close()

if __name__=="__main__":
    #Omron(("210.253.251.3",9600),"")
    if len(sys.argv)>1:
        Omron((sys.argv[2],9600),sys.argv[1])