import socket, ssl
import OpenSSL
from OpenSSL import crypto
from Crypto.PublicKey import RSA
import sys
import os
import csv
def getRSALib(ip,port,ofile,infile,classfile):
    libs=["G&D SmartCafe 3.2",
          "G&D SmartCafe 4.x & 6.0",
          "GNU Crypto 2.0.1",
          "Gemalto GXP E64",
          "NXP J2A080 & J2A081 & J3A081 & JCOP 41 V2.2.1",
          "Oberthur Cosmo Dual 72K",
          "OpenSSL 0.9.7 & 1.0.2g & 1.0.2k & 1.1.0e",
          "PGPSDK 4 FIPS",
          "Infineon JTOP 80K, YubiKey 4 & 4 Nano",
          "NXP J2D081 & J2E145G, YubiKey NEO",
          "Bouncy Castle 1.54 (Java), Crypto++ 5.6.0 & 5.6.3 & 5.6.5, Libgcrypt 1.7.6 FIPS, Microsoft CryptoAPI & CNG & .NET",
          "Bouncy Castle 1.53 (Java), Cryptix JCE 20050328, FlexiProvider 1.7p7, HSM Utimaco Security Server Se50, Nettle 2.0, PolarSSL 0.10.0, PuTTY 0.67, SunRsaSign OpenJDK 1.8.0, mbedTLS 1.3.19 & 2.2.1 & 2.4.2",
          "Botan 1.5.6 & 1.11.29 & 2.1.0, Feitian JavaCOS A22 & A40, Gemalto GCX4 72K, HSM SafeNet Luna SA-1700, LibTomCrypt 1.17, Libgcrypt 1.6.0 & 1.6.5 & 1.7.6, Libgcrypt 1.6.0 FIPS & 1.6.5 FIPS, Nettle 3.2 & 3.3, Oberthur Cosmo 64, OpenSSL FIPS 2.0.12 & 2.0.14, PGPSDK 4, WolfSSL 2.0rc1 & 3.9.0 & 3.10.2, cryptlib 3.4.3 & 3.4.3.1"]
    context = ssl.SSLContext(ssl.PROTOCOL_TLSv1_2)

    s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    ssl_sock = context.wrap_socket(s, server_hostname=ip)
    ssl_sock.connect((ip, port))
    ssl_sock.close()
    cert = ssl.get_server_certificate((ip, port))
    x509 = OpenSSL.crypto.load_certificate(OpenSSL.crypto.FILETYPE_PEM, cert)
    pk = x509.get_pubkey()
    pub_key = crypto.dump_publickey(crypto.FILETYPE_PEM, pk)
    # print(pub_key)
    key = RSA.importKey(pub_key.decode())

    # print(hex(key.n))
    # print(hex(key.e))
    # print(pub_key)
    store_info="{\"n\":\""+hex(key.n)+"\","+"\"e\":\""+hex(key.e)+"\","+"\"count\":1,"+"\"source\":[\"Example Microsoft CryptoAPI key 1\"]}"

    in_file=infile
    classificate_file=classfile
    out_file=ofile
    with open(in_file,"w") as f:
        f.write(store_info+"\n")
        f.write("{\"n\":\"0xaed25a5cf56ce04bfd26ee7619a94b4c490ca6856bebe174a17bb2a211e5d1da339df2bae378caeef1ea4938b4a5aed95d95e7fae5f5800eeb742b1335d1e683\", \"e\":\"0x10001\", \"count\":1, \"source\":[\"Example Microsoft CryptoAPI key 2\"]}\n")
        f.write("{\"n\":\"0xbdde8198f524d166baf28ff7df5693490e440e09e8798c6b5c230e70303ca5c3f00f8786f4c1b0a0373281cf62a950cebbe7c000f57b9ab7074902ba7909f593\", \"e\":\"0x10001\", \"count\":1, \"source\":[\"Example Microsoft CryptoAPI key 3\"]}\n")
        f.write("{\"n\":\"0xf083b58090693364b7ba3a7c252c6501d78d1ed41c7b943aee92ba6a19ff51f39f104a89810caebb230f4871317ed4f36b87888eb6f4605a8dc985727bd4e101\", \"e\":\"0x10001\", \"count\":1, \"source\":[\"Example Microsoft CryptoAPI key 4\"]}\n")
        f.write("{\"n\":\"0xf9dd1d4fa5028b641c7fbfcdd351600f11844ea657ddbb6ab910aaa1b8ca4854e42ceba0b68438493e6190396bd40a6fb33a5e4cc9312f36b09bee55c46cd607\", \"e\":\"0x10001\", \"count\":1, \"source\":[\"Example Microsoft CryptoAPI key 5\"]}\n")
        f.write("{\"n\":\"0xc6fd453318ef22f97a189a799e7ea05ded9b6a3f39f22d15693c66e13f483788adfbad1e4814daad4af50119a48bf6664cdee1fc0899d73e2c3c488c259627dd\", \"e\":\"0x10001\", \"count\":1, \"source\":[\"Example Microsoft CryptoAPI key 6\"]}\n")
        f.write("{\"n\":\"0xd7ed7fdc0904339163a95b36f5e37d1d3e0f7b68c226441e944f4a43df0bbe08fd3c2d5e564a311d12100045c59f9a49b84d1aedf174671780ecb14b5154e77d\", \"e\":\"0x10001\", \"count\":1, \"source\":[\"Example Microsoft CryptoAPI key 7\"]}\n")
        f.write("{\"n\":\"0xb946bb9374985374eedc3457ad193617ee0f99925efc5859281743e4a7610de86f835e4a9b70ce743cb977d38931ab78930d56b80413720910425214c03f6b2d\", \"e\":\"0x10001\", \"count\":1, \"source\":[\"Example Microsoft CryptoAPI key 8\"]}\n")
        f.write("{\"n\":\"0x97C37F997E1B0850DAC67881B12FC819BEF7942CB1EB86C00A48425D325DF9BF34A979C8352807004E9FFBB553DA560FD6D66720E1F6C94ED2C90E1FB1AB1F59\", \"e\":\"0x10001\", \"count\":1, \"source\":[\"Example OpenSSL key 1\"]}\n")
        f.write("{\"n\":\"0xAF40070DF129E4D7379E5255BF69919FBE37791BD8412A3F4A7058E114164413B8B8E82E5276032E7D78D4CAEF0EB14DB9C8C49E722488F9B9C3C99ACADAE0A3\", \"e\":\"0x10001\", \"count\":1, \"source\":[\"Example OpenSSL key 2\"]}\n")
        f.write("{\"n\":\"0xD0680FCA1CA37048DAF1F8C9003992EC47774621D683F025FD440A94B966F9DEDA54C78C40383E2CC565A5EA3D155D95DE27740A682B52B4456C1CA29DDF2061\", \"e\":\"0x10001\", \"count\":1, \"source\":[\"Example OpenSSL key 3\"]}\n")
        f.write("{\"n\":\"0xF5450B149B4FACBEB64682E114798E310F8F8876B2EA26D27E6BBE5E211CA5744B321E4186F8FD88598ACC807E2941A3D3A8AC7041CAE2928FAF840C9DF99457\", \"e\":\"0x10001\", \"count\":1, \"source\":[\"Example OpenSSL key 4\"]}\n")
        f.write("{\"n\":\"0xA53CE9FAA5EB66F285AFFDDC704B09DB12C3FF022C2DF189034C127B74CF25ADD6331E27B7B2C1CBC8E96CEFA172315D7A8CE61B84BC73356E7A3A7EAEC9EB1B\", \"e\":\"0x10001\", \"count\":1, \"source\":[\"Example OpenSSL key 5\"]}\n")
        f.write("{\"n\":\"0xDD5E6E80BA94EDCDEB21AE861D65FC1398C38A3382A75D9BD15E356EEAFC9FD922EC67C498974E9E68653E23B2C0ECDB44D1BB53C9B6411CAB6ECE2830FAC8A9\", \"e\":\"0x10001\", \"count\":1, \"source\":[\"Example libgcrypt batch of keys A\"]}\n")
        f.write("{\"n\":\"0xD711F18F4F5E2777B6DAF552092FD678AD9037E66BB0E50C2F98E0700C4C6190FC464204BD244F439A48E8B1B84E3993B10225C42EF01CD46FB84345D31E730F\", \"e\":\"0x10001\", \"count\":1, \"source\":[\"Example libgcrypt batch of keys A\"]}\n")
        f.write("{\"n\":\"0xB2C60FB1F4ABE9E2F12F8041500E1B3D9B93812469BA7B6A6FCA6FD224F60F768D5A1D136E53FF31AB3F548748471D21720D0A556E685F14B4D3E1D6E1E49E25\", \"e\":\"0x10001\", \"count\":1, \"source\":[\"Example libgcrypt batch of keys A\"]}\n")
        f.write("{\"n\":\"0xEE106D4F6DEBF6E381E59938B0600B2C42A7BF2EB9C73E72D389C1465DA15D3D8DA97A9086EFF8D9ACAD981A2CFF1D50D1FB27A0937AD97081D666489FEEBB0D\", \"e\":\"0x10001\", \"count\":1, \"source\":[\"Example libgcrypt batch of keys A\"]}\n")
        f.write("{\"n\":\"0xA162F37DF808B8C427975F470383830C23554B97D248A0976273DA9B8571B999918AF92ECD082259011724525572FF2D27696F575BAFD458E122C1437EBBF655\", \"e\":\"0x10001\", \"count\":1, \"source\":[\"Example libgcrypt batch of keys A\"]}\n")
        f.write("{\"n\":\"0x42acccd9e51d0e45a8e49b2461c499148e621b9a1afbf7cf581e7cd7a693c078ec1a13127625bce08ab21f849ff92f082cc68f0800e495c9b68416c540faaa03\", \"e\":\"0x10001\", \"count\":1, \"source\":[\"Example PGPSDK4 FIPS key 1\"]}\n")
        f.write("{\"n\":\"0x4c61be4982144b0b25f26d36faf3c78036a4ab8f169fd437fbf6edaa8565c656d3c3fca3f3c659d2d8a69f9affe89cad25efb31170c674de6fc32fba5eee75e3\", \"e\":\"0x10001\", \"count\":1, \"source\":[\"Example PGPSDK4 FIPS key 2\"]}\n")
        f.write("{\"n\":\"0x5a5c2c9655733c6b1b8dc9054cba27a27bcf29254f2c0c7e3687d6e614d675438572a7eda6bc06486c2c60e7c26c0ac754b9fd62b0fa1988fa537ce93bd19bbd\", \"e\":\"0x10001\", \"count\":1, \"source\":[\"Example PGPSDK4 FIPS key 3\"]}\n")
        f.close()
    # print("java -jar /Users/ludam/Desktop/RsaLibTest/classifyRSAkey.jar -c -t "+classificate_file+
    #           " -i "+in_file+" -o "+out_file+"c-p estimate -b modulus_hash -e json -m memory")
    os.system("java -jar /home/ysf/Project_service/classifyRSAkey.jar -c -t "+classificate_file+
              " -i "+in_file+" -o "+out_file+" -p estimate -b modulus_hash -e json -m memory")

    r=""
    with open(out_file+"/infile.json/individual_statistics.csv","r") as f:
        reader=csv.reader(f)
        result=list(reader)
        #print(result[1])
        num=0
        for i in range(4,17):
            # print(float(result[1][i]))
            # print(type(float(result[1][i])))
            if float(result[1][i])>num:
                r=libs[i-4]
                num=float(result[1][i])
        f.close()
    with open(out_file+"/libresult.txt","w") as f:
        f.write(r)
        f.close()
    #print(r)

if __name__=="__main__":
    if len(sys.argv)>1:
        IP=sys.argv[1]
        ip=IP.split(":")[0]
        port=IP.split(":")[1]
        getRSALib(ip,int(port),sys.argv[2],sys.argv[3],sys.argv[4])
