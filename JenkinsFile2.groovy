
def myEmulators = ["Nexus_5X_API_25_7.1.1", "Galaxy_Nexus_API_18_Jelly_Bean"]
def tasks = [:]
def basePort = 5555

stage('Preparation') {
    node {
        git branch: 'master', url: 'git@github.com:jeancdc/MyJenkinsApplication.git'
    }
}

stage('Build') {
    node {
        echo "clean project and assemble"
        sh './gradlew clean assembleDebug'
    }
}

stage('Unit tests') {
    node {
        echo "execute JUnit tests"
        sh './gradlew testDebugUnitTest'
    }
}

stage('Launch emulators') {

    node {
        echo "launching emulators"

        for(int i = 0; i < myEmulators.size(); i++) {
            def myEmulator = myEmulators[i]
            def port = basePort + (i * 2)

            sh "$ANDROID_HOME/emulator/emulator -avd ${myEmulator} -port ${port} &"
            /*timeout(time: 60, unit: 'SECONDS') {
                sh "$ADB -s emulator-${port} wait-for-device"
            }
            echo "AVD ${myEmulator} is now ready."*/
        }
    }
}



