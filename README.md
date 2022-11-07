# 
随着物联网产业的发展，物联网技术的应用越来越广泛。在工业 4.0 的大趋
势下设备入网为工业生产效率的提高提供了许多切实可行的方案，同时设备入网
也为人们的生活提供诸多便利。物联网技术在为人们的生产和生活提供便捷的同
时，也带来了许多问题。许多针对物联网设备的攻击案例使人们看到不安全的物
联网设备产生的巨大危害。尤其在 TLS/SSL 协议使用方面，物联网设备厂商在使
用 TLS/SSL 协议时容易使用一些不正确的配置，这些不正确的配置可能会被攻击
者利用，最终造成安全事故。成功识别网络中存在的物联网设备，并进行安全性
测试，可以帮助使用者及时发现网络中潜在的安全问题，降低网络被攻破的可能
性。
本课题研究了网络中广泛应用的互联网协议和工业控制协议，分析网络协
议的通信机制以达到获取包含设备信息的协议内容的目的。研究基于引擎的规则
生成方法（ARE）来创建设备信息库，并利用基于搜索的物联网设备识别框架实
现设备的识别。设计并实现了采用 B/S 架构的物联网安全态势感知系统，
该系统采用前后端分离的方式，前端使用 Vue 编写实现，后端使用 Spring boot
框架响应用户的请求，对于系统各个模块的扫描结果，采用 MySQL 进行存储。
物联网安全态势感知系统，最终能够扫描发现 21 种不同的应用层网络协议
和 20 种设备类型的识别以及 17 类 TLS/SSL 安全漏洞的检测。物联网安全态势感
知系统未来还会支持更多协议的扫描，扩充更加完善的设备信息库，实现多种类
型漏洞的检测。
系统流程图
![image](https://user-images.githubusercontent.com/61414475/200316265-b2eb1d85-656e-42a8-9307-e4ad6a4cb6f6.png)
核心流程：
/findprotocol提交协议扫描任务，/searchProtocollist和/seatchprotocolresult显示和查询所扫描的协议；根据前面提交的IP进行设备识别（或单独提交设备识别任务/markdevice），/searchmarkdevicelist和/seatchdeviceresult显示和查询识别的设备；如果在提交协议扫描任务界面将渗透测试按钮设为是（excuteexploit=yes），执行渗透测试，或单独提交渗透测试任务/expoit，/getvulnerabilityinfolist和/searchdevicevul显示和查询所发现的漏洞；tls测试部分同渗透测试部分。
例如扫描27.221.22.158或27.192.0.0/11，在协议扫描界面<任务名称：Scan 、任务目标：27.221.22.158（IP地址或地址段） 、渗透测试：Yes（点击事件）、 TLS测试：Yes、全端口扫描：No、任务提交时间：2021-09-20（日期组件）、任务描述：其他>；点击提交后，前端调用js代码：get('/findprotocol' + '?name=' + this.form.name + '&target=' + this.form.target + '&desc=' + this.form.desc + '&date=' + this.form.date + '&excuteexploit=' + this.excuteexploit + '&excutetlstest=' + this.excutetlstest + '&allportscan=' + this.allportscan）（字段信息在19中已给出）将信息提交给后端，后端收到信息后，返回给前端字符串‘success’，证明提交成功。/searchProtocollist和/seatchprotocolresult显示和查询扫描的协议，具体字段已在18,20中给出。同理可显示设备扫描、渗透测试、tls测试的结果。
还可进行设备识别模块单独扫描，<任务名称：Scan 、任务目标：27.221.22.158 、任务提交时间：2021-09-20、任务描述：其他>,提交任务后进行扫描并展示结果（已在21，22,23中给出）。
其他模块单独运行同设备识别模块。
45、/datav/chinamap 获取ip位置信息，返回list列表，三个属性，name表示各省名称，num表示ip数量
/datav/sdmapnum获取ip位置信息，返回list列表，三个属性，name表示山东各市名称，num表示ip数量
