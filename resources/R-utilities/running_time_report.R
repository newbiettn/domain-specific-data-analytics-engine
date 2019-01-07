library(dplyr)
################################################################################
dt.1 <- read.csv("new_results_backup/myengine_evaluation_result-errorrate-kmeans.csv")
dt.2 <- read.csv("new_results_backup/myengine_evaluation_result-errorrate-gmeans.csv")
dt.3 <- read.csv("results_backup/autoweka_evaluation_result_errorrate_8000Mb_10threads_1m.csv")
dt.4 <- read.csv("results_backup/autoweka_evaluation_result_errorrate_8000Mb_10threads_2m.csv")
dt.5 <- read.csv("results_backup/autoweka_evaluation_result_errorrate_8000Mb_10threads_15m_seed_1_150.csv")

dt.6 <- read.csv("new_results_backup/myengine_evaluation_result-auc-kmeans.csv", stringsAsFactors=FALSE)
dt.7 <- read.csv("new_results_backup/myengine_evaluation_result-auc-gmeans.csv", stringsAsFactors=FALSE)
dt.8 <- read.csv("results_backup/autoweka_evaluation_result_auc_8000Mb_10threads_1m.csv", stringsAsFactors=FALSE)
dt.9 <- read.csv("results_backup/autoweka_evaluation_result_auc_8000Mb_10threads_2m.csv", stringsAsFactors=FALSE)
dt.10 <- read.csv("results_backup/autoweka_evaluation_result_auc_8000Mb_10threads_15m_seed_1_150.csv", stringsAsFactors=FALSE)
################################################################################
dt.1$timeElapsed <- (dt.1$timeElapsed/(1000))
dt.2$timeElapsed <- (dt.2$timeElapsed/(1000))
dt.3$timeElapsed <- (dt.3$timeElapsed/(1000))
dt.4$timeElapsed <- (dt.4$timeElapsed/(1000))
dt.5$timeElapsed <- (dt.5$timeElapsed/(1000))
dt.6$timeElapsed <- (dt.6$timeElapsed/(1000))
dt.7$timeElapsed <- (dt.7$timeElapsed/(1000))
dt.8$timeElapsed <- (dt.8$timeElapsed/(1000))
dt.9$timeElapsed <- (dt.9$timeElapsed/(1000))
dt.10$timeElapsed <- (dt.10$timeElapsed/(1000))

report <- function(dt){
  dt <- dt %>%
    summarize(meanTime = mean(timeElapsed),
              stdTime = sd(timeElapsed)) %>%
    as.data.frame()
  return(dt)
}

dt.1 <- report(dt.1)
dt.2 <- report(dt.2)
dt.3 <- report(dt.3)
dt.4 <- report(dt.4)
dt.5 <- report(dt.5)
dt.6 <- report(dt.6)
dt.7 <- report(dt.7)
dt.8 <- report(dt.8)
dt.9 <- report(dt.9)
dt.10 <- report(dt.10)




