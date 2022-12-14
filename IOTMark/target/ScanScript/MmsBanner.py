import socket
import struct
import sys
import os
def mmsBanner(dst,filepath):
    if os.path.exists(filepath):
        os.remove(filepath)
    socket.setdefaulttimeout(5)
    sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    try:
        sock.connect(dst)
    except:
        return '', -1
    CR_TPDU = [
        # tpkt
        0x03,  # version
        0x00,  # reserved
        # 0x00, 0x16, # length
        0x00, 0x0b,  # length

        # iso cotp
        # 0x11, # length
        0x06,  # length
        0xe0,  # pdu type: CR
        0x00, 0x00,  # destination reference
        0x00, 0x01,  # source reference
        0x00,  # class
    ]

    sock.send(bytes(CR_TPDU))
    recv=sock.recv(8192)

    MMS_INITIATE = [
        # tpkt
        0x03,  # version
        0x00,  # reserved
        0x00, 0xc5,  # length

        # iso 8073 COTP
        0x02,  # length
        0xf0,  # PDU type: DT data
        0x80,  # bin(int('0x80', 16))[2:]

        # iso 8327-1 OSI Session protocol
        0x0d,  # CONNECT CN spdu
        0xbc,  # length
        0x05, 0x06, 0x13,
        0x01, 0x00, 0x16, 0x01, 0x02, 0x14, 0x02, 0x00,
        0x02, 0x33, 0x02, 0x00, 0x01, 0x34, 0x02, 0x00,
        0x02, 0xc1, 0xa6,

        # iso 8823 osi presentation protocol
        0x31, 0x81, 0xa3, 0xa0, 0x03,
        0x80, 0x01, 0x01, 0xa2, 0x81, 0x9b, 0x80, 0x02,
        0x07, 0x80, 0x81, 0x04, 0x00, 0x00, 0x00, 0x01,
        0x82, 0x04, 0x00, 0x00, 0x00, 0x02, 0xa4, 0x23,
        0x30, 0x0f, 0x02, 0x01, 0x01, 0x06, 0x04, 0x52,
        0x01, 0x00, 0x01, 0x30, 0x04, 0x06, 0x02, 0x51,
        0x01, 0x30, 0x10, 0x02, 0x01, 0x03, 0x06, 0x05,
        0x28, 0xca, 0x22, 0x02, 0x01, 0x30, 0x04, 0x06,
        0x02, 0x51, 0x01, 0x88, 0x02, 0x06, 0x00, 0x61,
        0x60, 0x30, 0x5e, 0x02, 0x01, 0x01, 0xa0, 0x59,

        # iso 8650-1 osi association control service
        0x60, 0x57, 0x80, 0x02, 0x07, 0x80, 0xa1, 0x07,
        0x06, 0x05, 0x28, 0xca, 0x22, 0x01, 0x01, 0xa2,
        0x04, 0x06, 0x02, 0x29, 0x02, 0xa3, 0x03, 0x02,
        0x01, 0x02, 0xa6, 0x04, 0x06, 0x02, 0x29, 0x01,
        0xa7, 0x03, 0x02, 0x01, 0x01, 0xbe, 0x32, 0x28,
        0x30, 0x06, 0x02, 0x51, 0x01, 0x02, 0x01, 0x03,
        0xa0, 0x27,

        0xa8, 0x25, 0x80, 0x02, 0x7d, 0x00,
        0x81, 0x01, 0x14, 0x82, 0x01, 0x14, 0x83, 0x01,
        0x04, 0xa4, 0x16, 0x80, 0x01, 0x01, 0x81, 0x03,
        0x05, 0xfb, 0x00, 0x82, 0x0c, 0x03, 0x6e, 0x1d,
        0x00, 0x00, 0x00, 0x00, 0x00, 0x64, 0x00, 0x01,
        0x98
    ]

    sock.send(bytes(MMS_INITIATE))
    recv=sock.recv(8192)

    MMS_IDENTIFY = [

        0x03, 0x00, 0x00, 0x1b, 0x02, 0xf0, 0x80, 0x01,
        0x00, 0x01, 0x00, 0x61, 0x0e, 0x30, 0x0c, 0x02,
        0x01, 0x03, 0xa0, 0x07, 0xa0, 0x05, 0x02, 0x01,
        0x01, 0x82, 0x00
    ]

    sock.send(bytes(MMS_IDENTIFY))
    r=sock.recv(8192)
    if r:
        mms = r[18:]
        a0, a0_packetsize = struct.unpack('!BB', mms[:2])
        a1, a1_packetsize = struct.unpack('!BB', mms[2:4])
        invokeID, invokeID_size = struct.unpack('!BB', mms[4:6])
        a2, a2_packetsize = struct.unpack('!BB', mms[6 + invokeID_size:6 + invokeID_size + 2])
        mms_identify_info = mms[6 + invokeID_size + 2:]
        vendor_name_size, = struct.unpack('!B', mms_identify_info[1:2])
        vendor_name = ''.join(struct.unpack('!%dc' % vendor_name_size, mms_identify_info[2:2 + vendor_name_size]))
        mms_identify_info = mms_identify_info[2 + vendor_name_size:]
        model_name_size, = struct.unpack('!B', mms_identify_info[1:2])
        model_name = ''.join(struct.unpack('!%dc' % model_name_size, mms_identify_info[2:2 + model_name_size]))
        mms_identify_info = mms_identify_info[2 + model_name_size:]
        revision_size, = struct.unpack('!B', mms_identify_info[1:2])
        revision = ''.join(struct.unpack('!%dc' % revision_size, mms_identify_info[2:2 + revision_size]))
        with open(filepath,'a+') as f:
            writeline="{\"ip\":\""+dst[0]+"\","+"\"Vendor\":\"MMS\","+\
                "\"Device\":\""+model_name+"\","+\
                "\"DeviceType\":\"PLC\","+\
                      "\"Protocol\":\"MMS\",\"Marked\":\"true\"}"
            f.write(writeline)
            f.close()
        sock.close()
    else:
        sock.close()

if __name__=="__main__":
    if len(sys.argv)>1:
        mmsBanner((sys.argv[2],102),sys.argv[1])