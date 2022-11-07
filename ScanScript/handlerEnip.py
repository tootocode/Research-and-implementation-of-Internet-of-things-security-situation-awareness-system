with open("./vendors","r") as f:
    for line in f.readlines():
        temp=line.split("=")
        print(temp[1].replace("\n",""))
