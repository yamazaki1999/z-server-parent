创建证书:
keytool -genkey -alias 你的证书别名 -keyalg 密钥算法 -keystore 证书库文件保存的位置和文件名 -keysize 密钥长度 -validity 证书有效期天数
-genkey：创建证书
-alias：证书的别名。在一个证书库文件中，别名是唯一用来区分多个证书的标识符
-keyalg：密钥的算法，非对称加密的话就是RSA
-keystore：证书库文件保存的位置和文件名。如果路径写错的话，会出现报错信息。如果在路径下，证书库文件不存在，那么就会创建一个
-keysize：密钥长度，一般都是1024
-validity：证书的有效期，单位是天。比如36500的话，就是100年
所以完整的一条创建命令可能是这样的：
keytool -genkey -alias mykey -keyalg RSA -keystore C:/mykeystore.keystore -keysize 1024 -validity 36500 

查看证书库中的证书:
keytool -list -keystore /home/freezingxu/mykeystore.keystore  

导出公钥文件:
-export：用于导出公钥文件的命令参数
-alias：你的证书在证书库中的别名，也是唯一标识
-keystore：完整的证书库文件所在的目录及文件名
-file：导出后的公钥文件所在的完整目录及文件名
keytool -export -alias mykey -keystore /home/freezingxu/mykeystore.keystore -file mypublickey.cer 