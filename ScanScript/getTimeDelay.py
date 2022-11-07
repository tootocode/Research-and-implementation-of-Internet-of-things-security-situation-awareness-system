# coding=utf-8
import re
import subprocess
import sys
try:
	print("123")
	ret = subprocess.check_output('ping ' + '1.1.1.1' +' -n %d ' % (1), shell=True,
                                          stderr=subprocess.STDOUT).decode('gbk')
except:
	ret = "error"
if not ret.__eq__("error"):
	print(ret)
tim = re.search(r'(.*)\((.*)\% 丢失(.*)\n(.*)\n(.*)平均 = (.*?)ms', ret, re.M | re.I)
print("丢包率 : " + str(tim.group(2)))
print("平均时间 : " + tim.group(6))