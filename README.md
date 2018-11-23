#### 
mmall: backend project
System: CentOS 6.8 64
##### Installation
1. JDK: 
* create file:
```
> cd /
> mkdir developer
> cd developer
```
* download:
JDK url will be invalid after a period of time. Please donwload on www.oracle.com
```
> wget http://xxx/jdk-8u144-linux-x64xxxxx.rpm
```
* remove suffix
```
> mv jdk-8u144-linux-x64.rpm jdk-8u144-linux-x64.rpmxxxxxxxxx
```
* grant privileges
```
> chmod 777 jdk-8u144-linux-x64.rpm
```
* installation
```
> rpm -ivh jdk-8u144-linux-x64.rpm
```
2. iptables:
* turn off:
```
> /etc/init.d/iptables stop
```
* switch if off permanently:
```
> chkconfig iptables off
```
* Selinux status
```
> sestatus
```
* turn off Selinux
```
> vi /etc/selinux/config 

modify: SELINUX=disabled  
```
3. Tomcat: 
* move to directory
```
> cd /developer
```
* download
```
> wget http://mirrors.hust.edu.cn/apache/tomcat/tomcat-7/v7.0.79/bin/apache-tomcat-7.0.79.tar.gz
```
* unzip
```
> tar -zxvf apache-tomcat-7.079.tar.gz
```
* add environment variables
```
> vim /etc/profile

add:
export CATALINA_HOME=/developer/apache-tomcat-7.0.79

export PATH=$PATH:$CATALINA_HOME/bin
```
* source
```
> source /etc/profile
```
* turn on tomcat
```
> cd apache-tomcat-7.0.79
> ./bin/startup.sh
```
4. Maven: 
* move to directory
```
> cd /developer
```
* download
```
> wget https://mirrors.tuna.tsinghua.edu.cn/apache/maven/maven-3/3.5.2/binaries/apache-maven-3.5.2-bin.tar.gz
```
* unzip
```
> tar -zxvf apache-maven-3.5.2-bin.tar.gz 
```
* add environment variables
```
> vim /etc/profile

add:
export MAVEN_HOME=/developer/apache-maven-3.5.2

export PATH=$PATH:$MAVEN_HOME/bin
```
* source
```
> source /etc/profile
```
* check tomcat version
```
> mvn -version
```
5. MySQL: 
* download
```
> yum -y install mysql-server
```
* add utf-8
```
> vim /etc/my.cnf

add:
character-set-server=utf8

default-character-set=utf8
```
* start mysql
```
> service mysqld start
```
* log in root
```
> mysql -u root
```
* change password
```
> set password for root@localhost = password('YOUR_PASSWORD');
```
* add new user
```
> insert into mysql.user(host,user,password) values("localhost","YOUR_USERNAME",password("YOUR_PASSWORD"));
```
* log in your new account
```
> mysql -u root -p
```
* create database
```
> mysql> create database `YOUR_DATABASE_NAME` default character set utf8 COLLATE utf8_general_ci;
```
* grant privileges
```
>  grant all privileges on YOURDATABASENAME.* to YOURUSERNAME@localhost identified by 'YOUR_PASSWORD';
```
* enable remote control
```
>  update mysql.user set Host='%' where user='YOUR_USERNAME';
```
6. Nginx: 
* move to directory
```
> cd /developer
```
* download
```
> yum -y install pcre pcre-devel zlib zlib-devel openssl openssl-devel gcc gcc-c++ autoconf automake make
> wget http://nginx.org/download/nginx-1.12.2.tar.gz
```
* unzip
```
> tar -zxvf nginx-1.12.2.tar.gz
```
* enter nginx
```
> cd nginx-1.12.2
```
* compile
```
> make
```
* installation
```
> make install
```
* modify conf.
```
> vim /usr/local/nginx/conf/nginx.conf

add:
include vhost/*.conf;
ON TOP OF:  # another virtual host using mix of IP-, name-, and port-based configuration
```
* add config directory and files
```
> cd /usr/local/nginx/conf
> mkdir vhost
```
* start nginx
```
> cd /usr/local/nginx/sbin
> ./nginx
```
7. Vsftpd: 
* move to directory
```
> cd ~
> mkdir ftpfile
```
* download
```
> yum -y install vsftpd
```
* add user
```
> useradd YOUR_USERNAME
```
* reset password
```
> passwd YOUR_PASSWORD
```
* add username
```
> vim /etc/vsftpd/chroot_list

add: 
YOUR_USERNAME
```
* modify config
```
> vim /etc/vsftpd/vsftpd.conf

modify:
anonymous_enable=NO
idle_session_timeout=6000
local_root=/ftpfile
use_localtime=YES
chroot_list_enable=YES
chroot_list_file=/etc/vsftpd/chroot_list
# your ports opened for vsftpd
pasv_min_port=30000
pasv_max_port=30999
pasv_enable=YES
```
* grant privileges
```
> chown -R YOUR_FTP_USERNAME.YOUR_VITUAL_USERNAME /ftpfile
```
* open vsftpd
```
> service vsftpd restart
```
* check version
```
> vsftpd -version
```












