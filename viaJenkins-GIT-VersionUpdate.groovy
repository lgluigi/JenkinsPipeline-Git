pipeline {
    agent { label '*VM*' }
    stages{
        stage('Reconstruindo Workspace'){
            steps{
            sh 'ls'
            sh 'rm -rf *'    
            sh 'echo rm -rf removendo arquivos temp'      
            }
        }
        stage('Git Cloning')
        {
            steps {            
                    sh "git clone git@git.com.br/grupo/projeto${COMPONENTE}.git"                         

            } 
        }
        stage('Verificando/Gerando Tags')
        {
            steps{
                //'git checkout numero da tag'
                script{
                    /*definindo variavel clonelist como Lista dos repositórios do git */
                    
                    email = ''
                    name =''
                    def cloneList = ["${COMPONENTE}"]
                    sh "date '+%d/%m/%Y' > data.txt"  
                    localData = readFile('data.txt')
                    for(def i =0; i < cloneList.size(); i++){
                        if("${RELEASE}" == 'Nao'){
                            sh 'echo RELEASE = NAO'
                        dir("${cloneList[i]}"){
                              /****Lendo a tag atual do componente*****/  
                            sh "git config user.email ${email}"
                            sh "git config user.name ${name}"
                            sh 'git tag > tag.txt'
                            sh 'cat tag.txt'
                            String teste = readFile('tag.txt').trim()
                            releasecompare = teste
                            if(teste.equals("")){                                
                                sh "echo Tag não presente em ${cloneList[i]}, criando tag -> ${cloneList[i]}_1.0.0"
                                sh "git tag -m 'Geracao de Tag (${localData})' ${cloneList[i]}_1.0.0"
                                 /* sh git merge --no-ff --commit -X theirs dev
                                            sh "git push"
                                            sh "git push --tags              
                                */
                            }else{
                            converte = teste[-2..-1]
                            println converte
                            println converte.take(1)
                            /*Lógica para adicionar a tag seguinte*/
                            if(converte.take(1).equals(".")){
                               converte = teste [-1]
                                    int numFinal = converte
                                    numFinal = numFinal+1
                                    for(def k = 1; k < 8; k++){                             
                                        if(releasecompare[k*(-1)..k*(-1)].equals("_")){
                                            numeroRelease = releasecompare[k*(-1)+1..k*(-1)+1]                                                                                                               
                                            sh "git tag -m 'Geração da Tag( ${localData} ) ' ${cloneList[i]}_${numeroRelease}.0.${numFinal}"
                                         /* sh git merge --no-ff --commit -X theirs dev
                                            sh "git push"
                                            sh "git push --tags"
                                            */
                                    }
                                }
                            }else{
                                int numFinal = converte
                                numFinal = numFinal+1
                                for(def k = 1; k < 8; k++){                              
                                     if(releasecompare[k*(-1)..k*(-1)].equals("_")){
                                        numeroRelease = releasecompare[k*(-1)+1..k*(-1)+1]
                                        sh "git tag -m 'Geracao de Tag (${localData})' ${cloneList[i]}_${numeroRelease}.0.${numFinal}"
                                          /* sh git merge --no-ff --commit -X theirs dev
                                            sh "git push"
                                            sh "git push --tags"
                                            */
                                        }
                                    }
                                }
                            }
                          
                        }
                        }else{
                            sh 'echo RELEASE = SIM'
                            dir("${cloneList[i]}"){
                                sh "git config user.email ${email}"
                                sh "git config user.name ${name}"
                                sh "git tag -m 'Geracao de Tag (${localData})' ${cloneList[i]}_${VERSAO_RELEASE}"
                                sh "echo ${cloneList[i]}_${VERSAO_RELEASE}"
                             /* sh git merge --no-ff --commit -X theirs dev
                                            sh "git push"
                                            sh "git push --tags"
                                            */
                            }
                        } 
                    }
                }
            }
        }
    }
       /* stage('Sending to builder...')
        {
            steps{
                builder = 
                PARAMETROS ...
                COMPONENTE:
                ORIGEM: SNAPSHOT
                TASK:
                build job: 'JobName' , parameters: [text(name : 'name_id', value: '?')] 
            }
        }*/
        
    
}
