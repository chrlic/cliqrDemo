node {
    echo "${env.JOB_URL}test/trend"
    // Mark the code checkout 'stage'....
    stage 'Checkout'
    
    // Get some code from a GitHub repository
    git url: 'https://github.com/chrlic/cliqrDemo.git'
    
    // Mark the code build 'stage'....
    stage 'Build/Publishing Test Results'
    try{
        sh "./gradlew clean build"
        step([$class: "JUnitResultArchiver", testResults: "build/**/TEST-*.xml"])
        def to = emailextrecipients([
        [$class: 'DevelopersRecipientProvider']
        ])
        mail to: to, subject: "${env.JOB_NAME} Succeeded!",
        body: "View test results here: ${env.BUILD_URL}testReport"
    }
    catch(err){
        stage 'Failure Notification'
        def to = emailextrecipients([
        [$class: 'DevelopersRecipientProvider']
        ])
        mail to: to, subject: "${env.JOB_NAME} Failed!",
        body: "${env.JOB_NAME} failed the last time it was run. See ${env.BUILD_URL} for more information."
        currentBuild.result = 'FAILURE'
    }
}

