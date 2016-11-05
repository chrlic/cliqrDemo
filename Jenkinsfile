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

