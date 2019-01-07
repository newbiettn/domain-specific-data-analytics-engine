library(dplyr)
library(ggplot2)
library(xtable)
################################################################################
# Get a list of datasets
dt <- read.csv("new_results_backup/mf_uci.csv")
dt <- dt[c("DATASET", "NUMBEROFCLASSES", "NUMBEROFFEATURES", "NUMBEROFINSTANCES")]
dt <- dt %>%
  arrange(DATASET) %>%
  as.data.frame()
xtable(dt)



