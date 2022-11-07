import socket
def AtgBanner(dst):
    socket.setdefaulttimeout(5)
    sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)

    try:
        sock.connect(dst)
    except:
        return '', -1

    Testfr=[
        0x01
    ]
    command="I20100\n"
    command=command.encode('utf-8')
    Testfr.extend(command)
    sock.send(bytes(Testfr))
    recv=sock.recv(8192)
    if recv[0]==0x01 or recv[0]==0x0a:
        result=''.join(map(chr,recv[1:-2]))
        sock.close()
    else:
        sock.close()
if __name__=='__main__':
    AtgBanner(('',100001))
