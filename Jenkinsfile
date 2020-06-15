pipeline {
    agent any
    tools{
        maven 'maven'
    }

    environment {
        GIT_PROJECT_ADDR="https://github.com/tommyteng2019/integratedFSDEmart.git" 
        JENKINS_WORKSPACE="/var/jenkins_home/workspace"                             
        PROJECT_NAME="integratedFSDEmart"  			                                          
        IMAGE_NAME="integratedFSDEmart"                                                   
        IMAGE_ADDR="tommyteng/${IMAGE_NAME}"                                          
        VERSION_ID="1.0.0"
        CREDENTIAL_ID="Dockerhub"
    }
node {
   stage('git clone') { // for display purposes
     checkout([$class: 'GitSCM', branches: [[name: '*/master']], doGenerateSubmoduleConfigurations: false, extensions: [], submoduleCfg: [], userRemoteConfigs: [[credentialsId: 'gitlab', url: 'http://192.168.1.240/edc/search-xiehe.git']]])
   }
    stages {
        stage('docker build') {
            steps{	       	    
				script {	    
                
				def OLD_CONTAINER_ID=sh(returnStdout:true,script:"sudo docker ps -aq --filter name=${IMAGE_NAME}")
				echo "OLD_CONTAINER_ID:${OLD_CONTAINER_ID}"			
				if(OLD_CONTAINER_ID){	
					echo 'delete old container'	
					sh 'sudo docker rm -f $(sudo docker ps -aq --filter name="${IMAGE_NAME}")'
				}
							
				def OLD_IMAGE_ID=sh(returnStdout:true,script:"sudo docker image ls -q ${IMAGE_NAME}")
				echo "OLD_IMAGE_ID:${OLD_IMAGE_ID}"			
				if(OLD_IMAGE_ID){
					echo 'delete old image'	
					sh 'sudo docker rmi -f $(sudo docker image ls -q ${IMAGE_NAME})'
				}	
				
				sh 'sudo docker build -t ${IMAGE_NAME}:${VERSION_ID} .'
				def NEW_IMAGE_ID=sh(returnStdout:true,script:"sudo docker image ls -q ${IMAGE_NAME}")
				echo "NEW_IMAGE_ID:${NEW_IMAGE_ID}"
				}       
            }
        }
        stage('run') {
            
            agent any
            options {
                
                skipDefaultCheckout()
            }
            steps {  
	       script {	    
                echo 'pull image and docker run'
                withEnv(['JENKINS_NODE_COOKIE=dontKillMe']) {
		  
		    def OLD_CONTAINER_ID=sh(returnStdout:true,script:"sudo docker ps -aq --filter name=${IMAGE_NAME}")
		    echo "OLD_CONTAINER_ID:${OLD_CONTAINER_ID}"			
		    if(OLD_CONTAINER_ID){	
		        echo 'delete old container'	
                        sh 'sudo docker rm -f $(docker ps -aq --filter name="${IMAGE_NAME}")'
		    }
       
		   sh 'sudo docker run --name "${IMAGE_NAME}" --restart=always -p 80:80 --network emart-network -d ${IMAGE_NAME}:${VERSION_ID}'
					
                }
		}
            }
        }
    }
}