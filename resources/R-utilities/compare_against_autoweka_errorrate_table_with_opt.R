rm(list = ls(all = T))
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
dt.1 <- read.csv("myengine_evaluation_result-errorrate-kmeans_same_ranks-without_optimization.csv")
dt.2 <- read.csv("myengine_evaluation_result-errorrate-kmeans_same_ranks-with_optimization.csv")
dt.3 <- read.csv("myengine_evaluation_result-errorrate-gmeans_same_ranks-without_optimization.csv")
dt.4 <- read.csv("myengine_evaluation_result-errorrate-gmeans_same_ranks-with_optimization.csv")
dt.5 <- read.csv("results_backup/autoweka_evaluation_result_errorrate_8000Mb_10threads_15m_seed_1_150.csv")

dt.1$errorRate <- dt.1$errorRate*100
dt.2$errorRate <- dt.2$errorRate*100
dt.3$errorRate <- dt.3$errorRate*100
dt.4$errorRate <- dt.4$errorRate*100
dt.5$errorRate <- dt.5$errorRate*100

dt.1$engine <- "Our Engine(KMeans-NoHyperOpt)"
dt.2$engine <- "Our Engine(KMeans-HyperOpt)"
dt.3$engine <- "Our Engine(GMeans-NoHyperOpt)"
dt.4$engine <- "Our Engine(GMeans-HyperOpt)"
dt.5$engine <- "Auto-Weka(15-Min)"

summarize <- function(our.dt, autoweka.dt, name){
  ours.vs.autoweka <- our.dt %>%
    inner_join(autoweka.dt, by = c("dataset", "seed")) %>%
    as.data.frame()
  ours.vs.autoweka <- ours.vs.autoweka %>%
    mutate(errorRateDiff = errorRate.x - errorRate.y) %>%
    as.data.frame()
  ours.vs.autoweka <- ours.vs.autoweka[c("dataset", "errorRateDiff")]
  sum.dt <- summarySE(ours.vs.autoweka, measurevar="errorRateDiff", groupvars=c("dataset"))
  sum.dt[c("errorRateDiff", "sd", "se", "ci")] <- apply(sum.dt[c("errorRateDiff", "sd", "se", "ci")], 2, function(x) format(round(x, 2), nsmall = 2))
  for (i in 1:nrow(sum.dt)){
    val <- as.numeric(sum.dt[i, c("errorRateDiff")])
    if (val >= 0) {
      sum.dt[i, c("errorRateDiff")] <- paste0("\\textbf{", sum.dt[i, c("errorRateDiff")], "}")
      sum.dt[i, c("se")] <- paste0("\\textbf{", sum.dt[i, c("se")], "}")
      
    }
  }
  sum.dt$case <- name
  return(sum.dt)
}
kmeans.vs.autoweka.1m <- summarize(dt.1, dt.5, "case 1")
kmeans.vs.autoweka.2m <- summarize(dt.2, dt.5, "case 2")
gmeans.vs.autoweka.1m <- summarize(dt.3, dt.5, "case 3")
gmeans.vs.autoweka.2m <- summarize(dt.4, dt.5, "case 4")

# Create latex table
latex.dt <- data.frame(id = 1:nrow(kmeans.vs.autoweka.1m))
latex.dt$dataset <- kmeans.vs.autoweka.1m$dataset

latex.dt$kmeans.vs.autoweka.1m <- paste0(kmeans.vs.autoweka.1m$errorRateDiff, "($\\pm$",
                                         kmeans.vs.autoweka.1m$se,
                                         ")")

latex.dt$kmeans.vs.autoweka.2m <- paste0(kmeans.vs.autoweka.2m$errorRateDiff, "($\\pm$",
                                         kmeans.vs.autoweka.2m$se,
                                         ")")
latex.dt$gmeans.vs.autoweka.1m <- paste0(gmeans.vs.autoweka.1m$errorRateDiff, "($\\pm$",
                                         gmeans.vs.autoweka.1m$se,
                                         ")")
latex.dt$gmeans.vs.autoweka.2m <- paste0(gmeans.vs.autoweka.2m$errorRateDiff, "($\\pm$",
                                         gmeans.vs.autoweka.2m$se,
                                         ")")
latex.dt$id <- NULL

xtable(latex.dt)

stargazer::stargazer(latex.dt, summary = FALSE)
Hmisc::latex(latex.dt)
