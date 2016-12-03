node('master')
{
    def version = 1.0
//    def gitUrl = 'https://github.com/chrlic/cliqrDemo'
    def gitUrl = '/Users/mdivis/Documents/workspaceLuna/CliQrDemo/.git'
    def projectRoot = ""
    def group = "dashboard/frontend/"
    def artifactName = "dashboard_ui"
    def artifactRepo = "ext-release-local"

	println "Env Build Id: ${env.BUILD_ID}"

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
