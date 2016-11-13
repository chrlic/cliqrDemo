node('master')
{
    def version = 1.0
//    def gitUrl = 'https://github.com/chrlic/cliqrDemo'
    def gitUrl = '/Users/mdivis/Documents/workspaceLuna/CliQrDemo/.git'
    def projectRoot = ""
    def group = "dashboard/frontend/"
    def artifactName = "dashboard_ui"
    def artifactRepo = "ext-release-local"

    stage "git"

    git poll: true, url: "${gitUrl}"

    dir(projectRoot)
    {

        sh 'chmod +x gradlew'
        stage "test"
        sh './gradlew clean buildAllServices'

        stage "build"
        sh './gradlew buildAllContainerImages'

    }
}

/*
node {
    echo "${env.JOB_URL}test/trend"
    // Mark the code checkout 'stage'....
    stage 'Checkout'
    
    // Get some code from a GitHub repository
    git url: 'https://github.com/chrlic/cliqrDemo.git'
    
    // Mark the code build 'stage'....
	stage 'build_Project'
	node{
  		if(isUnix()){
	  		sh 'gradle build --info'
  		}
  		else{
    		bat 'gradle build --info' 
  		}
  	}
}
*/
