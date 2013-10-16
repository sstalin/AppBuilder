USER=jia
DEBUG_KEYSTORE=/Users/$USER/.android/debug

keytool -list -alias androiddebugkey -keystore $DEBUG_KEYSTORE.keystore -storepass android -keypass android