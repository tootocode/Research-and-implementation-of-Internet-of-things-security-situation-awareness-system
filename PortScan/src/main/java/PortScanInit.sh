cp Config PortScan
cd PortScan
docker build -t portscan:1.0.0 .
docker run -d portscan:1.0.0