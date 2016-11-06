node('master')
{
    def version = 1.0
    def gitUrl = 'https://github.com/chrlic/cliqrDemo'
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
        sh './gradlew clean test'

        stage "build"
        sh './gradlew build createPom'

 /*
        stage "artifact"
        def server = Artifactory.server('artifactory_dev01')
        def uploadSpec = """{
          "files": [
            {
              "pattern": "build/**.jar",
              "target": "${artifactRepo}/$group/${artifactName}/${version}/${artifactName}-${version}.jar"
            },
            {
              "pattern": "pom.xml",
              "target": "${artifactRepo}/$group/${artifactName}/${version}/${artifactName}.pom"
            }
         ]
        }"""
        def buildInfo1 = server.upload spec: uploadSpec
        server.publishBuildInfo buildInfo1
        */
    }
}


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

