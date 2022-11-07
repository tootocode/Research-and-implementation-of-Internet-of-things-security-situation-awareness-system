cp Config Metasploit
cd Metasploit
docker build -t metasploit:1.0.0 .
docker run -d metasploit:1.0.0
Cd ..
