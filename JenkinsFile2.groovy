
def myEmulators = ["Nexus_5X_API_25_7.1.1", "Galaxy_Nexus_API_18_Jelly_Bean"]
def tasks = [:]
def basePort = 5555

stage('Preparation') {
    node {
        echo "Get the project from the Github repository"
        git branch: 'master', url: 'git@github.com:jeancdc/MyJenkinsApplication.git'
    }
}

stage('Build') {
    node {
        echo "Clean the project and assemble"
        sh './gradlew clean assembleDebug'
    }
}

stage('Unit tests') {
    node {
        echo "Execute the JUnit tests"
        sh './gradlew testDebugUnitTest'
    }
}


for(int i = 0; i < myEmulators.size(); i++) {

    def myEmulator = myEmulators[i]
    def port = basePort + (i * 2)

    tasks["${myEmulator}"] {

        node(myEmulator) {

            sh "$ANDROID_HOME/emulator/emulator -avd ${myEmulator} -port ${port} &"
            timeout(time: 60, unit: 'SECONDS') {
                sh "$ADB -s emulator-${port} wait-for-device"
            }
            echo "AVD ${myEmulator} is now ready."

            echo "Execute the instrumented tests"
            sh './gradlew connectedDebugAndroidTest'

            sh "$ADB -s emulator-${port} emu kill"
        }
    }
}

stage('Instrumented tests') {
    parallel tasks
}

//stage('Instrumented tests') {
//
//    node {
//        echo "Launch the emulators"
//
//        for(int i = 0; i < myEmulators.size(); i++) {
//            def myEmulator = myEmulators[i]
//            def port = basePort + (i * 2)
//
//            sh "$ANDROID_HOME/emulator/emulator -avd ${myEmulator} -port ${port} &"
//            timeout(time: 60, unit: 'SECONDS') {
//                sh "$ADB -s emulator-${port} wait-for-device"
//            }
//            echo "AVD ${myEmulator} is now ready."
//        }
//
//        echo "Execute the instrumented tests"
//        sh './gradlew connectedDebugAndroidTest'
//
//        echo "Close the emulators"
//        for(int i = 0; i < myEmulators.size(); i++) {
//            def port = basePort + (i * 2)
//            sh "$ADB -s emulator-${port} emu kill"
//        }
//    }
//}

stage('Lint') {
    node {
        echo "Execute the Lint report"
        sh './gradlew lintDebug'
    }
}

stage('Results') {
    node {
        echo "Publish the reports"
        junit '**/build/test-results/testDebugUnitTest/*.xml, **/build/outputs/androidTest-results/connected/*.xml'
        jacoco(
                execPattern: '**/**.exec, **/**.ec',
                classPattern: '**/classes',
                sourcePattern: '**/src/main/java',
                exclusionPattern: '**/R.class, **/R$*.class, **/BuildConfig.*, **/Manifest*.*, **/*Test*.*'
        )
        androidLint canComputeNew: false, defaultEncoding: '', healthy: '', pattern: '**/build/reports/lint-results-debug.xml', unHealthy: ''
    }
}



