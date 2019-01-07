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
dt.1 <- read.csv("myengine_evaluation_result-errorrate-gmeans_same_ranks-without_optimization.csv")
dt.2 <- read.csv("results_backup/autoweka_evaluation_result_errorrate_8000Mb_10threads_1m.csv")
dt.3 <- read.csv("results_backup/autoweka_evaluation_result_errorrate_8000Mb_10threads_2m.csv")
dt.4 <- read.csv("results_backup/autoweka_evaluation_result_errorrate_8000Mb_10threads_15m_seed_1_150.csv")

boxplot(dt.1$timeElapsed)

dt.1 <- summarySE(dt.1, measurevar="errorRate", groupvars=c("dataset"))
dt.2 <- summarySE(dt.2, measurevar="errorRate", groupvars=c("dataset"))
dt.3 <- summarySE(dt.3, measurevar="errorRate", groupvars=c("dataset"))
dt.4 <- summarySE(dt.4, measurevar="errorRate", groupvars=c("dataset"))


dt.1$engine <- "System (GMeans)"
dt.2$engine <- "Auto-Weka(1-Min)"
dt.3$engine <- "Auto-Weka(2-Min)"
dt.4$engine <- "Auto-Weka(15-Min)"
dt <- rbind(dt.1, dt.2, dt.3, dt.4)

pd <- position_dodge(0) # move them .05 to the left and right
ggplot(dt, aes(x = dataset, y = errorRate, colour = engine, group = engine)) +
  geom_errorbar(aes(ymin = errorRate - se, ymax = errorRate + se), width=.8, position=pd) +
  geom_line(position=pd, size = 0.3) +
  geom_point(position=pd, alpha = 0.8, size = 0.8) +
  theme_bw() +
  theme(legend.position="bottom",
        legend.background = element_rect(fill="gray90", size=1, linetype="dotted"),
        axis.text.x = element_text(angle = 90, hjust = 1)) +
  xlab("Dataset") +
  ylab("Error Rate") +
  guides(col=guide_legend(title="", nrow = 2))
ggsave("figs/errorrate_experiment_gmeans.png")

