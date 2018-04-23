
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
        sh './gradlew clean assembleDebug'
    }
}

stage('Unit tests') {
    node {
        sh './gradlew testDebugUnitTest'
    }
}

for(int i = 0; i < myEmulators.size(); i++) {

    def myEmulator = myEmulators[i]
    def port = basePort + (i * 2)

    tasks["${myEmulator}"] = {

        node {

            parallel (

                launchEmulator: {
                    sh "$ANDROID_HOME/emulator/emulator -avd ${myEmulator} -port ${port} &"
                },

                runAndroidTests: {
                    timeout(time: 60, unit: 'SECONDS') {
                        sh "$ADB -s emulator-${port} wait-for-device"
                    }
                    echo "Device(s) is ready"
                    //sh "$ADB -s emulator-5555 shell wm dismiss-keyguard"
                    //sh "$ADB -s emulator-5555 shell input keyevent 3"
                    try {
                        sh './gradlew connectedDebugAndroidTest createDebugCoverageReport'
                    } catch(e) {
                        throw e
                    }
                    sh "$ADB emu kill"
                }

            )
        }
    }
}

stage('Instrumented tests') {
    parallel tasks
}

stage('Lint') {
    node {
        sh './gradlew lintDebug'
    }
}

stage('Results') {
    node {
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


