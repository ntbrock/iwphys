keytool -genkey -alias server -keyalg RSA -keysize 2048 -keystore InteractiveWebPhysicsNCSSMiwphys_org.jks -dname "CN=InteractiveWebPhysicsNCSSMiwphys.org,OU=Department, O=Interactive Web Physics, L=Durham, ST=North Carolina, C=US" && keytool -certreq -alias server -file InteractiveWebPhysicsNCSSMiwphys_org.csr -keystore InteractiveWebPhysicsNCSSMiwphys_org.jks && echo Your certificate signing request is in InteractiveWebPhysicsNCSSMiwphys_org.csr.  Your keystore file is InteractiveWebPhysicsNCSSMiwphys_org.jks.  Thanks for using the DigiCert keytool CSR helper.