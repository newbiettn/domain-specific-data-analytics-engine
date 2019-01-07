rm(list = ls(all = T))
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
library(ggplot2)
library(wesanderson) #color palette for ggplot
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

dt.1$timeElapsed <- (dt.1$timeElapsed/(1000*60))
dt.2$timeElapsed <- (dt.2$timeElapsed/(1000*60))
dt.3$timeElapsed <- (dt.3$timeElapsed/(1000*60))
dt.4$timeElapsed <- (dt.4$timeElapsed/(1000*60))
dt.5$timeElapsed <- (dt.5$timeElapsed/(1000*60))
dt.6$timeElapsed <- (dt.6$timeElapsed/(1000*60))
dt.7$timeElapsed <- (dt.7$timeElapsed/(1000*60))
dt.8$timeElapsed <- (dt.8$timeElapsed/(1000*60))
dt.9$timeElapsed <- (dt.9$timeElapsed/(1000*60))
dt.10$timeElapsed <- (dt.10$timeElapsed/(1000*60))

dt.1 <- summarySE(dt.1, measurevar="timeElapsed", groupvars=c("dataset"))
dt.2 <- summarySE(dt.2, measurevar="timeElapsed", groupvars=c("dataset"))
dt.3 <- summarySE(dt.3, measurevar="timeElapsed", groupvars=c("dataset"))
dt.4 <- summarySE(dt.4, measurevar="timeElapsed", groupvars=c("dataset"))
dt.5 <- summarySE(dt.5, measurevar="timeElapsed", groupvars=c("dataset"))
dt.6 <- summarySE(dt.6, measurevar="timeElapsed", groupvars=c("dataset"))
dt.7 <- summarySE(dt.7, measurevar="timeElapsed", groupvars=c("dataset"))
dt.8 <- summarySE(dt.8, measurevar="timeElapsed", groupvars=c("dataset"))
dt.9 <- summarySE(dt.9, measurevar="timeElapsed", groupvars=c("dataset"))
dt.10 <- summarySE(dt.10, measurevar="timeElapsed", groupvars=c("dataset"))

dt.1$engine <- "S1"
dt.2$engine <- "S2"
dt.3$engine <- "Auto-Weka(1m)"
dt.4$engine <- "Auto-Weka(2m)"
dt.5$engine <- "Auto-Weka(15m)"
dt <- rbind(dt.1, dt.2, dt.3, dt.4, dt.5)
# dt <- dt[order(dt$timeElapsed),]
# dt$dataset <- as.character(dt$dataset)
# dt$dataset
# dt$dataset <- factor(dt$dataset, levels = unique(dt$dataset))
# dt$dataset

pd <- position_dodge(0) # move them .05 to the left and right
ggplot(dt, aes(x = dataset, y = timeElapsed, colour = engine, group = engine)) +
  geom_errorbar(aes(ymin = timeElapsed - se, ymax = timeElapsed + se), width=.8, position=pd) +
  geom_line(position=pd, size = 0.3) +
  geom_point(position=pd, alpha = 0.8, size = 0.8) +
  theme_linedraw() +
  theme(panel.grid.minor = element_blank()) +
  theme(legend.position = "top",
        legend.background = element_rect(fill="gray90", size=0),
        axis.text.x = element_text(angle = 45, hjust = 1)) +
  xlab("Dataset") +
  ylab("Runtime (seconds)") +
  guides(col=guide_legend(title="", nrow = 1))

ggsave("figs/runtime_experiment.png")



