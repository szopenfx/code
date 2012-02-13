del web-start-keystore
set PATH="C:\Program Files\Java\jdk1.5.0_06\bin"
keytool -genkey -keystore web-start-keystore -alias joost
keytool -selfcert -keystore web-start-keystore -alias joost
keytool -list -keystore web-start-keystore
