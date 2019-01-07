rm(list = ls(all = T))
################################################################################
library(dplyr)
################################################################################
dt <- read.csv("temp.csv", header = TRUE)
dt$FLOW <- as.factor(dt$FLOW)
n_levels <- length(levels(factor(dt$FLOW)))

# remove workflows that do not perform on all datasets
# for (cluster in 0:max(dt$CLUSTER)){
    temp.dt <- dt %>%
      # filter(CLUSTER == cluster) %>%
      as.data.frame()
    num.datasets <- length(unique(temp.dt$DATASET))
    tb <- table(temp.dt$CLASSIFIER, temp.dt$ATTRIBUTESELECTION)
    tb
    rnames <- row.names(tb)
    cnames <- colnames(tb)
    for (i in 1:nrow(tb)){ #row
      for(j in 1:ncol(tb)){ #col
        if (tb[i,j] < num.datasets && tb[i,j] > 0){
          c <- rnames[i]
          a <- cnames[j]
          dt <- dt[!(dt$CLASSIFIER == c & 
                       dt$ATTRIBUTESELECTION == a),]
          print(paste0(c, "/", a))
        }
      }
    }
# }
dt <- dt %>%
  group_by(DATASET) %>%
  arrange(DATASET, desc(MEANAUC)) %>%
  mutate(rankByAUC = rank(-MEANAUC, ties.method = "min")) %>%
  as.data.frame()

dt <- dt %>%
  group_by(DATASET, rankByAUC) %>%
  arrange(DATASET, rankByAUC, MEANTIMEELAPSED) %>%
  mutate(rankByTime = rank(MEANTIMEELAPSED, ties.method = "random")) %>%
  as.data.frame()

myrank <- function(rankByErr, rankByTime){
  newrank <- c()
  if (length(unique(rankByErr)) == 1){
    newrank <- rankByErr + rankByTime - 1
  } else {
    newrank <- rankByErr
  }
  return (newrank)
}
dt <- dt %>%
  group_by(DATASET, rankByAUC, rankByTime) %>%
  arrange(DATASET, rankByAUC, rankByTime) %>%
  mutate(rank = myrank(rankByAUC, rankByTime)) %>%
  as.data.frame()

dt$MEANAUC <- as.numeric(as.character(dt$MEANAUC))
dt$MEANFMEASURE <- as.numeric(as.character(dt$MEANFMEASURE))
dt$MEANPRECISION <- as.numeric(as.character(dt$MEANPRECISION))
dt$MEANRECALL <- as.numeric(as.character(dt$MEANRECALL))
dt$MEANERRORRATE <- as.numeric(as.character(dt$MEANERRORRATE))
dt$MEANTIMEELAPSED <- as.numeric(as.character(dt$MEANTIMEELAPSED))

mask <- apply(dt[c("MEANAUC", "MEANFMEASURE", "MEANPRECISION",
                   "MEANRECALL", "MEANERRORRATE", "MEANTIMEELAPSED")], 2, is.nan)

dt[is.na(dt)] <- NA

write.csv(dt, 
          "experiment_workflow_ranking_by_dataset.csv",
          quote = FALSE,
          row.names = FALSE,
          na = "")
################################################################################
