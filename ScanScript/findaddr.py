# coding=utf-8
import socket
import struct
import sys
import re

class CzIp:

    def __init__(self, db_file='/home/ysf/Project_service/ScanScript/qqwry.dat'):
        self.f_db = open(db_file, "rb")
        bs = self.f_db.read(8)
        (self.first_index, self.last_index) = struct.unpack('II', bs)
        # print(self.first_index, self.last_index)
        self.index_count = int((self.last_index - self.first_index) / 7 + 1)
        self.cur_start_ip = None
        self.cur_end_ip_offset = None
        self.cur_end_ip = None
        # print(self.get_version(), " 纪录总数: %d 条 " % self.index_count)

    def get_version(self):
        s = self.get_addr_by_ip(0xffffff00)
        # print(s)
        return s

    def _get_area_addr(self, offset=0):
        if offset:
            self.f_db.seek(offset)
        bs = self.f_db.read(1)
        (byte,) = struct.unpack('B', bs)
        if byte == 0x01 or byte == 0x02:
            p = self.getLong3()
            if p:
                return self.get_offset_string(p)
            else:
                return ""
        else:
            self.f_db.seek(-1, 1)
            return self.get_offset_string(offset)

    def _get_addr(self, offset):
        self.f_db.seek(offset + 4)
        bs = self.f_db.read(1)
        (byte,) = struct.unpack('B', bs)
        if byte == 0x01:  # 重定向模式1
            country_offset = self.getLong3()
            self.f_db.seek(country_offset)
            bs = self.f_db.read(1)
            (b,) = struct.unpack('B', bs)
            if b == 0x02:
                country_addr = self.get_offset_string(self.getLong3())
                self.f_db.seek(country_offset + 4)
            else:
                country_addr = self.get_offset_string(country_offset)
            area_addr = self._get_area_addr()
        elif byte == 0x02:  # 重定向模式2
            country_addr = self.get_offset_string(self.getLong3())
            area_addr = self._get_area_addr(offset + 8)
        else:  # 字符串模式
            country_addr = self.get_offset_string(offset + 4)
            area_addr = self._get_area_addr()
        return country_addr + " " + area_addr

    def dump(self, first, last):
        if last > self.index_count:
            last = self.index_count
        for index in range(first, last):
            offset = self.first_index + index * 7
            self.f_db.seek(offset)
            buf = self.f_db.read(7)
            (ip, of1, of2) = struct.unpack("IHB", buf)
            address = self._get_addr(of1 + (of2 << 16))
            # print("%d %s %s" % (index, self.ip2str(ip), address))

    def _set_ip_range(self, index):
        offset = self.first_index + index * 7
        self.f_db.seek(offset)
        buf = self.f_db.read(7)
        (self.cur_start_ip, of1, of2) = struct.unpack("IHB", buf)
        self.cur_end_ip_offset = of1 + (of2 << 16)
        self.f_db.seek(self.cur_end_ip_offset)
        buf = self.f_db.read(4)
        (self.cur_end_ip,) = struct.unpack("I", buf)

    def get_addr_by_ip(self, ip):
        if type(ip) == str:
            ip = self.str2ip(ip)
            # print(ip)
        L = 0
        R = self.index_count - 1
        while L < R - 1:
            M = int((L + R) / 2)
            # print("M:", M)
            self._set_ip_range(M)
            if ip == self.cur_start_ip:
                L = M
                break
            if ip > self.cur_start_ip:
                L = M
            else:
                R = M
        self._set_ip_range(L)
        # version information, 255.255.255.X, urgy but useful
        if ip & 0xffffff00 == 0xffffff00:
            self._set_ip_range(R)
        if self.cur_start_ip <= ip <= self.cur_end_ip:
            address = self._get_addr(self.cur_end_ip_offset)
        else:
            address = "未找到该IP的地址"
        return address

    def get_ip_range(self, ip):
        if type(ip) == str:
            ip = self.str2ip(ip)
        self.get_addr_by_ip(ip)
        range = self.ip2str(self.cur_start_ip) + ' - ' \
                + self.ip2str(self.cur_end_ip)
        return range

    def get_offset_string(self, offset=0):
        if offset:
            self.f_db.seek(offset)
        bs = b''
        ch = self.f_db.read(1)
        (byte,) = struct.unpack('B', ch)
        while byte != 0:
            bs += ch
            ch = self.f_db.read(1)
            (byte,) = struct.unpack('B', ch)
        return bs.decode('gbk')

    def ip2str(self, ip):
        return str(ip >> 24) + '.' + str((ip >> 16) & 0xff) + '.' + str((ip >> 8) & 0xff) + '.' + str(ip & 0xff)

    def str2ip(self, s):
        (ip,) = struct.unpack('I', socket.inet_aton(s))
        # print(((ip >> 8) & 0xff00))
        return ((ip >> 24) & 0xff) | ((ip & 0xff) << 24) | ((ip >> 8) & 0xff00) | ((ip & 0xff00) << 8)

    def getLong3(self, offset=0):
        if offset:
            self.f_db.seek(offset)
        bs = self.f_db.read(3)
        (a, b) = struct.unpack('HB', bs)
        return (b << 16) + a

def getdetials(detial):
    if detial.__contains__(" "):
        provandcity = detial.split(" ")[0]
        # print(provandcity)
        if provandcity.__contains__("省") and provandcity.__contains__("市"):
            match = re.match(r'(.*?)省(.*?)市', provandcity, re.M | re.I)
            print("中国"+":"+match.group(1)+":"+match.group(2)+"市")
        elif provandcity.__contains__("省") and not provandcity.__contains__("市"):
            match = re.match(r'(.*?)省', provandcity, re.M | re.I)
            print("中国" + ":" + match.group(1)+":"+" ")
        elif provandcity.__contains__("香港") or provandcity.__contains__("澳门") or provandcity.__contains__("台湾"):
            print("中国" + ":" + provandcity+":"+" ")
        else:
            print(provandcity+":"+" "+":"+" ")


if __name__ == '__main__':
    if len(sys.argv) == 2:
        cz = CzIp()
        # print(cz.get_version())
        ip = sys.argv[1]
        # print(cz.get_ip_range(ip))
        getdetials(cz.get_addr_by_ip(ip))
