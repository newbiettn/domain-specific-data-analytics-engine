rm(list = ls(all = T))
options(digits=4)
################################################################################
library(ggplot2)
library(wesanderson) #color palette for ggplot
library(dplyr)
library(xtable)
################################################################################
## Summarizes data.
## Gives count, mean, standard deviation, standard error of the mean, and confidence interval (default 95%).
##   data: a data frame.
##   measurevar: the name of a column that contains the variable to be summariezed
##   groupvars: a vector containing names of columns that contain grouping variables
##   na.rm: a boolean that indicates whether to ignore NA's
##   conf.interval: the percent range of the confidence interval (default is 95%)
summarySE <- function(data=NULL, measurevar, groupvars=NULL, na.rm=FALSE,
                      conf.interval=.95, .drop=TRUE) {
  library(plyr)
  
  # New version of length which can handle NA's: if na.rm==T, don't count them
  length2 <- function (x, na.rm=FALSE) {
    if (na.rm) sum(!is.na(x))
    else       length(x)
  }
  
  # This does the summary. For each group's data frame, return a vector with
  # N, mean, and sd
  datac <- ddply(data, groupvars, .drop=.drop,
                 .fun = function(xx, col) {
                   c(N    = length2(xx[[col]], na.rm=na.rm),
                     mean = mean   (xx[[col]], na.rm=na.rm),
                     sd   = sd     (xx[[col]], na.rm=na.rm)
                   )
                 },
                 measurevar
  )
  
  # Rename the "mean" column
  datac <- rename(datac, c("mean" = measurevar))
  
  datac$se <- datac$sd / sqrt(datac$N)  # Calculate standard error of the mean
  
  # Confidence interval multiplier for standard error
  # Calculate t-statistic for confidence interval:
  # e.g., if conf.interval is .95, use .975 (above/below), and use df=N-1
  ciMult <- qt(conf.interval/2 + .5, datac$N-1)
  datac$ci <- datac$se * ciMult
  
  return(datac)
}
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

dt.6$weightedAreaUnderROC <- as.numeric(dt.6$weightedAreaUnderROC)
dt.7$weightedAreaUnderROC <- as.numeric(dt.7$weightedAreaUnderROC)
dt.8$weightedAreaUnderROC <- as.numeric(dt.8$weightedAreaUnderROC)
dt.9$weightedAreaUnderROC <- as.numeric(dt.9$weightedAreaUnderROC)
dt.10$weightedAreaUnderROC <- as.numeric(dt.10$weightedAreaUnderROC)

dt.1$errorRate <- dt.1$errorRate*100
dt.2$errorRate <- dt.2$errorRate*100
dt.3$errorRate <- dt.3$errorRate*100
dt.4$errorRate <- dt.4$errorRate*100
dt.5$errorRate <- dt.5$errorRate*100

dt.6$weightedAreaUnderROC <- dt.6$weightedAreaUnderROC*100
dt.7$weightedAreaUnderROC <- dt.7$weightedAreaUnderROC*100
dt.8$weightedAreaUnderROC <- dt.8$weightedAreaUnderROC*100
dt.9$weightedAreaUnderROC <- dt.9$weightedAreaUnderROC*100
dt.10$weightedAreaUnderROC <- dt.10$weightedAreaUnderROC*100

dt.6 <- dt.6[which(!is.na(dt.6$weightedAreaUnderROC)),]
dt.7 <- dt.7[which(!is.na(dt.7$weightedAreaUnderROC)),]
dt.8 <- dt.8[which(!is.na(dt.8$weightedAreaUnderROC)),]
dt.9 <- dt.9[which(!is.na(dt.9$weightedAreaUnderROC)),]
dt.10 <- dt.10[which(!is.na(dt.10$weightedAreaUnderROC)),]

dt.1$engine <- "Our Engine(KMeans-ErrorRate)"
dt.2$engine <- "Our Engine(GMeans-ErrorRate)"
dt.3$engine <- "Auto-Weka(1-Min-ErrorRate)"
dt.4$engine <- "Auto-Weka(2-Min-ErrorRate)"
dt.5$engine <- "Auto-Weka(15-Min-ErrorRate)"

dt.6$engine <- "Our Engine(KMeans-AUC)"
dt.7$engine <- "Our Engine(GMeans-AUC)"
dt.8$engine <- "Auto-Weka(1-Min-AUC)"
dt.9$engine <- "Auto-Weka(2-Min-AUC)"
dt.10$engine <- "Auto-Weka(15-Min-AUC)"

sum.dt.1 <- summarySE(dt.1, measurevar="errorRate", groupvars=c("dataset"))
sum.dt.2 <- summarySE(dt.2, measurevar="errorRate", groupvars=c("dataset"))
sum.dt.3 <- summarySE(dt.3, measurevar="errorRate", groupvars=c("dataset"))
sum.dt.4 <- summarySE(dt.4, measurevar="errorRate", groupvars=c("dataset"))
sum.dt.5 <- summarySE(dt.5, measurevar="errorRate", groupvars=c("dataset"))

sum.dt.6 <- summarySE(dt.6, measurevar="weightedAreaUnderROC", groupvars=c("dataset"))
sum.dt.7 <- summarySE(dt.7, measurevar="weightedAreaUnderROC", groupvars=c("dataset"))
sum.dt.8 <- summarySE(dt.8, measurevar="weightedAreaUnderROC", groupvars=c("dataset"))
sum.dt.9 <- summarySE(dt.9, measurevar="weightedAreaUnderROC", groupvars=c("dataset"))
sum.dt.10 <- summarySE(dt.10, measurevar="weightedAreaUnderROC", groupvars=c("dataset"))

ds.names <- sum.dt.1$dataset

summarize <- function(sum.dt.1, sum.dt.2, sum.dt.3, sum.dt.4, sum.dt.5, metric){
  sum.dt.1[c(metric, "se")] <- apply(sum.dt.1[c(metric, "se")], 2, function(x) formatC(x, digits = 2, format = "f"))
  sum.dt.2[c(metric, "se")] <- apply(sum.dt.2[c(metric, "se")], 2, function(x) formatC(x, digits = 2, format = "f"))
  sum.dt.3[c(metric, "se")] <- apply(sum.dt.3[c(metric, "se")], 2, function(x) formatC(x, digits = 2, format = "f"))
  sum.dt.4[c(metric, "se")] <- apply(sum.dt.4[c(metric, "se")], 2, function(x) formatC(x, digits = 2, format = "f"))
  sum.dt.5[c(metric, "se")] <- apply(sum.dt.5[c(metric, "se")], 2, function(x) formatC(x, digits = 2, format = "f"))
  sum.dt.1[c(metric, "se")] <- apply(sum.dt.1[c(metric, "se")], 2, function(x) formatC(x, digits = 2, format = "f"))
  sum.dt.2[c(metric, "se")] <- apply(sum.dt.2[c(metric, "se")], 2, function(x) formatC(x, digits = 2, format = "f"))
  sum.dt.3[c(metric, "se")] <- apply(sum.dt.3[c(metric, "se")], 2, function(x) formatC(x, digits = 2, format = "f"))
  sum.dt.4[c(metric, "se")] <- apply(sum.dt.4[c(metric, "se")], 2, function(x) formatC(x, digits = 2, format = "f"))
  sum.dt.5[c(metric, "se")] <- apply(sum.dt.5[c(metric, "se")], 2, function(x) formatC(x, digits = 2, format = "f"))
  
  sum.dt.1[,metric] <- as.numeric(sum.dt.1[,metric])
  sum.dt.2[,metric] <- as.numeric(sum.dt.2[,metric])
  sum.dt.3[,metric] <- as.numeric(sum.dt.3[,metric])
  sum.dt.4[,metric] <- as.numeric(sum.dt.4[,metric])
  sum.dt.5[,metric] <- as.numeric(sum.dt.5[,metric])
  
  val1 <- sum.dt.1[,metric] 
  val2 <- sum.dt.2[,metric] 
  val3 <- sum.dt.3[,metric] 
  val4 <- sum.dt.4[,metric] 
  val5 <- sum.dt.5[,metric] 
  
  # txt1 <- paste0(trimws(sprintf("%.2f", sum.dt.1[,metric])), "($backslashpm$", sum.dt.1[,"se"], ")")
  # txt2 <- paste0(trimws(sprintf("%.2f", sum.dt.2[,metric])), "($backslashpm$", sum.dt.2[,"se"], ")")
  # txt3 <- paste0(trimws(sprintf("%.2f", sum.dt.3[,metric])), "($backslashpm$", sum.dt.3[,"se"], ")")
  # txt4 <- paste0(trimws(sprintf("%.2f", sum.dt.4[,metric])), "($backslashpm$", sum.dt.4[,"se"], ")")
  # txt5 <- paste0(trimws(sprintf("%.2f", sum.dt.5[,metric])), "($backslashpm$", sum.dt.5[,"se"], ")")
  
  txt1 <- paste0(trimws(sprintf("%.2f", sum.dt.1[,metric])))
  txt2 <- paste0(trimws(sprintf("%.2f", sum.dt.2[,metric])))
  txt3 <- paste0(trimws(sprintf("%.2f", sum.dt.3[,metric])))
  txt4 <- paste0(trimws(sprintf("%.2f", sum.dt.4[,metric])))
  txt5 <- paste0(trimws(sprintf("%.2f", sum.dt.5[,metric])))
  
  merge <- data.frame(val1, val2, val3, val4, val5)
  result <- data.frame()
  for (i in 1:nrow(merge)){
    row <- as.double(merge[i,])
    if (metric == "errorRate"){
      max.index <- which.min(row)
    } else {
      max.index <- which.max(row) 
    }

    r1 <- txt1[i]
    r2 <- txt2[i]
    r3 <- txt3[i]
    r4 <- txt4[i]
    r5 <- txt5[i]
    if (max.index == 1){
      r1 <- paste0("backslashtextbf{", r1, "}")
    } else if (max.index == 2){
      r2 <- paste0("backslashtextbf{", r2, "}")
    } else if (max.index == 3){
      r3 <- paste0("backslashtextbf{", r3, "}")
    } else if (max.index == 4){
      r4 <- paste0("backslashtextbf{", r4, "}")
    } else if (max.index == 5){
      r5 <- paste0("backslashtextbf{", r5, "}")
    }
    comb <- data.frame(r1, r2, r3, r4, r5)
    result <- rbind(result, comb)
  }
  return (result)
}
result1 <- summarize(sum.dt.1, sum.dt.2, sum.dt.3, sum.dt.4, sum.dt.5, "errorRate")
result2 <- summarize(sum.dt.6, sum.dt.7, sum.dt.8, sum.dt.9, sum.dt.10, "weightedAreaUnderROC")

dt.latex <- cbind(ds.names, result1, result2)
xtable(dt.latex)

t.test(sum.dt.1$errorRate, sum.dt.3$errorRate, paired = T)
t.test(sum.dt.2$errorRate, sum.dt.3$errorRate, paired = T)
t.test(sum.dt.1$errorRate, sum.dt.4$errorRate, paired = T)
t.test(sum.dt.2$errorRate, sum.dt.4$errorRate, paired = T)
t.test(sum.dt.1$errorRate, sum.dt.5$errorRate, paired = T)
t.test(sum.dt.2$errorRate, sum.dt.5$errorRate, paired = T)

t.test(sum.dt.6$weightedAreaUnderROC, sum.dt.8$weightedAreaUnderROC, paired = T)
t.test(sum.dt.7$weightedAreaUnderROC, sum.dt.8$weightedAreaUnderROC, paired = T)
t.test(sum.dt.6$weightedAreaUnderROC, sum.dt.9$weightedAreaUnderROC, paired = T)
t.test(sum.dt.7$weightedAreaUnderROC, sum.dt.9$weightedAreaUnderROC, paired = T)
t.test(sum.dt.6$weightedAreaUnderROC, sum.dt.10$weightedAreaUnderROC, paired = T)
t.test(sum.dt.7$weightedAreaUnderROC, sum.dt.10$weightedAreaUnderROC, paired = T)
