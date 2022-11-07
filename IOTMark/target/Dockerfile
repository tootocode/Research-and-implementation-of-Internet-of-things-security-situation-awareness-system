FROM openbase:latest
MAINTAINER ysf
ADD IOTMark10.0.jar demo.jar
ADD input_file /input_file
ADD output_file /output_file
ADD ScanScript /ScanScript
ADD DeviceMark.sqlite /
ENTRYPOINT ["java","-jar","demo.jar"]
